package com.tm.invoice.reader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.tm.common.domain.CompanyProfile;
import com.tm.common.domain.LookupView;
import com.tm.common.repository.CompanyProfileRepository;
import com.tm.common.repository.LookupViewRepository;
import com.tm.commonapi.util.CalculationUtil;
import com.tm.expense.domain.ExpenseImage;
import com.tm.expense.domain.ExpenseView;
import com.tm.expense.jpa.repository.ExpenseViewRepository;
import com.tm.invoice.constants.InvoiceEngineConstants;
import com.tm.invoice.domain.BillCycle.AccuringFlag;
import com.tm.invoice.domain.BillingQueue;
import com.tm.invoice.domain.InvoiceDetail;
import com.tm.invoice.domain.InvoiceQueue;
import com.tm.invoice.domain.InvoiceSetup;
import com.tm.invoice.domain.InvoiceSetup.ActiveFlag;
import com.tm.invoice.domain.InvoiceSetupOption;
import com.tm.invoice.domain.InvoiceTemplate;
import com.tm.invoice.domain.PoInvoiceSetupDetailsView;
import com.tm.invoice.dto.BillCycleDTO;
import com.tm.invoice.dto.BillToManagerDTO;
import com.tm.invoice.dto.BillingProfileDTO;
import com.tm.invoice.dto.ClientInfoDTO;
import com.tm.invoice.dto.DeliveryDTO;
import com.tm.invoice.dto.EmployeeAttachmentsDTO;
import com.tm.invoice.dto.InvoiceDTO;
import com.tm.invoice.dto.InvoiceExceptionDetailsDTO;
import com.tm.invoice.dto.InvoiceSetupBatchDTO;
import com.tm.invoice.dto.InvoiceSetupBillCycleDTO;
import com.tm.invoice.dto.InvoiceSetupDTO;
import com.tm.invoice.dto.InvoiceTemplateDTO;
import com.tm.invoice.dto.PurchaseOrderDTO;
import com.tm.invoice.dto.UserPreferenceDTO;
import com.tm.invoice.mapper.InvoiceSetupBillCycleMapper;
import com.tm.invoice.mongo.repository.BillingQueueRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.repository.BillCycleRepository;
import com.tm.invoice.repository.InvoiceSetupRepository;
import com.tm.invoice.repository.InvoiceTemplateRepository;
import com.tm.invoice.repository.PoInvoiceSetupDetailsViewRepository;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.domain.TimesheetDetails;
import com.tm.timesheetgeneration.repository.TimesheetRepository;
import com.tm.util.DateConversionUtil;
import com.tm.util.DateUtil;
import com.tm.util.InvoiceBillDateUtil;
import com.tm.util.InvoiceConstants;
import com.tm.util.TimesheetConstants;

@Component
public class InvoiceEngineMigrationReader {

	private static final Logger log = LoggerFactory.getLogger(InvoiceEngineMigrationReader.class);

	private static final String PREBILL = "PREBILL";
	private static final String REGULAR = "REGULAR";
	private static final String MONTHLY = "Monthly";
	private static final String SEMIMONTHLY = "Semi Monthly";
	private static final String FILE_UPLOAD_INDEX = "/";
	private List<PoInvoiceSetupDetailsView> inActivePoInvoiceSetupDetailList = null;
	private Date lastBillDate = null;
	private List<InvoiceDTO> invoiceDTOList = null;
	private static final String PDF = "pdf";
	private static final String THUMB_FOLDER = "thumb";
	private static final String YEAR = "Year(YYYY)";
	private static final String MONTH = "Month(MM)";
	private static final String YEAR_MONTH = "Month-Year(MM-YYYY)";
	//private static final List<String> STATUS_LIST = Arrays.asList(TimesheetConstants.APPROVED,TimesheetConstants.VERIFIED);
	private static final List<String> STATUS_LIST = Arrays.asList(TimesheetConstants.COMPLETED);
	@Autowired
	private PoInvoiceSetupDetailsViewRepository poInvoiceSetupDetailsViewRepository;

	@Autowired
	private BillCycleRepository billCycleRepository;
 
	@Autowired(required = true)
	private InvoiceQueueRepository invoiceQueueRepository;

	@Autowired
	private CompanyProfileRepository companyProfileRepository;

	@Autowired
	private InvoiceTemplateRepository invoiceTemplateRepository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private InvoiceSetupRepository invoiceSetupRepository;

	@Autowired
	private LookupViewRepository lookUpViewRepository;

	@Autowired
	private TimesheetRepository timesheetRepository;
 

	@Autowired
	private BillingQueueRepository billingQueueRepository;

	@Autowired
	private ExpenseViewRepository expenseViewRepository;

	private LocalDate runCronDate;
	private Date invoiceLiveDate;
	private int startingNumber = 100;
	private String separatorValue = "/";
	private String prefixValue = "Inv";
	private Map<UUID, Integer> startingNumberHashMap = new HashMap<>();
	@Value("${spring.application.expense-receipts}")
	private String FILE_UPLOAD_LOCATION = null;
	private String month = null;
	private String year = null;

	@Value("${spring.application.jasper-report}")
	private String jasperReport;
	
	public InvoiceEngineMigrationReader(PoInvoiceSetupDetailsViewRepository poInvoiceSetupDetailsViewRepository,
			InvoiceQueueRepository invoiceQueueRepository) {
		this.poInvoiceSetupDetailsViewRepository = poInvoiceSetupDetailsViewRepository;
		this.invoiceQueueRepository = invoiceQueueRepository;
	}

	public InvoiceSetupBatchDTO prepareInvoiceEngineReader(int fromId, int toId, LocalDate runCronDate,
			Date invoiceLiveDate) {
		this.runCronDate = runCronDate;
		this.invoiceLiveDate = invoiceLiveDate;
		InvoiceSetupBatchDTO invoiceBatchDTO = new InvoiceSetupBatchDTO();
		inActivePoInvoiceSetupDetailList = new ArrayList<>();
		invoiceDTOList = new ArrayList<>();
		DecimalFormat monthFormat = new DecimalFormat("00");
		month = monthFormat.format(LocalDate.now().getMonthValue());
		year = String.valueOf(LocalDate.now().getYear());
		getAllInvoiceSetupBillCycleDetails(fromId, toId);

		if (CollectionUtils.isNotEmpty(inActivePoInvoiceSetupDetailList)) {
			log.info("inActivePoInvoiceSetupDetailList size ---->{}", inActivePoInvoiceSetupDetailList.size());
			inActivePoInvoiceSetup(inActivePoInvoiceSetupDetailList);
		}
		if (CollectionUtils.isNotEmpty(invoiceDTOList)) {
			log.info("Reader invoiceDTOList size --- {}", invoiceDTOList.size());

			invoiceBatchDTO.setInvoiceDTOList(invoiceDTOList);
			invoiceBatchDTO.setRunCronDate(runCronDate);
		}
		return invoiceBatchDTO;
	}

	public InvoiceSetupBatchDTO prepareInvoiceExceptionReportEngineReader(int fromId, int toId, LocalDate runCronDate,
			Date invoiceLiveDate) {
		this.runCronDate = runCronDate;
		this.invoiceLiveDate = invoiceLiveDate;

		InvoiceSetupBatchDTO invoiceBatchDTO = new InvoiceSetupBatchDTO();
		invoiceDTOList = new ArrayList<>();
		getAllInvoiceQueues(fromId, toId);

		if (CollectionUtils.isNotEmpty(invoiceDTOList)) {
			log.info("Reader invoiceDTOList size ---{}", invoiceDTOList.size());

			invoiceBatchDTO.setInvoiceDTOList(invoiceDTOList);
			invoiceBatchDTO.setRunCronDate(runCronDate);
		}
		log.info("********************InvoiceEngine Reader End*********************");
		return invoiceBatchDTO;
	}

