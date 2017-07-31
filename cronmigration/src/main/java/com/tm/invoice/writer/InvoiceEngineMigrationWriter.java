package com.tm.invoice.writer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.engagement.domain.Engagement;
import com.tm.engagement.repository.EngagementRepository;
import com.tm.invoice.domain.AuditFields;
import com.tm.invoice.domain.InvoiceDetail;
import com.tm.invoice.domain.InvoiceExceptionDetails;
import com.tm.invoice.domain.InvoiceQueue;
import com.tm.invoice.dto.InvoiceDTO;
import com.tm.invoice.dto.InvoiceExceptionDetailsDTO;
import com.tm.invoice.dto.UserPreferenceDTO;
import com.tm.invoice.mapper.HistoricalMapper;
import com.tm.invoice.mapper.InvoiceQueueMapper;
import com.tm.invoice.mongo.domain.Historical;
import com.tm.invoice.mongo.domain.InvoiceAttachments;
import com.tm.invoice.mongo.repository.HistoricalRepository;
import com.tm.invoice.mongo.repository.InvoiceDetailRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.processor.InvoiceEngineMigrationProcessor;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.repository.TimesheetRepository;
import com.tm.util.InvoiceConstants;
import com.tm.util.MailManager;

@Service
public class InvoiceEngineMigrationWriter {

	private static final Logger log = LoggerFactory.getLogger(InvoiceEngineMigrationWriter.class);

	private static final String Y_STR = "Y";

	private InvoiceQueueRepository invoiceQueueRepository;

	private MongoTemplate mongoTemplate;

	private InvoiceDetailRepository invoiceDetailRepository;

	private EngagementRepository engagementRepository;

	private TimesheetRepository timesheetRepository;
	
	private HistoricalRepository historicalRepository;

	@Autowired
	MailManager mailManager;

	public InvoiceEngineMigrationWriter(InvoiceDetailRepository invoiceDetailRepository,
			TimesheetRepository timesheetRepository, InvoiceQueueRepository invoiceQueueRepository,
			HistoricalRepository historicalRepository, MongoTemplate mongoTemplate, EngagementRepository engagementRepository) {
		this.invoiceDetailRepository = invoiceDetailRepository;
		this.timesheetRepository = timesheetRepository;
		this.invoiceQueueRepository = invoiceQueueRepository;
		this.historicalRepository = historicalRepository;
		this.mongoTemplate = mongoTemplate;
		this.engagementRepository = engagementRepository;
	}

