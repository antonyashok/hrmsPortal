package com.tm.invoice.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.dto.InvoiceBillWatchDTO;
import com.tm.invoice.dto.InvoiceQueueDTO;
import com.tm.invoice.dto.InvoiceReturnDTO;
import com.tm.invoice.exception.InvoiceBadRequestException;
import com.tm.invoice.mapper.InvoiceMapper;
import com.tm.invoice.mongo.domain.AuditFields;
import com.tm.invoice.mongo.domain.InvoiceBillWatch;
import com.tm.invoice.mongo.domain.InvoiceExceptionDetails;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.domain.InvoiceReturn;
import com.tm.invoice.mongo.repository.InvoiceBillWatchRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.mongo.repository.InvoiceReturnRepository;
import com.tm.invoice.service.InvoiceBillWatchService;
import com.tm.invoice.service.mapper.InvoiceBillWatchMapper;
import com.tm.invoice.service.mapper.InvoiceQueueMapper;
import com.tm.invoice.service.resttemplate.EmployeeRestTemplate;
import com.tm.invoice.util.InvoiceCommonUtils;

@Service
@Transactional
public class InvoiceBillWatchServiceImpl implements InvoiceBillWatchService {

	private static final Logger log = LoggerFactory
			.getLogger(InvoiceBillWatchServiceImpl.class);
	private InvoiceQueueRepository invoiceQueueRepository;
	private InvoiceBillWatchRepository invoiceBillWatchRepository;
	private InvoiceReturnRepository invoiceReturnRepository;

	public static final String INVOICE_DELIVERED_STATUS = "Delivered";
	public static final String INVOICE_PENDING_APPROVAL_STATUS = "Pending Approval";
	public static final String RETURN_PROCESS = "Initiate to Return Process";
	public static final String EXCEPTION_REPORT = "Push to Exception Report";
	public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";
	public static final String EMPLOYEE_DATA_IS_AVAILABLE = "Employee Datum is not available";
	public static final String EMPLOYEE_ID_IS_REQUIRED = "Employee Id is required";
	public static final String INVOICE_RETURN_STATUS = "Invoice Return";
	public static final String EXCEPTION_SOURCE = "Bill Watch";

	@Autowired
	@LoadBalanced
	RestTemplate restTemplate;

	@Autowired
	@Qualifier("discoveryClient")
	DiscoveryClient discoveryClient;

	@Inject
	public InvoiceBillWatchServiceImpl(
			InvoiceQueueRepository invoiceQueueRepository,
			InvoiceBillWatchRepository invoiceBillWatchRepository,
			InvoiceReturnRepository invoiceReturnRepository,
			RestTemplate restTemplate) {
		this.invoiceQueueRepository = invoiceQueueRepository;
		this.invoiceBillWatchRepository = invoiceBillWatchRepository;
		this.invoiceReturnRepository = invoiceReturnRepository;
		this.restTemplate = restTemplate;
	}

	@Transactional(readOnly = true)
	@Override
	public Page<InvoiceQueueDTO> getInvoiceQueues(Long billingSpecialistId,
			Pageable pageable) {
		List<String> statuses = new ArrayList<>();
		statuses.add(INVOICE_DELIVERED_STATUS);
		statuses.add(INVOICE_PENDING_APPROVAL_STATUS);
		return getInvoiceQueues(billingSpecialistId, pageable, statuses, false);
	}