	public InvoiceSetupBatchDTO regenrateInvoiceExceptionReportEngineReader(String invoiceQueueId,
			LocalDate runCronDate, Date invoiceLiveDate) {
		this.runCronDate = runCronDate;
		this.invoiceLiveDate = invoiceLiveDate;

		InvoiceSetupBatchDTO invoiceBatchDTO = new InvoiceSetupBatchDTO();
		invoiceDTOList = new ArrayList<>();
		getInvoiceQueuesById(invoiceQueueId);

		if (CollectionUtils.isNotEmpty(invoiceDTOList)) {
			log.info("Reader invoiceDTOList size ---{}", invoiceDTOList.size());

			invoiceBatchDTO.setInvoiceDTOList(invoiceDTOList);
			invoiceBatchDTO.setRunCronDate(runCronDate);
		}
		log.info("********************InvoiceEngine Reader End*********************");
		return invoiceBatchDTO;
	}

	private void getInvoiceQueuesById(String invoiceNumber) {
		InvoiceQueue invoiceQueue = invoiceQueueRepository.findOne(UUID.fromString(invoiceNumber));
		log.info("invoiceQueue ---{}", invoiceQueue);
		if (null != invoiceQueue) {
			log.info("invoiceSetup startdate : {}", invoiceQueue.getStartDate());

			PoInvoiceSetupDetailsView poInvoiceSetupDetailsView = poInvoiceSetupDetailsViewRepository
					.findOne(invoiceQueue.getPurchaseOrderId());

			InvoiceDTO invoiceDTO;
			try {
				invoiceDTO = prepareInvoiceDTOProcess(poInvoiceSetupDetailsView, invoiceQueue.getStartDate(),
						DateConversionUtil.convertToLocalDate(invoiceQueue.getBillDate()),
						invoiceQueue.getInvoiceNumber());
				if (CollectionUtils.isNotEmpty(invoiceDTO.getTimesheetList())) {
					invoiceDTO.setInvoiceQueueId(invoiceQueue.getId());
					invoiceDTOList.add(invoiceDTO);
				}
			} catch (ParseException e) {
				log.error("********************InvoiceEngine Reader getInvoiceQueuesById*********************{}",e);
 				e.printStackTrace();
			}

		}
	}

	private Page<InvoiceQueue> getAllInvoiceQueues(int pageNumber, int pageSize) {
		Pageable pageRequest = new PageRequest(pageNumber, pageSize, Sort.Direction.ASC, "startDate");
		Page<InvoiceQueue> invoiceQueues = invoiceQueueRepository.getAllInvoiceQueue(pageRequest);
		log.info("invoiceSetups list ---{}", invoiceQueues.getContent().size());
		if (CollectionUtils.isNotEmpty(invoiceQueues.getContent())) {
			invoiceQueues.getContent().forEach(invoiceQueue -> {
				log.info("invoiceSetup startdate  {}", invoiceQueue.getStartDate());
				log.info("invoiceQueue.getPurchaseOrderId()  {}", invoiceQueue.getPurchaseOrderId());
				
				PoInvoiceSetupDetailsView poInvoiceSetupDetailsView = poInvoiceSetupDetailsViewRepository
						.findOne(invoiceQueue.getPurchaseOrderId());

				InvoiceDTO invoiceDTO;
				try {
					invoiceDTO = prepareInvoiceDTOProcess(poInvoiceSetupDetailsView, invoiceQueue.getStartDate(),
							DateConversionUtil.convertToLocalDate(invoiceQueue.getBillDate()), invoiceQueue.getInvoiceNumber());
					if (CollectionUtils.isNotEmpty(invoiceDTO.getTimesheetList())) {
						invoiceDTO.setInvoiceQueueId(invoiceQueue.getId());
						invoiceDTOList.add(invoiceDTO);
					}
				} catch (ParseException e) {
					log.error("********************InvoiceEngine Reader getAllInvoiceQueues*********************{}",e);
					e.printStackTrace();
				}


			
			});
		}
		return invoiceQueues;
	}

	private void inActivePoInvoiceSetup(List<PoInvoiceSetupDetailsView> inActivePoInvoiceSetupDetailList) {
 
		List<UUID> inActiveInvoiceSetupIds = inActivePoInvoiceSetupDetailList.stream()
				.map(PoInvoiceSetupDetailsView::getInvoiceSetupId).collect(Collectors.toList());
		log.info("inactive invoicesetup ---> {}", inActiveInvoiceSetupIds);

		if (CollectionUtils.isNotEmpty(inActiveInvoiceSetupIds)) {
			invoiceSetupRepository.updateByActiveFlag(ActiveFlag.N, inActiveInvoiceSetupIds);
		} 
	}

	private Page<PoInvoiceSetupDetailsView> getAllInvoiceSetupBillCycleDetails(int pageNumber, int pageSize) {
		log.info("reader pageNumber ----> {}", pageNumber);
		log.info("reader pageSize ----> {} ", pageSize);
		Pageable pageRequest = new PageRequest(pageNumber, pageSize, Sort.Direction.ASC, "invoiceStartDate");
		Page<PoInvoiceSetupDetailsView> invoiceSetups = poInvoiceSetupDetailsViewRepository.findAll(pageRequest);
		log.info("invoiceSetups list ---{}", invoiceSetups.getContent().size());
		if (CollectionUtils.isNotEmpty(invoiceSetups.getContent())) {
			invoiceSetups.getContent().forEach(invoiceSetup -> {

			//	if(invoiceSetup.getInvoiceSetupId().toString().equals("286e7960-ca76-478c-83ad-52d8d7361d01")){
				log.info("invoiceSetup startdate  {}", invoiceSetup.getInvoiceStartDate());
				if (Objects.nonNull(invoiceSetup.getInvoiceStartDate())) {
					LocalDate invoiceStartDate = DateConversionUtil
							.convertToLocalDate(invoiceSetup.getInvoiceStartDate());
					LocalDate invoiceEndDate = DateConversionUtil.convertToLocalDate(invoiceSetup.getInvoiceEndDate());
					validateInvoiceSetup(invoiceSetup, invoiceStartDate, invoiceEndDate);
				}
				// }
			});
		}
		return invoiceSetups;
	}

	private void validateInvoiceSetup(PoInvoiceSetupDetailsView invoiceSetup, LocalDate invoiceStartDate,
			LocalDate invoiceEndDate) {
		if (invoiceEndDate.isBefore(runCronDate)) {
			inActivePoInvoiceSetupDetailList.add(invoiceSetup);
		} else {
			if (invoiceStartDate.isBefore(runCronDate)) {
				InvoiceQueue latestInvoiceQueue = invoiceQueueRepository
						.getOneInvoiceQueueOrderByPurchaseOrderId(invoiceSetup.getPurchaseOrderId());
				if (Objects.nonNull(latestInvoiceQueue) && Objects.nonNull(latestInvoiceQueue.getBillDate())) {
					lastBillDate = DateUtil.addDaysToUtilDate(latestInvoiceQueue.getBillDate(), 1);
					prepareInvoiceSetupBillCycle(invoiceSetup);
				} else {
					validateBillDate(invoiceSetup);
				}
			}
		}
	}

	private Integer prepareLatestInvoiceNumber(PoInvoiceSetupDetailsView invoiceSetup) {
		Integer latestInvoiceNumber = 0;
		InvoiceQueue latestInvoiceQueue = invoiceQueueRepository
				.getOneInvoiceQueueOrderByInvoiceSetupId(invoiceSetup.getInvoiceSetupId());
		if (Objects.nonNull(latestInvoiceQueue) && Objects.nonNull(latestInvoiceQueue.getInvoiceSetupNumber())) {
			latestInvoiceNumber = latestInvoiceQueue.getInvoiceSetupNumber();
			log.info("latestInvoiceNumber ---- {} " , latestInvoiceNumber);
		}
		return latestInvoiceNumber;
	}