	public void startInvoiceEngineMigrationWriter(List<InvoiceDTO> invoiceDTOs) {

		if (CollectionUtils.isNotEmpty(invoiceDTOs)) {
			log.info("Writer invoiceDTO List size --- {}", invoiceDTOs.size());
			try {
				saveInvoices(invoiceDTOs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void saveInvoices(List<InvoiceDTO> invoices) {

		List<UUID> invoiceQueueIdsTobeRemoved = new ArrayList<>();
		List<InvoiceQueue> invoiceQueues = new ArrayList<>();
		List<InvoiceDetail> invoiceDetails = new ArrayList<>();
		List<Timesheet> timesheets = new ArrayList<>();
		List<Historical> historicals = new ArrayList<>();
		List<InvoiceAttachments> invoiceAttachmentList = new ArrayList<>();
		invoices.stream().forEach(invoice -> {

			// Preparing invoice queue ids to be removed
			UUID invoiceQueueID = invoice.getInvoiceQueueId();
			if (Objects.nonNull(invoiceQueueID)) {
				invoiceQueueIdsTobeRemoved.add(invoiceQueueID);
			}

			if (StringUtils.isBlank(invoice.getExceptionReason())) {
				detectingEnagagementAmount(invoice);
			}

			// Preparing invoice queues
			UUID invoiceQueueId = ResourceUtil.generateUUID();
			InvoiceQueue invoiceQueue = prepareInvoiceQueue(invoice, invoiceQueueId);
			invoiceQueues.add(invoiceQueue);

			// Preparing historicals
			historicals.add(prepareHistorical(invoiceQueue));

			// Preparing invoice details
			prepareInvoiceDetails(invoice, invoiceQueueId, invoiceDetails);

			// Preparing time sheets
			List<Timesheet> timeSheetList = invoice.getTimesheetList();
			if (timeSheetList != null) {
				timesheets.addAll(timeSheetList);
			}

			try {
				// Preparing attachments
				prepareInvoiceAttachments(invoice, invoiceAttachmentList);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Writer Attachment issue {} ", e);
			}
		});

		removeExistingInvoiceQueueDetails(invoiceQueueIdsTobeRemoved);

		try {
			invoiceDetailRepository.save(invoiceDetails);
		} catch (Exception e) {
			log.error("Bulk saving of invoice details is failed", e);
			invoiceDetails.stream().forEach(invoiceDetail -> invoiceDetailRepository.save(invoiceDetail));
		}

		try {
			timesheetRepository.save(timesheets);
		} catch (Exception e) {
			log.error("Bulk saving of time sheets is failed", e);
			timesheets.stream().forEach(timesheet -> timesheetRepository.save(timesheet));
		}

		invoiceAttachmentList.stream().forEach(
				invoiceAttachments -> mongoTemplate.save(invoiceAttachments, InvoiceConstants.INVOICE_ATTACHMENTS));
		checkPreferenceForEmailDelivery(invoices);

		try {
			invoiceQueueRepository.save(invoiceQueues);
		} catch (Exception e) {
			log.error("Bulk removal of invoice details is failed", e);
			invoiceQueues.stream().forEach(invoiceQueue -> invoiceQueueRepository.save(invoiceQueue));
		}

		try {
			historicalRepository.save(historicals);
		} catch (Exception e) {
			log.error("Bulk saving of historical is failed", e);
			historicals.stream().forEach(historical -> historicalRepository.save(historical));
		}
	}

	private void removeExistingInvoiceQueueDetails(List<UUID> invoiceQueueIdsTobeRemoved) {
		if (CollectionUtils.isNotEmpty(invoiceQueueIdsTobeRemoved)) {
			// Bulk Save, Update, Removal || Single Hit if bulk hit fails
			try {
				removeAllExistingInvoiceDetailsFromGrid(invoiceQueueIdsTobeRemoved, Arrays.asList(
						InvoiceConstants.TIMESHEET_STR, InvoiceConstants.INVOICE_STR, InvoiceConstants.EXPENSE_STR));
			} catch (Exception e) {

				log.error("Bulk removal of existing invoice details from Grid is failed", e);
				try {
					invoiceQueueIdsTobeRemoved.stream().forEach(invoiceQueueIdToBeRemoved -> {
						removeAllExistingInvoiceDetailsFromGrid(Arrays.asList(invoiceQueueIdToBeRemoved),
								Arrays.asList(InvoiceConstants.TIMESHEET_STR, InvoiceConstants.INVOICE_STR,
										InvoiceConstants.EXPENSE_STR));
					});
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			try {
				removeInvoiceDetails(invoiceQueueIdsTobeRemoved);
			} catch (Exception e) {
				try {
					log.error("Bulk removal of invoice details is failed", e);
					invoiceQueueIdsTobeRemoved.stream().forEach(invoiceQueueIdToBeRemoved -> {
						removeInvoiceDetails(Arrays.asList(invoiceQueueIdToBeRemoved));
					});
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private InvoiceQueue prepareInvoiceQueue(InvoiceDTO invoice, UUID invoiceQueueId) {

		InvoiceQueue invoiceQueue = InvoiceQueueMapper.INSTANCE.invoiceDTOToInvoiceQueue(invoice);
		invoice.setInvoiceQueueId(invoiceQueueId);
		invoiceQueue.setId(invoiceQueueId);
		invoiceQueue.setCreated(populateCreatedAuditFields());
		List<InvoiceExceptionDetailsDTO> invoiceExceptionDetailDTOs = invoice.getInvoiceExceptionDetail();
		List<InvoiceExceptionDetails> invoiceExceptionDetails = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(invoiceExceptionDetailDTOs)) {
			invoiceExceptionDetails = InvoiceQueueMapper.INSTANCE
					.invoiceExceptionDetailsDTOsToInvoiceExceptionDetails(invoiceExceptionDetailDTOs);

			for (InvoiceExceptionDetails invoiceExceptionDetails2 : invoiceExceptionDetails) {
				invoiceExceptionDetails2.setInvoiceNumber(invoice.getInvoiceNumber());
				invoiceExceptionDetails2.setFileNumber(invoice.getInvoiceNumber());
			}

		}
		invoiceQueue.setInvoiceExceptionDetail(invoiceExceptionDetails);
		invoiceQueue.setInvoiceSetupName(invoiceQueue.getInvoiceSetupName().trim());
		return invoiceQueue;
	}

	private Historical prepareHistorical(InvoiceQueue invoiceQueue) {

		Historical historical = HistoricalMapper.INSTANCE.invoiceQueueToHistorical(invoiceQueue);
		historical.setId(ResourceUtil.generateUUID());

		return historical;
	}

	private void prepareInvoiceDetails(InvoiceDTO invoice, UUID invoiceQueueId, List<InvoiceDetail> invoiceDetails) {

		List<InvoiceDetail> invoiceDetailList = invoice.getInvoiceDetails();
		if (CollectionUtils.isNotEmpty(invoiceDetailList)) {
			invoiceDetailList.stream().forEach(invoiceDetail -> {
				invoiceDetail.set_id(UUID.randomUUID());
				invoiceDetail.setInvoiceQueueId(invoiceQueueId);
			});
			invoiceDetails.addAll(invoiceDetailList);
		}
	}

	private void prepareInvoiceAttachments(InvoiceDTO invoice, List<InvoiceAttachments> invoiceAttachmentList) {

		List<InvoiceDTO> invoiceAttachmentsDTOs = new ArrayList<>();
		List<InvoiceDTO> timesheetAttachmentsDTOs = new ArrayList<>();
		List<InvoiceDTO> expenseAttachmentsDTOs = new ArrayList<>();
		UserPreferenceDTO userPreference = invoice.getUserPreference();
		if (Objects.nonNull(userPreference)) {
			if (invoice.getTimesheetAttachment().equals(Y_STR)) {
				timesheetAttachmentsDTOs.add(invoice);
			}
			if (invoice.getBillableExpensesAttachment().equals(Y_STR)) {
				expenseAttachmentsDTOs.add(invoice);
			}
		}
		invoiceAttachmentsDTOs.add(invoice);

		invoiceAttachmentList
				.addAll(prepareInvoiceAttachmentsBySubType(timesheetAttachmentsDTOs, InvoiceConstants.TIMESHEET_STR));
		invoiceAttachmentList
				.addAll(prepareInvoiceAttachmentsBySubType(invoiceAttachmentsDTOs, InvoiceConstants.INVOICE_STR));
		invoiceAttachmentList
				.addAll(prepareInvoiceAttachmentsBySubType(expenseAttachmentsDTOs, InvoiceConstants.EXPENSE_STR));
	}

	private List<InvoiceAttachments> prepareInvoiceAttachmentsBySubType(List<InvoiceDTO> invoices, String subType) {

		List<InvoiceAttachments> invoiceAttachmentList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(invoices)) {
			invoices.stream().forEach(invoice -> {
				byte[] bytes = getBytes(subType, invoice);
				if (ObjectUtils.notEqual(null, bytes)) {
					InvoiceAttachments invoiceAttachments = new InvoiceAttachments();
					GridFS gridFS = new GridFS(mongoTemplate.getDb(), InvoiceConstants.INVOICE);
					InputStream inputStream = new ByteArrayInputStream(bytes);
					invoiceAttachments.setSourceReferenceId(invoice.getInvoiceQueueId().toString());
					invoiceAttachments.setSourceReferenceName(invoice.getInvoicePDF().getSourceReferenceName());
					GridFSInputFile gfsFile = gridFS.createFile(inputStream);
					gfsFile.put(InvoiceConstants.SRC_REF_ID, invoice.getInvoiceQueueId().toString());
					gfsFile.put(InvoiceConstants.SRC_REF_NAME, invoice.getInvoicePDF().getSourceReferenceName());
					gfsFile.put(InvoiceConstants.INVOICE_ATTACHMENT_ID, UUID.randomUUID());
					gfsFile.setContentType(invoice.getInvoicePDF().getContentType());
					gfsFile.setFilename(invoice.getInvoicePDF().getFileName());
					gfsFile.put(InvoiceConstants.SUB_TYPE_STR, subType);
					gfsFile.setChunkSize(bytes.length);
					gfsFile.save();
					invoiceAttachmentList.add(invoiceAttachments);
				}
			});
		}

		return invoiceAttachmentList;
	}

	private void removeInvoiceDetails(List<UUID> invoiceQueueIds) {

		Query query = new Query();
		query.addCriteria(Criteria.where(InvoiceConstants.SRC_REF_ID).in(invoiceQueueIds));
		mongoTemplate.remove(query, InvoiceAttachments.class);

		Query detailQuery = new Query();
		detailQuery.addCriteria(Criteria.where(InvoiceConstants.INVOICE_QUEUE_ID_STR).in(invoiceQueueIds));
		mongoTemplate.remove(detailQuery, InvoiceDetail.class);

		Query queueQuery = new Query();
		queueQuery.addCriteria(Criteria.where(InvoiceConstants.ID_STR).in(invoiceQueueIds));
		mongoTemplate.remove(queueQuery, InvoiceQueue.class);
	}

	private void checkPreferenceForEmailDelivery(List<InvoiceDTO> invoices) {

		invoices.stream().filter(
				invoice -> StringUtils.equals(invoice.getStatus(), InvoiceEngineMigrationProcessor.STATUS_DELIVERED_STR)
						&& Objects.nonNull(invoice.getUserPreference()) && invoice.getUserPreference().isAutoDelivery())
				.forEach(invoice -> mailManager.sendEmailForInvoice(invoice));
	}

	private void removeAllExistingInvoiceDetailsFromGrid(List<UUID> invoiceUUIDs, List<String> srcRefNames) {

		log.info("************* removeAllExistingInvoiceDetailsFromGrid Starts ************* {} ", srcRefNames);
		GridFS gridFS = new GridFS(mongoTemplate.getDb(), InvoiceConstants.INVOICE);
		BasicDBObject query = new BasicDBObject();
		query.put(InvoiceConstants.SRC_REF_ID, new BasicDBObject("$in", invoiceUUIDs));
		query.put(InvoiceConstants.SRC_REF_NAME, new BasicDBObject("$in", srcRefNames));
		gridFS.remove(query);
		log.info("************* removeAllExistingInvoiceDetailsFromGrid End*************");
	}

	private byte[] getBytes(String subType, InvoiceDTO invoice) {

		byte[] bytes = null;
		if (null != invoice.getInvoicePDF() && subType.equals(InvoiceConstants.INVOICE_STR)) {
			bytes = invoice.getInvoicePDF().getContent();
		}
		if (null != invoice.getTimesheetPDF() && subType.equals(InvoiceConstants.TIMESHEET_STR)) {
			bytes = invoice.getTimesheetPDF().getContent();
		}
		if (null != invoice.getExpensePDF() && subType.equals(InvoiceConstants.EXPENSE_STR)) {
			bytes = invoice.getExpensePDF().getContent();
		}
		return bytes;
	}

	private AuditFields populateCreatedAuditFields() {

		AuditFields auditFields = new AuditFields();
		auditFields.setOn(new Date());
		return auditFields;
	}

	public Engagement detectingEnagagementAmount(InvoiceDTO invoiceQueue) {

		log.info("************* Deducting Po Amount Starts *************");
		Engagement engagement = engagementRepository.findOne(invoiceQueue.getProjectId());

		BigDecimal balRevAmount = BigDecimal.ZERO;
		BigDecimal balExpAmount = BigDecimal.ZERO;

		boolean isUpdate = false;
		if (null != engagement) {
			log.info("************* Deducting Po Amount PO NUmber *************");
			if (Objects.nonNull(engagement.getBalanceRevenueAmount()) && null != invoiceQueue.getBillingAmount()) {

				balRevAmount = engagement.getBalanceRevenueAmount().subtract(invoiceQueue.getBillingAmount());
				isUpdate = true;
			}
			if (Objects.nonNull(engagement.getBalanceExpenseAmount()) && null != invoiceQueue.getExpenseAmount()
					&& !invoiceQueue.getExpenseAmount().equals(BigDecimal.ZERO)) {
				balExpAmount = engagement.getBalanceExpenseAmount().subtract(invoiceQueue.getExpenseAmount());

				if (balExpAmount.intValue() > 0) {
					engagementRepository.updateEngagementExpenseAmount(balExpAmount, engagement.getEngagementId());
				} else if (isUpdate) {
					balRevAmount = balRevAmount.subtract(balExpAmount);
				} else if (!isUpdate && Objects.nonNull(engagement.getBalanceRevenueAmount())) {
					balRevAmount = engagement.getBalanceRevenueAmount().subtract(balExpAmount);
				}
			}

			if (isUpdate && balRevAmount.intValue() >= 0) {
				engagementRepository.updateEngagementRevenueAmount(balRevAmount, engagement.getEngagementId());
			} else {
				populateExceptionDetails(invoiceQueue);
			}

			log.info("************* Deducting Balance Amount *************" + engagement.getBalanceRevenueAmount());

			log.info("************* Deducting Balance Amount *************" + engagement.getBalanceExpenseAmount());

		}
		log.info("************* Deducting Po Amount Ends *************");
		return engagement;
	}

	private List<InvoiceExceptionDetailsDTO> populateExceptionDetails(InvoiceDTO invoiceQueue) {

		List<InvoiceExceptionDetailsDTO> exceptionDetailsList = new ArrayList<>();
		InvoiceExceptionDetailsDTO exceptionDetails = new InvoiceExceptionDetailsDTO();
		exceptionDetails.setRejectComments(InvoiceConstants.LOW_PO_AMOUNT);
		exceptionDetails.setFileNumber(invoiceQueue.getInvoiceNumber());
		exceptionDetails.setStatus(InvoiceConstants.STATUS_INSUFFICIENT_BALANCE_STR);
		invoiceQueue.setExceptionSource(InvoiceConstants.STATUS_INSUFFICIENT_BALANCE_STR);
		invoiceQueue.setStatus(InvoiceConstants.STATUS_INSUFFICIENT_BALANCE_STR);
		invoiceQueue.setInvoiceExceptionDetail(exceptionDetailsList);
		exceptionDetailsList.add(exceptionDetails);
		return exceptionDetailsList;
	}
}