	private Page<InvoiceQueueDTO> getInvoiceQueues(Long billingSpecialistId,
			Pageable pageable, List<String> statuses, boolean isHistorical) {
		Page<InvoiceQueue> invoiceQueues = invoiceQueueRepository
				.getInvoiceQueues(billingSpecialistId, statuses, pageable,
						isHistorical);
		List<InvoiceQueueDTO> result = new ArrayList<>();
		if (Objects.nonNull(invoiceQueues)
				&& CollectionUtils.isNotEmpty(invoiceQueues.getContent())) {

			invoiceQueues
				.forEach(invoiceQueue -> {
				InvoiceQueueDTO invoiceQueueDTO = InvoiceQueueMapper.INSTANCE
						.invoiceQueueToInvoiceQueueDTO(invoiceQueue);
				invoiceQueueDTO.setAmountStr(InvoiceCommonUtils
						.roundOfValue(invoiceQueue.getAmount()));

				Query billWatchQuery = new Query();
				billWatchQuery = billWatchQuery.addCriteria(Criteria
						.where("invoiceNumber").is(
								invoiceQueueDTO.getInvoiceNumber()));

				List<InvoiceBillWatch> billWatchList = invoiceBillWatchRepository
						.getBillWatchByInvoiceNumber(invoiceQueueDTO
								.getInvoiceNumber());
				List<InvoiceBillWatchDTO> billWatchListDTO = new ArrayList<>();
				billWatchList.forEach(invoiceBillWatch -> {

					InvoiceBillWatchDTO invoiceBillWatchDTO = InvoiceBillWatchMapper.INSTANCE
							.invoiceBillWatchToInvoiceBillWatchDTO(invoiceBillWatch);

					if (invoiceQueueDTO.getStatus().equalsIgnoreCase(
							INVOICE_PENDING_APPROVAL_STATUS)) {
						invoiceBillWatchDTO.setAction(EXCEPTION_REPORT);
					} else if (invoiceQueueDTO.getStatus()
							.equalsIgnoreCase(INVOICE_DELIVERED_STATUS)) {
						invoiceBillWatchDTO.setAction(RETURN_PROCESS);
					}

					billWatchListDTO.add(invoiceBillWatchDTO);

				});

				invoiceQueueDTO.setInvoiceBillWatch(billWatchListDTO);

				if (isHistorical && null != invoiceQueue.getUpdated()
						&& null != invoiceQueue.getUpdated().getOn()) {
					invoiceQueueDTO.setSubmittedDate(InvoiceCommonUtils
							.getFormattedDate(invoiceQueue.getUpdated()
									.getOn()));
				}
				result.add(invoiceQueueDTO);
			});
			return new PageImpl<>(result, pageable,
					invoiceQueues.getTotalElements());
		}
		return new PageImpl<>(result, pageable,
            invoiceQueues.getTotalElements());
	}

	@Override
	public InvoiceReturnDTO createInvoiceReturn(String invoiceNumber) {
		List<String> statuses = new ArrayList<>();
		statuses.add(INVOICE_DELIVERED_STATUS);
		// statuses.add(INVOICE_PENDING_APPROVAL_STATUS);
		InvoiceReturn invoiceReturn = new InvoiceReturn();
		List<InvoiceQueue> invoiceQueueList = invoiceQueueRepository
				.getInvoiceQueuesByInvoiceNumber(invoiceNumber, statuses);
		invoiceQueueList
				.forEach(invoiceQueue -> {
					// InvoiceReturn invoiceReturn = new InvoiceReturn();
					EmployeeProfileDTO employeeProfileDTO = getLoggedInUser();

					invoiceReturn.setId(ResourceUtil.generateUUID());
					invoiceReturn
							.setCreated(prepareAuditFields(employeeProfileDTO
									.getEmployeeId()));
					invoiceReturn
							.setUpdated(prepareAuditFields(employeeProfileDTO
									.getEmployeeId()));
					invoiceReturn.setInvoiceNumber(invoiceQueue
							.getInvoiceNumber());
					invoiceReturn.setBillToClientId(invoiceQueue
							.getBillToClientId());
					invoiceReturn.setInvoiceSetupId(invoiceQueue
							.getInvoiceSetupId());
					invoiceReturn.setBillToClientName(invoiceQueue
							.getBillToClientName());
					invoiceReturn.setEndClientId(invoiceQueue.getEndClientId());
					invoiceReturn.setEndClientName(invoiceQueue
							.getEndClientName());
					invoiceReturn.setBillingSpecialistId(invoiceQueue
							.getBillingSpecialistId());
					invoiceReturn.setBillingSpecialistName(invoiceQueue
							.getBillingSpecialistName());
					invoiceReturn.setReportingManagerId((employeeProfileDTO
							.getReportingManagerId()));
					invoiceReturn.setCurrencyType(invoiceQueue
							.getCurrencyType());
					invoiceReturn.setAmount(invoiceQueue.getAmount());
					invoiceReturn.setStatus(INVOICE_RETURN_STATUS);
					invoiceReturnRepository.save(invoiceReturn);
					// InvoiceReturnDTO invoiceReturnDTO =
					// InvoiceMapper.INSTANCE.invoiceReturnToinvoiceReturnDTO(invoiceReturn);
				});
		InvoiceReturnDTO invoiceReturnDTO = InvoiceMapper.INSTANCE
				.invoiceReturnToinvoiceReturnDTO(invoiceReturn);
		removeInvoiceBillwatch(invoiceNumber);
		// return invoiceReturnDTO;
		return invoiceReturnDTO;
	}