	private void validateBillDate(PoInvoiceSetupDetailsView invoiceSetup) {
		log.info("invoiceLiveDate ---> {}", invoiceLiveDate);
		log.info("InvoiceStartDate --->{}", invoiceSetup.getInvoiceStartDate());
		log.info("InvoiceStartDate --->{}", invoiceSetup.getInvoiceEndDate());
		log.info("EngagementStartDate --->{}", invoiceSetup.getEngagementStartDate());
		log.info("EngagementEndDate --->{}", invoiceSetup.getEngagementEndDate());
		LocalDate invoiceLiveUtilDate = DateConversionUtil.convertToLocalDate(invoiceLiveDate);
		if (invoiceLiveUtilDate.isBefore(runCronDate) || invoiceLiveUtilDate.isEqual(runCronDate)) {
			lastBillDate = invoiceLiveDate;
		} else {
			lastBillDate = DateConversionUtil.convertToDate(runCronDate);
		}
		log.info("lastBillDate ---> {}", lastBillDate);
		if (lastBillDate.before(invoiceSetup.getEngagementEndDate())) {
			if (!DateUtil.isWithinRange(lastBillDate, invoiceSetup.getEngagementStartDate(),
					invoiceSetup.getEngagementEndDate())) {
				lastBillDate = invoiceSetup.getEngagementStartDate();
			}
			prepareInvoiceSetupBillCycle(invoiceSetup);
		}
	}

	private void prepareInvoiceSetupBillCycle(PoInvoiceSetupDetailsView invoiceSetup) {

		if (invoiceSetup.getInvoiceTypeName().equalsIgnoreCase(PREBILL)
				|| invoiceSetup.getInvoiceTypeName().equalsIgnoreCase(REGULAR)) {
			prepareRegularPrepillBillDate(invoiceSetup);
		}
	}

	public void prepareInvoiceDTO(PoInvoiceSetupDetailsView invoiceSetup, List<Long> validCycleIds, Date startDate,
			BigDecimal billingAmount, List<InvoiceDetail> invoiceDetailList,
			List<InvoiceExceptionDetailsDTO> invoiceExceptionDetailsDTOs, List<BillingProfileDTO> billingProfileList) throws ParseException {

		updateTheAccruingFlagOfValidBillCycles(validCycleIds);

		InvoiceDTO invoiceDTO = populateInvoiceDTO(invoiceSetup, startDate, runCronDate);

		invoiceDTO.setBillingProfiles(billingProfileList);
		invoiceDTO.setInvoiceDetails(invoiceDetailList);
		invoiceDTO.setAmount(billingAmount);
		invoiceDTO.setBillingAmount(billingAmount);
		invoiceDTO.setPoNumber(invoiceSetup.getPoNumber());
		invoiceDTO.setUserPreference(getAllInvoiceSetupOptions(invoiceSetup));
		if (CollectionUtils.isNotEmpty(invoiceExceptionDetailsDTOs)) {
			invoiceDTO.setInvoiceExceptionDetail(invoiceExceptionDetailsDTOs);
			invoiceDTO.setExceptionReason(InvoiceConstants.STATUS_TIMESHEET_NOT_APPROVED);
			invoiceDTO.setExceptionSource(InvoiceConstants.TIMESHEET_NOT_APPROVAL);
		}
		invoiceDTOList.add(invoiceDTO);
	}

	private void generateInvoiceNumber(PoInvoiceSetupDetailsView invoiceSetup, InvoiceDTO invoiceDTO) {
		StringBuilder invoiceNumber = new StringBuilder();
		if (StringUtils.isNotEmpty(invoiceSetup.getPrefix())) {
			prefixValue = invoiceSetup.getPrefix();
		}
		if (StringUtils.isNotBlank(invoiceSetup.getSeparatorValue())) {
			separatorValue = invoiceSetup.getSeparatorValue();
		}
		invoiceNumber.append(prefixValue);
		invoiceNumber.append(separatorValue);
		Integer invoiceSetupNumber = appendInvoiceNumber(invoiceSetup);
		invoiceNumber.append(invoiceSetupNumber);
		invoiceNumber.append(separatorValue);

		if (StringUtils.isNotEmpty(invoiceSetup.getSuffixType())) {
			if (invoiceSetup.getSuffixType().equalsIgnoreCase(YEAR)) {
				invoiceNumber.append(year);
			} else if (invoiceSetup.getSuffixType().equalsIgnoreCase(MONTH)) {
				invoiceNumber.append(month);
			} else if (invoiceSetup.getSuffixType().equalsIgnoreCase(YEAR_MONTH)) {
				invoiceNumber.append(month);
				invoiceNumber.append("-");
				invoiceNumber.append(year);
			} else {
				invoiceNumber.append(invoiceSetup.getSuffixValue());
			}
		}
		log.info("invoiceNumber ----> {}", invoiceNumber);
		invoiceDTO.setInvoiceNumber(invoiceNumber.toString());
		invoiceDTO.setInvoiceSetupNumber(invoiceSetupNumber);
	}

	private Integer appendInvoiceNumber(PoInvoiceSetupDetailsView invoiceSetup) {
		if (MapUtils.isEmpty(startingNumberHashMap) || (MapUtils.isNotEmpty(startingNumberHashMap)
				&& !startingNumberHashMap.containsKey(invoiceSetup.getInvoiceSetupId()))) {
			Integer latestInvoiceNumber = prepareLatestInvoiceNumber(invoiceSetup);
			log.info("latestInvoiceNumber ---> {} ", latestInvoiceNumber);
			if (latestInvoiceNumber > 0) {
				startingNumber = latestInvoiceNumber + 1;
			} else {
				startingNumber = invoiceSetup.getStartingNumber() > 0 ? invoiceSetup.getStartingNumber()
						: startingNumber;
			}
			startingNumberHashMap.put(invoiceSetup.getInvoiceSetupId(), startingNumber);
		} else {
			startingNumber = startingNumberHashMap.get(invoiceSetup.getInvoiceSetupId()) + 1;
			startingNumberHashMap.put(invoiceSetup.getInvoiceSetupId(), startingNumber);

		}
		return startingNumber;
	}

	private void updateTheAccruingFlagOfValidBillCycles(List<Long> validCycleIds) {
		validCycleIds.forEach(validCycleId -> log.info("Accrued BillCycle Id\n" + validCycleId));
		billCycleRepository.updateAccruingFlagValue(validCycleIds, AccuringFlag.N);
	}

 

	private void prepareRegularPrepillBillDate(PoInvoiceSetupDetailsView invoiceSetup) {
		List<LocalDate> billDateList = new ArrayList<>();

		if (invoiceSetup.getBillCycleName() != null && invoiceSetup.getInvoiceTypeName().equalsIgnoreCase(REGULAR)) {
			billDateList = findAllBillDateByBillCycle(invoiceSetup.getBillCycleDay(), invoiceSetup.getBillCycleName(),
					invoiceSetup.getBillCycleStartEndDetail());
			log.info("REGULAR invoice bill date ---> {} ", billDateList);
		}

		if (CollectionUtils.isNotEmpty(billDateList) && Objects.nonNull(lastBillDate)) {
			log.info("validate before lastBillDate ---> {}", lastBillDate);
			billDateList.stream().forEach(invoicebilldate -> {
				log.info("billDate ----> {} ", invoicebilldate);
				log.info("runCronDate ----> {}", runCronDate);
				LocalDate lastBillDateConversion = DateConversionUtil.convertToLocalDate(lastBillDate);
				LocalDate reduceInvoiceBillDate = invoicebilldate;
				if (lastBillDateConversion.isBefore(reduceInvoiceBillDate)
						|| lastBillDateConversion.isEqual(reduceInvoiceBillDate)) {
					validateInvoiceBillDate(invoiceSetup, lastBillDate, reduceInvoiceBillDate.minusDays(1));
				}
				lastBillDate = DateConversionUtil.convertToDate(invoicebilldate);
			});
		}
	}
	
	private void validateInvoiceBillDate(PoInvoiceSetupDetailsView invoiceSetup, Date startDate, LocalDate billDate) {
		LocalDate startDateConversion = DateConversionUtil.convertToLocalDate(startDate);
		if (Objects.nonNull(startDateConversion)
				&& (startDateConversion.isBefore(runCronDate) || startDateConversion.isEqual(runCronDate))) {
			List<InvoiceQueue> invoiceQueueList = null;
			try {

				log.info("validateInvoiceBillDate billDate ---> {}", startDateConversion);
				Date billDateDefaultTime = DateUtil.parseLocalDateFormatWithDefaultTime(startDateConversion);
				log.info("validateInvoiceBillDate billDateDefaultTime ---> {} ", billDateDefaultTime);
				invoiceQueueList = invoiceQueueRepository.getInvoiceQueueByBillingDateAndCronDate(billDateDefaultTime,
						invoiceSetup.getPurchaseOrderId());

			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			log.info("invoiceQueueList ---> {}", invoiceQueueList);
			// here we form invoice data
			if (CollectionUtils.isEmpty(invoiceQueueList)) {
				InvoiceDTO invoiceDTO;
				try {
					invoiceDTO = prepareInvoiceDTOProcess(invoiceSetup, startDate, billDate ,null);
					if (CollectionUtils.isNotEmpty(invoiceDTO.getTimesheetList())) {
						invoiceDTOList.add(invoiceDTO);
						log.info("After invoiceDTO.getAmount() --->{}", invoiceDTO.getAmount());
						log.info("After invoiceDTO.getBillingAmount() --->{}", invoiceDTO.getBillingAmount());
					}
				} catch (ParseException e) {
					// TODO  Need to handle this Exception
					e.printStackTrace();
				}
				
			} else {
				 

				UUID poId = invoiceSetup.getPurchaseOrderId();
				List<InvoiceQueue> invoiceQueues = null;
				if(Objects.nonNull(invoiceQueueList) && CollectionUtils.isNotEmpty(invoiceQueueList)){
					invoiceQueues = invoiceQueueList.stream()
						.filter(invoiceque -> invoiceque.getPurchaseOrderId().equals(poId)
								&& invoiceque.getBillDate().equals(startDate))
						.collect(Collectors.toList());
				}

				if (CollectionUtils.isEmpty(invoiceQueues)) {
					InvoiceDTO invoiceDTO;
					try {
						invoiceDTO = prepareInvoiceDTOProcess(invoiceSetup, startDate, billDate ,null);
						if (CollectionUtils.isNotEmpty(invoiceDTO.getTimesheetList())) {
							log.info("After invoiceDTO.getAmount() --->{}", invoiceDTO.getAmount());
							log.info("After invoiceDTO.getBillingAmount() --->{}", invoiceDTO.getBillingAmount());
							invoiceDTOList.add(invoiceDTO);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				
				}
			}
		}
	}

	private InvoiceDTO prepareInvoiceDTOProcess(PoInvoiceSetupDetailsView invoiceSetup, Date startDate,
			LocalDate billDate,String existingInvoiceNumber) throws ParseException {
		List<BillingProfileDTO> billingProfileList = new ArrayList<>();
		List<InvoiceDetail> invoiceDetailList = new ArrayList<>();
		List<InvoiceExceptionDetailsDTO> invoiceExceptionDetailsDTOs = new ArrayList<>();
		BigDecimal totBillingAmount = new BigDecimal(0);
		InvoiceDTO invoiceDTO = populateInvoiceDTO(invoiceSetup, startDate, billDate);
		populateBillingProfileDetails(invoiceSetup, billDate, billingProfileList, invoiceExceptionDetailsDTOs,
				invoiceDetailList, totBillingAmount, invoiceDTO, startDate);
		invoiceDTO.setBillingProfiles(billingProfileList);
		invoiceDTO.setInvoiceDetails(invoiceDetailList);
		invoiceDTO.setPoNumber(invoiceSetup.getPoNumber());
		invoiceDTO.setBillToManagerName(invoiceSetup.getBillToMgrName());
		invoiceDTO.setBillToManagerEmailId(invoiceSetup.getBillToManagerEmailId());
		if(StringUtils.isBlank(existingInvoiceNumber)){
			generateInvoiceNumber(invoiceSetup, invoiceDTO);
		}else{
			invoiceDTO.setInvoiceNumber(existingInvoiceNumber);
		}
		invoiceDTO.setUserPreference(getAllInvoiceSetupOptions(invoiceSetup));
		return invoiceDTO;
	}
 
	private InvoiceDTO populateInvoiceDTO(PoInvoiceSetupDetailsView invoiceSetup, Date startDate, LocalDate billDate) throws ParseException {
		InvoiceDTO invoiceDTO = new InvoiceDTO();
		DeliveryDTO deliveryDTO = new DeliveryDTO();
		deliveryDTO.setDeliveryType(invoiceSetup.getInvoiceDeliveryMode());
		Long invoiceTemplateId = invoiceSetup.getInvoiceTemplateId();
		invoiceDTO.setBillToClientId(invoiceSetup.getCustomerId());
		invoiceDTO.setBillToClientName(invoiceSetup.getCustomerName());
		invoiceDTO.setFinanceRepresentId(invoiceSetup.getFinanceRepresentId());
		invoiceDTO.setFinanceRepresentName(invoiceSetup.getFinanceRepresentName());
		invoiceDTO.setInvoiceSetupNotes(invoiceSetup.getInvoiceSetupNotes());
		invoiceDTO.setCurrencyType(invoiceSetup.getCurrencyName());
		invoiceDTO.setDelivery(invoiceSetup.getInvoiceDeliveryMode());
		invoiceDTO.setDeliveryMethod(deliveryDTO);
		invoiceDTO.setRunCronDate(runCronDate);
	 
			log.info("populateInvoiceDTO billDate -> {} ", billDate);
			Date billDateConversion = DateConversionUtil.convertToDate(billDate);
			log.info("populateInvoiceDTO billDateConversion ->  {} ", billDateConversion);
			Date runCronDateConversion = DateConversionUtil.convertToDate(runCronDate);
			log.info("populateInvoiceDTO runCronDateConversion -> {} ", runCronDateConversion);
			Date billDateDefaultTime = DateUtil.parseUtilDateFormatWithDefaultTime(billDateConversion);
			log.info("populateInvoiceDTO billDateDefaultTime -> {} ", billDateDefaultTime);
			log.info("populateInvoiceDTO startDate -> {} ", startDate);
			Date startDateDefaultTime = DateUtil.parseUtilDateFormatWithDefaultTime(startDate);
			log.info("populateInvoiceDTO startDateDefaultTime -> {}", startDateDefaultTime);
			Date runCronDateDefaultTime = DateUtil.parseUtilDateFormatWithDefaultTime(runCronDateConversion);
			invoiceDTO.setBillDate(billDateDefaultTime);
			invoiceDTO.setStartDate(startDateDefaultTime);
			invoiceDTO.setEndDate(billDateDefaultTime);
			invoiceDTO.setInvoiceDate(runCronDateDefaultTime);
		 
		invoiceDTO.setLocation(invoiceSetup.getCityName());
		invoiceDTO.setCountry(invoiceSetup.getCountryName());
		invoiceDTO.setProjectId(invoiceSetup.getProjectId());
		invoiceDTO.setProjectName(invoiceSetup.getProjectName());
		InvoiceSetupDTO invoiceSetupData = new InvoiceSetupDTO();
		invoiceSetupData.setInvoiceSetupId(invoiceSetup.getInvoiceSetupId());
		invoiceSetupData.setInvoiceSetupName(invoiceSetup.getInvoiceSetupName());
		invoiceSetupData.setInvoiceType(invoiceSetup.getInvoiceTypeName());
//		invoiceSetupData.setPaymentTerms(invoiceSetup.getPaymentTerms());

		if (CollectionUtils.isNotEmpty(invoiceSetup.getBillCycles())) {
			List<BillCycleDTO> billCycleDTOList = InvoiceSetupBillCycleMapper.INSTANCE
					.billCyclesListToBillCycleDTOList(invoiceSetup.getBillCycles());
			invoiceSetupData.setBillCycleDTOList(billCycleDTOList);
		}

		if (Objects.nonNull(invoiceSetup.getInvoiceSetupBillCycle())) {
			InvoiceSetupBillCycleDTO invoiceSetupBillCycleDTO = InvoiceSetupBillCycleMapper.INSTANCE
					.invoiceSetupBillCycleToInvoiceSetupBillCycleDTO(invoiceSetup.getInvoiceSetupBillCycle());
			invoiceSetupData.setInvoiceSetupBillCycleDTO(invoiceSetupBillCycleDTO);
		}

		InvoiceTemplate invoiceTemplate = invoiceTemplateRepository.findByInvoiceTemplateId(invoiceTemplateId);
		InvoiceTemplateDTO invoiceTemplateDTO = new InvoiceTemplateDTO();
		invoiceTemplateDTO.setTemplate(invoiceTemplate.getTemplate());
		invoiceTemplateDTO.setTemplateflag(invoiceTemplate.getTemplateflag());
		invoiceTemplateDTO.setLogofileName(invoiceTemplate.getLogofileName());
		invoiceTemplateDTO.setInvoiceTemplateId(invoiceTemplate.getInvoiceTemplateId());
		invoiceTemplateDTO.setInvoiceTemplateName(invoiceTemplate.getInvoiceTemplateName());
		invoiceSetupData.setInvoiceTemplate(invoiceTemplateDTO);

		PurchaseOrderDTO purchaseOrder = new PurchaseOrderDTO();
		purchaseOrder.setPoStartDate(invoiceSetup.getPoStartDate());
		purchaseOrder.setPoEndDate(invoiceSetup.getPoEndDate());
		purchaseOrder.setPoNumber(invoiceSetup.getPoNumber());
		purchaseOrder.setPurchaseOrderId(invoiceSetup.getPurchaseOrderId());
		purchaseOrder.setInvoiceSetup(invoiceSetupData);
 		invoiceDTO.setPurchaseOrder(purchaseOrder);

		invoiceDTO.setInvoiceSetup(invoiceSetupData);

		BillToManagerDTO billToManager = new BillToManagerDTO();
		billToManager.setBillToManagerEmailId(invoiceSetup.getBillToManagerEmailId());
		billToManager.setBillToMgrName(invoiceSetup.getBillToMgrName());
		billToManager.setCityName(invoiceSetup.getCityName());
		billToManager.setCountryName(invoiceSetup.getCountryName());
		billToManager.setBillAddress(invoiceSetup.getBillAddress());
		billToManager.setPostalCode(invoiceSetup.getPostalCode());
		billToManager.setStateName(invoiceSetup.getStateName());
		invoiceDTO.setBillToManager(billToManager);

		invoiceDTO.setClientInfo(getClientInfoDTO());
		return invoiceDTO;
	}

	private void populateBillingProfileDetails(PoInvoiceSetupDetailsView invoiceSetup, LocalDate billDate,
			List<BillingProfileDTO> billingProfileList, List<InvoiceExceptionDetailsDTO> invoiceExceptionDetailsDTOs,
			List<InvoiceDetail> invoiceDetailList, BigDecimal totBillingAmount, InvoiceDTO invoiceDTO, Date startDate) throws ParseException {
		List<BillingQueue> billingQueueList = getBillingProfile(invoiceSetup.getPurchaseOrderId());

		List<ExpenseView> expensesList = null;
		List<ExpenseView> expensesReceiptList = new ArrayList<>();

		List<Timesheet> allTimesheetList = new ArrayList<>();
		BigDecimal expenseAmount = new BigDecimal(0);
		int serialNumber = 1;
		log.info("billingQueueList ---{}****************** {}", invoiceSetup.getPoNumber(), billingQueueList);
		if (CollectionUtils.isNotEmpty(billingQueueList)) {
			for (BillingQueue billingQueue : billingQueueList) {
				BillingProfileDTO billingProfileDTO = new BillingProfileDTO();
				InvoiceDetail invoiceDetail = new InvoiceDetail();
				BigDecimal billingAmount = new BigDecimal(0);
				Date maxTimesheetEndDate = findMaxTimesheetEndDateByEmployeeAndEngagementId(
						billingQueue.getEmployeeId(), billingQueue.getEngagementId(), startDate);
				log.info("maxTimesheetEndDate ---->{}", maxTimesheetEndDate);
				if (maxTimesheetEndDate == null) {
					maxTimesheetEndDate = invoiceLiveDate;
				}
				List<Timesheet> timesheetList = getAllTimesheetsFromMaxTSEndDateToBillDateByEmpIdEngagId(
						billingQueue.getEmployeeId(), billingQueue.getEngagementId(), maxTimesheetEndDate, billDate);
				log.info("timesheetList --->{}", timesheetList);
				if (CollectionUtils.isNotEmpty(timesheetList)) {
					billingAmount = cumulativeBillAmount(timesheetList, billingQueue, billingProfileDTO, invoiceDetail);
					log.info("billingAmount --->{}", billingAmount);
					totBillingAmount = totBillingAmount.add(billingAmount);
					invoiceDetail.setSerialNumber(serialNumber++);

					allTimesheetList.addAll(timesheetList);
					if (Objects.nonNull(invoiceDTO)) {

						invoiceDetail.setInvoiceSetupId(invoiceSetup.getInvoiceSetupId());
						invoiceDetail.setPoId(invoiceSetup.getPurchaseOrderId());
						invoiceDetailList.add(invoiceDetail);

						List<InvoiceExceptionDetailsDTO> exceptionDetailsDTOs = setInvoiceExceptionDetailsDTO(
								timesheetList, billingQueue, invoiceSetup.getCurrencyName());
						invoiceExceptionDetailsDTOs.addAll(exceptionDetailsDTOs);
						billingProfileList.add(billingProfileDTO);
					}
				}
			}
			if (Objects.nonNull(invoiceDTO)) {

				expensesList = expenseViewRepository.getApprovalLevelForExpenses(invoiceSetup.getProjectId(), startDate,
						DateConversionUtil.convertToDate(billDate));
				try {
					expenseAmount = setExpenseList(expensesList, expensesReceiptList);
				} catch (IOException e) {
				}

				BigDecimal totAmount = totBillingAmount.add(expenseAmount);
				invoiceDTO.setExpensesList(expensesList);
				invoiceDTO.setExpensesReceiptList(expensesReceiptList);
				invoiceDTO.setBillingProfiles(billingProfileList);
				invoiceDTO.setInvoiceDetails(invoiceDetailList);
				invoiceDTO.setAmount(totAmount);
				invoiceDTO.setBillingAmount(totBillingAmount);
				invoiceDTO.setExpenseAmount(expenseAmount);
				invoiceDTO.setPoNumber(invoiceSetup.getPoNumber());
				invoiceDTO.setUserPreference(getAllInvoiceSetupOptions(invoiceSetup));
				invoiceDTO.setTimesheetList(allTimesheetList);
				if (CollectionUtils.isNotEmpty(invoiceExceptionDetailsDTOs)) {
					invoiceDTO.setInvoiceExceptionDetail(invoiceExceptionDetailsDTOs);
					invoiceDTO.setExceptionReason(InvoiceConstants.STATUS_TIMESHEET_NOT_APPROVED);
					invoiceDTO.setExceptionSource(InvoiceConstants.TIMESHEET_NOT_APPROVAL);
				}

			}
		} else {
			if (Objects.nonNull(invoiceDTO)) {
				log.info("PO Number {} has no Billing profiles", invoiceDTO.getPoNumber());
			}
		}
	}

	private List<LocalDate> findAllBillDateByBillCycle(String billCycleDay, String billCycleName,
			String billCycleStartEndDetail) {
		List<LocalDate> allBillDate = new ArrayList<>();
		Set<LocalDate> billDate = new LinkedHashSet<>();
		LocalDate lastBillLocalDate = DateConversionUtil.convertToLocalDate(lastBillDate);
		if (Objects.nonNull(lastBillLocalDate) && Objects.nonNull(runCronDate)) {
			if (StringUtils.isNotBlank(billCycleName) && StringUtils.equalsIgnoreCase(billCycleName, MONTHLY)) {
				billDate = InvoiceBillDateUtil.getListRegularOnMonthly(billCycleStartEndDetail, billCycleDay,
						lastBillLocalDate, runCronDate);
			} else if (StringUtils.isNotBlank(billCycleName)
					&& StringUtils.equalsIgnoreCase(billCycleName, SEMIMONTHLY)) {
				billDate = InvoiceBillDateUtil.getListRegularOnSemiMonthly(billCycleDay, lastBillLocalDate,
						runCronDate);
			} else {
				billDate = InvoiceBillDateUtil.getListRegularOnWeekly(billCycleDay, lastBillLocalDate, runCronDate);
			}
		}
		allBillDate.addAll(billDate);
		return allBillDate;
	}

	public ClientInfoDTO getClientInfoDTO() {
		ClientInfoDTO clientInfoDTO = null;
		CompanyProfile companyProfile = companyProfileRepository.getProfileDetails();
		if (companyProfile == null) {
		} else {
			try {
				EmployeeAttachmentsDTO employeeAttachmentsDTO = getEmployeeFile(
						companyProfile.getCompanyProfileImageId());
				clientInfoDTO = setCommonInfo(companyProfile, employeeAttachmentsDTO);
			} catch (Exception e) {
			}
		}
		return clientInfoDTO;
	}

	private EmployeeAttachmentsDTO getEmployeeFile(String employeeAttachmentId) throws IOException {
		BasicDBObject query = new BasicDBObject();
		EmployeeAttachmentsDTO employeeAttachmentsDTO = new EmployeeAttachmentsDTO();
		query.put("employeeAttachmentId", employeeAttachmentId);
		GridFS gridFS = new GridFS(mongoTemplate.getDb(), "timesheet");
		GridFSDBFile gridFSDBfile = gridFS.findOne(query);
		if (null != gridFSDBfile) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			gridFSDBfile.writeTo(baos);
			employeeAttachmentsDTO.setContent(baos.toByteArray());
			employeeAttachmentsDTO.setContentType(gridFSDBfile.getContentType());
			employeeAttachmentsDTO.setFileName(gridFSDBfile.getFilename());
			employeeAttachmentsDTO.setEmployeeAttachmentId(gridFSDBfile.get("employeeAttachmentId").toString());
			employeeAttachmentsDTO.setSourceReferenceId(gridFSDBfile.get("sourceReferenceId").toString());
		}
		return employeeAttachmentsDTO;
	}

	private ClientInfoDTO setCommonInfo(CompanyProfile companyProfile, EmployeeAttachmentsDTO employeeAttachmentsDTO) {
		ClientInfoDTO clientInfoDTO = new ClientInfoDTO();
		clientInfoDTO.setCompanyProfile(companyProfile);
		clientInfoDTO.setEmployeeAttachmentsDTO(employeeAttachmentsDTO);
		return clientInfoDTO;
	}

	private UserPreferenceDTO getAllInvoiceSetupOptions(PoInvoiceSetupDetailsView poInvoiceSetupDetailsView) {
		if (!ObjectUtils.isEmpty(poInvoiceSetupDetailsView)) {

			InvoiceSetup invoiceSetup = invoiceSetupRepository
					.findByInvoiceSetupId(poInvoiceSetupDetailsView.getInvoiceSetupId());
			if (!ObjectUtils.isEmpty(invoiceSetup)) {
				return populateInvoiceSetupOptions(invoiceSetup, poInvoiceSetupDetailsView);
			}
		}
		return new UserPreferenceDTO();
	}

	private UserPreferenceDTO populateInvoiceSetupOptions(InvoiceSetup invoiceSetup,
			PoInvoiceSetupDetailsView poInvoiceSetupDetailsView) {
		UserPreferenceDTO userPreferenceDTO = new UserPreferenceDTO();
		List<InvoiceSetupOption> options = invoiceSetup.getInvoiceSetupOptions();
		if (CollectionUtils.isNotEmpty(options)) {
			List<UUID> optionIds = new ArrayList<>();
			options.forEach(option -> optionIds.add(option.getId()));
			List<LookupView> lookUpViews = lookUpViewRepository.findAttributeValueByEntityAttributeMapIds(optionIds);
			if (CollectionUtils.isNotEmpty(lookUpViews)) {
				lookUpViews.forEach(
						lookup -> populateUserPreferenceValueForPrivateSetup(lookup, options, userPreferenceDTO));
			}
			populateDeliveryModeDetails(poInvoiceSetupDetailsView, userPreferenceDTO);
		}
		return userPreferenceDTO;
	}
 
	private void populateDeliveryModeDetails(PoInvoiceSetupDetailsView poInvoiceSetupDetailsView,
			UserPreferenceDTO userPreferenceDTO) {
		if (poInvoiceSetupDetailsView.getInvoiceDeliveryMode().equals(InvoiceEngineConstants.AUTO_DELIVERY)) {
			userPreferenceDTO.setAutoDelivery(true);
		} else if (poInvoiceSetupDetailsView.getInvoiceDeliveryMode().equals(InvoiceEngineConstants.EMAIL_DELIVERY)) {
			userPreferenceDTO.setEmailDelivery(true);
		} else if (poInvoiceSetupDetailsView.getInvoiceDeliveryMode().equals(InvoiceEngineConstants.POSTAL_DELIVERY)) {
			userPreferenceDTO.setPostalDelivery(true);
		}
	}

	private void populateUserPreferenceValueForPrivateSetup(LookupView lookup, List<InvoiceSetupOption> options,
			UserPreferenceDTO userPreferenceDTO) {
		options.forEach(option -> {
			if (option.getId().equals(lookup.getEntityAttributeMapId())) {
				switch (lookup.getEntityAttributeMapValue()) {
				case InvoiceEngineConstants.IS_TS_INCLUDE:
					if (option.getValue().equals(InvoiceEngineConstants.PRIVATE_Y)) {
						userPreferenceDTO.setTimesheetInclude(true);
					}
					break;
				case InvoiceEngineConstants.IS_EXP_INCLUDE:
					if (option.getValue().equals(InvoiceEngineConstants.PRIVATE_Y)) {
						userPreferenceDTO.setExpenseInclude(true);
					}
					break;
				case InvoiceEngineConstants.IS_EXP_DOC_INCLUDE:
					if (option.getValue().equals(InvoiceEngineConstants.PRIVATE_Y)) {
						userPreferenceDTO.setExpenseDocumentationInclude(true);
					}
					break;
				case InvoiceEngineConstants.IS_HIRING_MANAGER_SHOW:
					if (option.getValue().equals(InvoiceEngineConstants.PRIVATE_Y)) {
						userPreferenceDTO.setHiringManagerShow(true);
					}
					break;
				case InvoiceEngineConstants.IS_SINGLE_LINE_ITEM_SHOW:
					if (option.getValue().equals(InvoiceEngineConstants.PRIVATE_Y)) {
						userPreferenceDTO.setSingleLineItemShow(true);
					}
					break;
				case InvoiceEngineConstants.IS_DIFF_LINE_ITEM_SHOW:
					if (option.getValue().equals(InvoiceEngineConstants.PRIVATE_Y)) {
						userPreferenceDTO.setDifferentLineItemShow(true);
					}
					break;
				case InvoiceEngineConstants.IS_SEP_OT_INV:
					if (option.getValue().equals(InvoiceEngineConstants.PRIVATE_Y)) {
						userPreferenceDTO.setSeparateOverTimeInvoiceGenerate(true);
					}
					break;
				case InvoiceEngineConstants.IS_CONTRACTOR_EXCLUDE:
					if (option.getValue().equals(InvoiceEngineConstants.PRIVATE_Y)) {
						userPreferenceDTO.setContractorNameExclude(true);
					}
					break;
				}
			}
		});
	}
	/*-------------------------------------------------Calculating Billing Amount -----Starts--------------------*/

	private List<BillingQueue> getBillingProfile(UUID poUUID) {
		log.info("PoId---- {}", poUUID);
		return billingQueueRepository.getBillingQueueByPoId(poUUID);
	}

	private Date findMaxTimesheetEndDateByEmployeeAndEngagementId(Long employeeId, String engagementId,
			Date lastInvoiceGeneratedDate) throws ParseException {
		log.info("findMaxTimesheetEndDateByEmployeeAndEngagementId lastInvoiceGeneratedDate--- {}",
				lastInvoiceGeneratedDate);

		Timesheet timesheet = null;
			timesheet = timesheetRepository.getAllTimesheetsByEmpIdAndEngagIdAndLastBillDate(employeeId, engagementId,
					DateUtil.parseUtilDateFormatWithDefaultTime(lastInvoiceGeneratedDate));
		 
		Date timesheetMaxEndDate = null;
		if (timesheet != null && timesheet.getEndDate() != null) {
			timesheetMaxEndDate = timesheet.getEndDate();
		}
		return timesheetMaxEndDate;
	}

	private List<Timesheet> getAllTimesheetsFromMaxTSEndDateToBillDateByEmpIdEngagId(Long employeeId,
			String engagementId, Date maxTimesheetEndDate, LocalDate runDate) throws ParseException {

		List<Timesheet> timesheetList = null;
		if (null != maxTimesheetEndDate) {
			Date plusTimesheetDate = DateUtil.addDaysToUtilDate(maxTimesheetEndDate, 1);
			Date billDate = DateConversionUtil.convertToDate(runDate);
			log.info("Employee Id :{} ,{} ,{} ,{}", employeeId, engagementId, maxTimesheetEndDate, runDate);

				timesheetList = timesheetRepository.getAllTimesheetsFromMaxTSEndDateToBillDateByEmpIdEngagId(employeeId,
						engagementId, DateUtil.parseUtilDateFormatWithDefaultTime(plusTimesheetDate),
						DateUtil.parseUtilDateFormatWithDefaultTime(billDate));
		 
		}
		return timesheetList;
	}

	private BigDecimal cumulativeBillAmount(List<Timesheet> timesheetList, BillingQueue billingQueue,
			BillingProfileDTO billingProfileDTO, InvoiceDetail invoiceDetail) {
		BigDecimal totalTimesheetCalAmount = BigDecimal.ZERO;

		Integer totalUnits = 0;
		Double totStHours = 0d;
		Double totOtHours = 0d;
		Double totDthours = 0d;
		Double totalWorkhours = 0d;
		Double ptoHours = 0d;
		Double leaveHours = 0d;
		String contractorName = null;
		int i = 1;
		List<UUID> unitTimesheetId = new ArrayList<>();
		for (Timesheet timesheet : timesheetList) {
			if (i == 1) {
				contractorName = timesheet.getEmployee().getName();
				invoiceDetail.setStartDate(timesheet.getStartDate());
			}
			if (timesheet.getLookupType().getValue().equals(InvoiceEngineConstants.UNITS)) {
				unitTimesheetId.add(timesheet.getId());
			} else {
				totStHours += timesheet.getStHours();
				totOtHours += timesheet.getOtHours();
				totDthours += timesheet.getDtHours();
			}
			if (i == timesheetList.size()) {
				invoiceDetail.setEndDate(timesheet.getEndDate());
			}

			if (null != timesheet.getWorkHours()) {
				totalWorkhours += timesheet.getWorkHours();
			}
			if (null != timesheet.getPtoHours()) {
				ptoHours += timesheet.getPtoHours();
			}

			if (null != timesheet.getLeaveHours()) {
				leaveHours += timesheet.getLeaveHours();
			}
			i++;
		}

		if (CollectionUtils.isNotEmpty(unitTimesheetId)) {
			totalUnits = getTotalUnits(unitTimesheetId);
		}
		totalTimesheetCalAmount = calculateHoursbasedAmount(billingQueue, invoiceDetail, totStHours, totOtHours,
				totDthours, totalWorkhours, totalUnits);

		billingProfileDTO.setAmount(totalTimesheetCalAmount);
		if (null != billingQueue.getBillToClientST()) {
			billingProfileDTO.setRate(billingQueue.getBillToClientST().doubleValue());
		}

		billingProfileDTO.setWorkHours(totalWorkhours);
		billingProfileDTO.setContractorName(contractorName);
		invoiceDetail.setContractorName(contractorName);
		invoiceDetail.setWorkHours(totalWorkhours);

		invoiceDetail.setLeaveHours(leaveHours);
		invoiceDetail.setPtoHours(ptoHours);
		return totalTimesheetCalAmount;
	}

	private Integer getTotalUnits(List<UUID> unitTimesheetId) {
		Integer totalUntis = 0;

		List<TimesheetDetails> timesheetsList = timesheetRepository.getAllTimesheets(unitTimesheetId);
		if (CollectionUtils.isNotEmpty(timesheetsList)) {
			for (TimesheetDetails timesheet : timesheetsList) {
				if (null != timesheet.getUnits())
					totalUntis += timesheet.getUnits().intValue();
			}
		}
		return totalUntis;
	}

	private BigDecimal calculateHoursbasedAmount(BillingQueue billingQueue, InvoiceDetail invoiceDetail,
			Double totStHours, Double totOtHours, Double totDthours, Double workhours, Integer totalUnits) {
		StringBuffer groupBySplitter = new StringBuffer();

		BigDecimal totalAmount = BigDecimal.ZERO;
		// Double totalWorkhours = 0d;
		BigDecimal totalSTAmount = BigDecimal.ZERO;
		BigDecimal totalOTAmount = BigDecimal.ZERO;
		BigDecimal totalDTAmount = BigDecimal.ZERO;
		BigDecimal unitAmount = BigDecimal.ZERO;
		Double totStCapHours = totStHours;
		Double totOtCapHours = totOtHours;
		Double totDtCapHours = totDthours;
		StringBuffer rate = new StringBuffer();
		if (null != billingQueue.getCappedMaxSTHours()
				&& totStHours.compareTo(billingQueue.getCappedMaxSTHours().doubleValue()) > 0) {
			totStCapHours = billingQueue.getCappedMaxSTHours().doubleValue();
		}
		if (null != billingQueue.getCappedMaxOTHours()
				&& totOtHours.compareTo(billingQueue.getCappedMaxOTHours().doubleValue()) > 0) {
			totOtCapHours = billingQueue.getCappedMaxOTHours().doubleValue();
		}
		if (null != billingQueue.getCappedMaxDTHours()
				&& totDthours.compareTo(billingQueue.getCappedMaxDTHours().doubleValue()) > 0) {
			totDtCapHours = billingQueue.getCappedMaxDTHours().doubleValue();
		}
		log.info("billingqueue  ---- {} ", billingQueue);
		log.info("billingqueue getbillingclientrate ----" + billingQueue.getBillClientrate());
		log.info("totalUnits ---- {}", totalUnits);
		if (null != billingQueue.getBillClientrate()) {
			unitAmount = billingQueue.getBillClientrate().multiply(BigDecimal.valueOf(totalUnits));
			invoiceDetail.setBillClientrate(billingQueue.getBillClientrate());
			groupBySplitter.append(billingQueue.getBillClientrate()).append(",");
			rate.append(billingQueue.getBillClientrate().toString());
		}

		// totalWorkhours = totStCapHours + totOtCapHours + totDtCapHours;
		if (null != billingQueue.getBillToClientST()) {
			totalSTAmount = billingQueue.getBillToClientST().multiply(BigDecimal.valueOf(totStCapHours));
			invoiceDetail.setBillToClientST(billingQueue.getBillToClientST());
			groupBySplitter.append(billingQueue.getBillToClientST()).append(",");
			rate.append(billingQueue.getBillToClientST().toString());
		}
		if (null != billingQueue.getBillToClientOT()) {
			totalOTAmount = billingQueue.getBillToClientOT().multiply(BigDecimal.valueOf(totOtCapHours));
			invoiceDetail.setBillToClientOT(billingQueue.getBillToClientOT());
			groupBySplitter.append(billingQueue.getBillToClientOT()).append(",");
			rate.append(billingQueue.getBillToClientOT().toString());
		}
		if (null != billingQueue.getBillToClientDT()) {
			totalDTAmount = billingQueue.getBillToClientDT().multiply(BigDecimal.valueOf(totDtCapHours));
			invoiceDetail.setBillToClientDT(billingQueue.getBillToClientDT());
			groupBySplitter.append(billingQueue.getBillToClientDT()).append(",");
			rate.append(billingQueue.getBillToClientDT().toString());
		}
		totalAmount = totalSTAmount.add(totalOTAmount).add(totalDTAmount).add(unitAmount);
		invoiceDetail.setUnitAmount(unitAmount.doubleValue());
		invoiceDetail.setTotalUnits(totalUnits);
		invoiceDetail.setStHours(totStCapHours);
		invoiceDetail.setOtHours(totOtCapHours);
		invoiceDetail.setDtHours(totDtCapHours);
		invoiceDetail.setStAmount(totalSTAmount.doubleValue());
		invoiceDetail.setOtAmount(totalOTAmount.doubleValue());
		invoiceDetail.setDtAmount(totalDTAmount.doubleValue());
		invoiceDetail.setTotalAmount(totalAmount.doubleValue());
		invoiceDetail.setWorkHours(workhours);
		invoiceDetail.setStotdtRate(StringUtils.substring(groupBySplitter.toString(), 0, groupBySplitter.toString().length() - 1));
		invoiceDetail.setStotdtDoubleRate(rate.toString());
		return totalAmount;
	}

	private synchronized List<InvoiceExceptionDetailsDTO> setInvoiceExceptionDetailsDTO(List<Timesheet> timesheets,
			BillingQueue billingQueue, String currencyType) {
		Boolean isException = false;
		List<InvoiceExceptionDetailsDTO> invoiceExceptionDetailsDTOList = new ArrayList<>();
		for (Timesheet timesheet : timesheets) {
			if (!STATUS_LIST.contains(timesheet.getStatus())) {
				isException = true;
				break;
			}
		}
		if (isException) {
			timesheets.forEach(timesheet -> {
				InvoiceDetail invoiceDetail = new InvoiceDetail();

				Integer timesheetUnits = 0;
				if (null != billingQueue.getBillClientrate()) {
					List<UUID> timesheetUUID = new ArrayList<>();
					timesheetUUID.add(timesheet.getId());
					timesheetUnits = getTotalUnits(timesheetUUID);
				}
				calculateHoursbasedAmount(billingQueue, invoiceDetail, timesheet.getStHours(), timesheet.getOtHours(),
						timesheet.getDtHours(), timesheet.getWorkHours(), timesheetUnits);
				InvoiceExceptionDetailsDTO invoiceExceptionDetailsDTO = new InvoiceExceptionDetailsDTO();
				invoiceExceptionDetailsDTO.setContractorName(timesheet.getEmployee().getName());
				invoiceExceptionDetailsDTO.setTimesheetId(timesheet.getId().toString());
				invoiceExceptionDetailsDTO.setSt(timesheet.getStHours().toString());
				invoiceExceptionDetailsDTO.setOt(timesheet.getOtHours().toString());
				invoiceExceptionDetailsDTO.setDt(timesheet.getDtHours().toString());

				if (null != timesheetUnits && !timesheetUnits.equals(Integer.valueOf(0))) {
					invoiceExceptionDetailsDTO
							.setTotalHours(invoiceDetail.getWorkHours().toString() + "/" + timesheetUnits);
				} else {
					invoiceExceptionDetailsDTO.setTotalHours(invoiceDetail.getWorkHours().toString());
				}
				if (STATUS_LIST.contains(timesheet.getStatus())) {
					invoiceExceptionDetailsDTO.setStatus(TimesheetConstants.VERIFIED);
				} else {
					invoiceExceptionDetailsDTO.setStatus(TimesheetConstants.UNVERIFIED);
				}
				invoiceExceptionDetailsDTO.setAmount(CalculationUtil.priceConversion(invoiceDetail.getTotalAmount()));
				invoiceExceptionDetailsDTO.setCurrencyType(currencyType);
				invoiceExceptionDetailsDTO.setWeekEndDate(timesheet.getEndDate());
				invoiceExceptionDetailsDTO.setUntis(timesheetUnits);
				invoiceExceptionDetailsDTOList.add(invoiceExceptionDetailsDTO);
			});
		}
		return invoiceExceptionDetailsDTOList;
	}
	/*-------------------------------------------------Calculating Billing Amount -----End------------------------*/

	@SuppressWarnings({ "unchecked", "unused" })
	private BigDecimal setExpenseList(List<ExpenseView> expensesList, List<ExpenseView> expensesReceiptList)
			throws IOException {

		BigDecimal expenseAmount = new BigDecimal(0);
		String storagePath;
		for (ExpenseView expenseView : expensesList) {
			if (StringUtils.equalsIgnoreCase(expenseView.getBillable(), "Yes")) {
				expenseAmount = expenseAmount.add(expenseView.getExpenseAmount());
			}
			Long currencyId = expenseView.getCurrencyId();
			String fileName = expenseView.getFileName();
			expenseView.setSUBREPORT_DIR(jasperReport);
			log.info("SUBREPORT DIR : {} ", expenseView.getSUBREPORT_DIR());
			if (StringUtils.isNoneBlank(fileName)) {
				List<ExpenseImage> expenseImageList = new ArrayList<>();
				Long createdBy = expenseView.getCreatedBy();
				String imageFile = null;
				String mimeType = FilenameUtils.getExtension(fileName);
				if (createdBy != null) {
					if (mimeType.equalsIgnoreCase(PDF)) {
						storagePath = FILE_UPLOAD_LOCATION
								.concat(createdBy.toString() + FILE_UPLOAD_INDEX + FilenameUtils.getName(fileName));

						log.info("storagePath 1: {} ", storagePath);
						PDDocument document = null;
						document = PDDocument.load(storagePath);
						List<PDPage> list = document.getDocumentCatalog().getAllPages();
						int pageNumber = 1;
						for (PDPage page : list) {
							fileName = fileName.replace(".pdf", "");

							if (pageNumber != 1) {
								imageFile = fileName + "_" + pageNumber + ".jpg";
							} else {
								imageFile = fileName + ".jpg";
							}
							storagePath = FILE_UPLOAD_LOCATION + createdBy.toString() + FILE_UPLOAD_INDEX
									+ FilenameUtils.getName(imageFile);
							log.info("storagePath 2: {} ", storagePath);
							File file = new File(storagePath);
							ExpenseImage expenseImage = new ExpenseImage();
							if (file.exists()) {
								expenseImage.setReceiptUrl(storagePath);
								expenseImageList.add(expenseImage);
							}
							pageNumber++;
						}
						document.close();
					} else {
						storagePath = FILE_UPLOAD_LOCATION + createdBy.toString() + FILE_UPLOAD_INDEX + THUMB_FOLDER
								+ FILE_UPLOAD_INDEX + FilenameUtils.getName(fileName);
						File file = new File(storagePath);
						ExpenseImage expenseImage = new ExpenseImage();
						if (file.exists()) {
							expenseImage.setReceiptUrl(storagePath);
							expenseImageList.add(expenseImage);
						}
						log.info("storagePath 3: {} ", storagePath);
					}
					expenseView.setExpenseImageList(expenseImageList);
				}
				expensesReceiptList.add(expenseView);
			}
			if (currencyId.longValue() != 1) {
				expenseView.setVendorAmount(expenseView.getCurrencySymbol() + " " + expenseView.getExpenseAmount()
						+ " ("+com.tm.commonapi.constants.InvoiceConstants.USD+ expenseView.getConvertedAmount() + ") ");
			} else {
				expenseView.setVendorAmount(com.tm.commonapi.constants.InvoiceConstants.USD +" "+ expenseView.getExpenseAmount());
			}
		}
		return expenseAmount;
	}
}