	@Override
	public EmployeeProfileDTO getLoggedInUser() {
		EmployeeRestTemplate employeeRestTemplate = new EmployeeRestTemplate(
				restTemplate,
				DiscoveryClientAndAccessTokenUtil.discoveryClient(
						COMMON_GROUP_KEY, discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		EmployeeProfileDTO employeeProfileDTO = employeeRestTemplate
				.getEmployeeProfileDTO();
		if (Objects.nonNull(employeeProfileDTO)) {
			if (Objects.isNull(employeeProfileDTO.getEmployeeId())) {
				log.error("Employee Id not Found");
				throw new InvoiceBadRequestException(EMPLOYEE_ID_IS_REQUIRED);
			}
		} else {
			throw new InvoiceBadRequestException(EMPLOYEE_DATA_IS_AVAILABLE);
		}
		return employeeProfileDTO;
	}

	private AuditFields prepareAuditFields(Long employeeId) {
		AuditFields auditFields = new AuditFields();
		auditFields.setBy(employeeId);
		auditFields.setOn(new Date());
		return auditFields;
	}

	@Override
	public InvoiceQueueDTO createExceptionReport(String invoiceNumber) {
		List<String> statuses = new ArrayList<>();
		// statuses.add(INVOICE_DELIVERED_STATUS);
		statuses.add(INVOICE_PENDING_APPROVAL_STATUS);
		InvoiceQueueDTO invoiceQueueDTO = new InvoiceQueueDTO();

		List<InvoiceBillWatch> billWatchList = invoiceBillWatchRepository
				.getBillWatchByInvoiceNumber(invoiceNumber);
		List<InvoiceQueue> invoiceQueueList = invoiceQueueRepository
				.getInvoiceQueuesByInvoiceNumber(invoiceNumber, statuses);

		invoiceQueueList
				.forEach(invoiceQueue -> {
					billWatchList
							.forEach(invoiceBillWatch -> {
								List<InvoiceExceptionDetails> invoiceExceptionDetailsList = invoiceQueue
										.getInvoiceExceptionDetail();
								InvoiceExceptionDetails invoiceExceptionDetails = new InvoiceExceptionDetails();
								invoiceExceptionDetails
										.setOriginalHours(invoiceBillWatch
												.getOriginalHours());
								invoiceExceptionDetails
										.setActualHours(invoiceBillWatch
												.getActualHours());
								invoiceExceptionDetails
										.setOriginalExpenseCurrencyType(invoiceBillWatch
												.getOriginalExpenseCurrencyType());
								invoiceExceptionDetails
										.setOriginalExpenseAmount(invoiceBillWatch
												.getOriginalExpenseAmount());
								invoiceExceptionDetails
										.setActualExpenseCurrencyType(invoiceBillWatch
												.getActualExpenseCurrencyType());
								invoiceExceptionDetails
										.setActualExpenseAmount(invoiceBillWatch
												.getActualExpenseAmount());
								invoiceExceptionDetailsList
										.add(invoiceExceptionDetails);
								invoiceQueue
										.setInvoiceExceptionDetail(invoiceExceptionDetailsList);
								invoiceQueue
										.setExceptionSource(EXCEPTION_SOURCE);

							});
					invoiceQueueRepository.save(invoiceQueue);
					removeInvoiceBillwatch(invoiceNumber);
				});

		return invoiceQueueDTO;
	}

	public void removeInvoiceBillwatch(String invoiceNumber) {
		List<InvoiceBillWatch> invoiceBillWatchList = invoiceBillWatchRepository
				.getBillWatchByInvoiceNumber(invoiceNumber);
		invoiceBillWatchList.forEach(invoiceBillWatch -> 
			invoiceBillWatchRepository.delete(invoiceBillWatch));

	}

}
