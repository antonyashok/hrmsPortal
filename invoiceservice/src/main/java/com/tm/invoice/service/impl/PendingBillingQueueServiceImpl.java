package com.tm.invoice.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.exception.BusinessException;
import com.tm.commonapi.exception.RecordNotFoundException;
import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;
import com.tm.employee.dto.EmployeeDTO;
import com.tm.invoice.domain.PtoAvailableDTO;
import com.tm.invoice.domain.PurchaseOrder;
import com.tm.invoice.domain.Task;
import com.tm.invoice.dto.BillingQueueDTO;
import com.tm.invoice.dto.TaskDTO;
import com.tm.invoice.exception.BillProfileAlreadyMappedException;
import com.tm.invoice.exception.BillProfileNotExpiredException;
import com.tm.invoice.exception.ContractorEmailIdAlreadyExistException;
import com.tm.invoice.exception.ContractorIdAlreadyExistException;
import com.tm.invoice.exception.InvoiceBadRequestException;
import com.tm.invoice.exception.PoNotMappedException;
import com.tm.invoice.exception.ProfileEndDateLessThanEffectiveEndDateException;
import com.tm.invoice.exception.ProfileStartDateGreaterThanJoiningDateException;
import com.tm.invoice.exception.ProfileStartDateLessThanEffectiveStartDateException;
import com.tm.invoice.exception.StartDateAndEndDateExceedException;
import com.tm.invoice.exception.TaskNameAlreadyExistException;
import com.tm.invoice.exception.TaskNotExistException;
import com.tm.invoice.mongo.domain.BillingQueue;
import com.tm.invoice.mongo.repository.BillingQueueRepository;
import com.tm.invoice.repository.PurchaseOrderRepository;
import com.tm.invoice.service.PendingBillingQueueService;
import com.tm.invoice.service.mapper.PendingBillingQueueMapper;
import com.tm.invoice.service.resttemplate.BillingProfileRestTemplate;
import com.tm.invoice.service.resttemplate.CustomerProfileRestTemplate;
import com.tm.invoice.service.resttemplate.EmployeeRestTemplate;
import com.tm.invoice.util.InvoiceCommonUtils;
import com.tm.invoice.enums.ActiveFlag;
import org.apache.commons.collections.CollectionUtils;

@Service
@Component
public class PendingBillingQueueServiceImpl implements PendingBillingQueueService {

	private static final String PROF_START_DATE_EXCEP = "The given profile start date should be greater than or equal to employee joining date";
	private BillingQueueRepository pendingBillingQueueRepository;
	private PurchaseOrderRepository purchaseOrderRepository;
	private RestTemplate restTemplate;
	private DiscoveryClient discoveryClient;
	public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";
	public static final String EMPLOYEE_DATA_IS_AVAILABLE = "Employee Datum is not available";
	public static final String EMPLOYEE_ID_IS_REQUIRED = "Employee Id is required";
	private static final Logger log = LoggerFactory.getLogger(PendingBillingQueueServiceImpl.class);

	@Inject
	public PendingBillingQueueServiceImpl(BillingQueueRepository pendingBillingQueueRepository,
			PurchaseOrderRepository purchaseOrderRepository, @LoadBalanced final RestTemplate restTemplate,
			@Qualifier("discoveryClient") final DiscoveryClient discoveryClient) {
		this.pendingBillingQueueRepository = pendingBillingQueueRepository;
		this.restTemplate = restTemplate;
		this.discoveryClient = discoveryClient;
		this.purchaseOrderRepository = purchaseOrderRepository;
	}

	@Override
	public Page<BillingQueueDTO> getPendingBillingQueueList(UUID engagementId, String status, Pageable pageable) {
		Pageable pageRequest = pageable;
		if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
			pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC,
					InvoiceConstants.LAST_UPDATED_ON);
		}
		Page<BillingQueue> pendingbillingQueueList = pendingBillingQueueRepository
				.getPendingBillingQueueList(pageRequest, engagementId, status);
		List<BillingQueueDTO> result = new ArrayList<>();
		Long totalElements = 0L;
		if (null != pendingbillingQueueList) {
			for (BillingQueue pendingBillingQueueDetails : pendingbillingQueueList) {
				BillingQueueDTO pendingBillingQueueDTO = mapPendingBillingQueueIntoPendingBillingQueueDTO(
						pendingBillingQueueDetails);
				result.add(pendingBillingQueueDTO);
			}
			totalElements = pendingbillingQueueList.getTotalElements();
		}
		return new PageImpl<>(result, pageable, totalElements);
	}
	
	@Override
	public Page<BillingQueueDTO> getPendingBillingQueueList(Long employeeId, String status, Pageable pageable) {
		Pageable pageRequest = pageable;
		if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
			pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC,
					InvoiceConstants.LAST_UPDATED_ON);
		}
		Page<BillingQueue> pendingbillingQueueList = pendingBillingQueueRepository
				.getPendingBillingQueueList(pageRequest, employeeId, status);
		List<BillingQueueDTO> result = new ArrayList<>();
		Long totalElements = 0L;
		if (null != pendingbillingQueueList) {
			for (BillingQueue pendingBillingQueueDetails : pendingbillingQueueList) {
				BillingQueueDTO pendingBillingQueueDTO = mapPendingBillingQueueIntoPendingBillingQueueDTO(
						pendingBillingQueueDetails);
				result.add(pendingBillingQueueDTO);
			}
			totalElements = pendingbillingQueueList.getTotalElements();
		}
		return new PageImpl<>(result, pageable, totalElements);
	}

	private BillingQueueDTO mapPendingBillingQueueIntoPendingBillingQueueDTO(BillingQueue pendingbillingQueue) {
		return PendingBillingQueueMapper.INSTANCE.pendingBillingQueueToPendingBillingQueueDTO(pendingbillingQueue);
	}

	@Override
	public BillingQueueDTO getPendingBillingQueueDetail(UUID pendingBillingQueueId) {
		BillingQueue pendingBillingQueue = pendingBillingQueueRepository.findOne(pendingBillingQueueId);
		if (null == pendingBillingQueue) {
			throw new RecordNotFoundException(InvoiceConstants.NO_RECORDS_FOUND);
		}
		return mapPendingBillingQueueIntoPendingBillingQueueDTO(pendingBillingQueue);
	}

	@Override
	public BillingQueueDTO activeInactiveBillingDetails(BillingQueueDTO pendingBillDetailsDTO) {

		BillingQueue billingQueue = pendingBillingQueueRepository.findOne(pendingBillDetailsDTO.getBillingQueueId());
		if (null == billingQueue) {
			throw new BusinessException("Billing Queue Not Exist");
		}

		if (pendingBillDetailsDTO.getStatus().equals(InvoiceConstants.ACTIVE)) {
			billingQueue.setStatus(InvoiceConstants.ACTIVE);
		} else if (pendingBillDetailsDTO.getStatus().equals(InvoiceConstants.INACTIVE)) {
			billingQueue.setStatus(InvoiceConstants.INACTIVE);
			if (InvoiceCommonUtils.convertStringToDate(billingQueue.getEffectiveEndDate()).after(new Date())) {
				billingQueue.setEffectiveEndDate(InvoiceCommonUtils.convertDateToString(new Date()));
			}
		}
		pendingBillingQueueRepository.save(billingQueue);
		return PendingBillingQueueMapper.INSTANCE.pendingBillingQueueToPendingBillingQueueDTO(billingQueue);
	}

	@Override
	public BillingQueueDTO submitBillingDetails(BillingQueueDTO pendingBillDetailsDTO) {
		billingProfileDateValidation(pendingBillDetailsDTO);
		
		if (StringUtils.isNotEmpty(pendingBillDetailsDTO.getEngagementId()) && null == pendingBillDetailsDTO.getPurchaseOrderId()) {
			List<PurchaseOrder> purchaseOrderList = purchaseOrderRepository.findByEngagementIdAndByActive(
					UUID.fromString(pendingBillDetailsDTO.getEngagementId()), ActiveFlag.Y);
			if (CollectionUtils.isNotEmpty(purchaseOrderList)) {
				pendingBillDetailsDTO.setPurchaseOrderId(purchaseOrderList.get(0).getPurchaseOrderId());
			}else{
				throw new PoNotMappedException("");
			}
		}
		pendingBillDetailsDTO = saveBillingProfile(pendingBillDetailsDTO);
		savePTOAvailable(prparePTOAvailable(pendingBillDetailsDTO));
		BillingQueue billingQueue = PendingBillingQueueMapper.INSTANCE
				.pendingBillingQueueDTOToPendingBillingQueue(pendingBillDetailsDTO);
		billingQueue.setStatus(InvoiceConstants.ACTIVE);
		if (null == billingQueue.getId()) {
			billingQueue.setId(UUID.randomUUID());
		}
		pendingBillingQueueRepository.save(billingQueue);
		return pendingBillDetailsDTO;
	}

	public BillingQueueDTO saveBillingProfile(BillingQueueDTO billingQueueDTO) {
		BillingProfileRestTemplate billingProfileRestTemplate = new BillingProfileRestTemplate(
				restTemplate, DiscoveryClientAndAccessTokenUtil
						.discoveryClient(CustomerProfileRestTemplate.ENGAGEMENT_GROUP_KEY, discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		BillingQueueDTO billingQueueDTODetails = billingProfileRestTemplate.createBillingProfile(billingQueueDTO);
		if (Objects.nonNull(billingQueueDTODetails)) {
			return billingQueueDTODetails;
		}
		return billingQueueDTO;
	}

	public PtoAvailableDTO prparePTOAvailable(BillingQueueDTO pendingBillDetailsDTO) {
		PtoAvailableDTO ptoAvailableDTO = new PtoAvailableDTO();
		ptoAvailableDTO.setAllotedHours(pendingBillDetailsDTO.getPtoAllottedHours().toString());
		ptoAvailableDTO.setStartDate(pendingBillDetailsDTO.getEffectiveStartDate());
		ptoAvailableDTO.setEndDate(pendingBillDetailsDTO.getEffectiveEndDate());
		ptoAvailableDTO.setEmployeeId(pendingBillDetailsDTO.getEmployeeId());
		ptoAvailableDTO.setEngagementId(pendingBillDetailsDTO.getEngagementId());
		return ptoAvailableDTO;

	}

	public void savePTOAvailable(PtoAvailableDTO ptoAvailable) {
		BillingProfileRestTemplate billingProfileRestTemplate = new BillingProfileRestTemplate(
				restTemplate, DiscoveryClientAndAccessTokenUtil
						.discoveryClient(CustomerProfileRestTemplate.TIMEOFF_GROUP_KEY, discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		billingProfileRestTemplate.createPTOAvailable(ptoAvailable);
	}

	@Override
	public BillingQueueDTO updateBillingDetails(BillingQueueDTO pendingBillDetailsDTO) {
		checkUpdateBillingQueueExists(pendingBillDetailsDTO.getEngagementId(), pendingBillDetailsDTO.getEmployeeId(),
				pendingBillDetailsDTO.getBillingQueueId());
		billingProfileDateValidation(pendingBillDetailsDTO);
		BillingQueue billingQueue = PendingBillingQueueMapper.INSTANCE
				.pendingBillingQueueDTOToPendingBillingQueue(pendingBillDetailsDTO);
		pendingBillingQueueRepository.save(billingQueue);
		return pendingBillDetailsDTO;
	}

	@Override
	public BillingQueueDTO createBillingDetails(BillingQueueDTO pendingBillDetailsDTO) {
		checkBillingQueueExists(pendingBillDetailsDTO.getEngagementId(), pendingBillDetailsDTO.getEmployeeId());
		billingProfileDateValidation(pendingBillDetailsDTO);
		BillingQueue billingQueue = PendingBillingQueueMapper.INSTANCE
				.pendingBillingQueueDTOToPendingBillingQueue(pendingBillDetailsDTO);
		pendingBillingQueueRepository.save(billingQueue);
		return PendingBillingQueueMapper.INSTANCE.pendingBillingQueueToPendingBillingQueueDTO(billingQueue);
	}

	private void billingProfileDateValidation(BillingQueueDTO billingQueueDTO) {
		Date engagementStartDate = InvoiceCommonUtils.convertStringToDate(billingQueueDTO.getProfileActiveDate());
		Date engagementEndDate = InvoiceCommonUtils.convertStringToDate(billingQueueDTO.getProfileEndDate());
		Date profileStartDate = InvoiceCommonUtils.convertStringToDate(billingQueueDTO.getEffectiveStartDate());
		Date profileEndDate = InvoiceCommonUtils.convertStringToDate(billingQueueDTO.getEffectiveEndDate());
		if (profileStartDate.after(profileEndDate)) {
			throw new StartDateAndEndDateExceedException("");
		}
		if (engagementStartDate.after(profileStartDate)) {
			throw new ProfileStartDateLessThanEffectiveStartDateException("");
		}
		if (engagementEndDate.before(profileEndDate)) {
			throw new ProfileEndDateLessThanEffectiveEndDateException("");
		}
		billingProfileJoiningDateValidation(billingQueueDTO, profileStartDate);
		if(null != billingQueueDTO.getContractorId()){
			checkContractorId(billingQueueDTO.getEngagementId(), billingQueueDTO.getBillingQueueId(), billingQueueDTO.getContractorId());
		}
		if(null != billingQueueDTO.getContractorMailId()){
			checkContractorMailId(billingQueueDTO.getEngagementId(),  billingQueueDTO.getBillingQueueId(), billingQueueDTO.getContractorMailId());
		}
		//checkBillingQueueInActiveExists(billingQueueDTO);
	}

	private void billingProfileJoiningDateValidation(BillingQueueDTO billingQueueDTO, Date profileStartDate) {
		if (null != billingQueueDTO.getEmployeeId()) {
			EmployeeDTO employee = getEmployeeDetails(billingQueueDTO.getEmployeeId());
			if (Objects.nonNull(employee)) {
				if (employee.getJoiningDate().after(profileStartDate)) {
					throw new ProfileStartDateGreaterThanJoiningDateException(PROF_START_DATE_EXCEP);
				}
			}
		}
	}
	
	private void checkContractorId(String engagementId,UUID billingQueueId,String contractorId){
		List<BillingQueue> billingQueueList = new ArrayList<>();
		if(null != billingQueueId){
			billingQueueList = pendingBillingQueueRepository.findByContractorIdAndEngagementIdAndIdNot(contractorId, engagementId,billingQueueId);
		}else{
			billingQueueList = pendingBillingQueueRepository.findByContractorIdAndEngagementId(contractorId, engagementId);
		}
		if(CollectionUtils.isNotEmpty(billingQueueList)){
			throw new ContractorIdAlreadyExistException(contractorId);
		}
	}
	
	private void checkContractorMailId(String engagementId,UUID billingQueueId,String contractorMailId){
		List<BillingQueue> billingQueueList = new ArrayList<>();
		if(null !=billingQueueId){
			billingQueueList = pendingBillingQueueRepository.findByContractorMailIdAndEngagementIdAndIdNot(contractorMailId, engagementId,billingQueueId);
		}else{
			billingQueueList = pendingBillingQueueRepository.findByContractorMailIdAndEngagementId(contractorMailId, engagementId);
		}
		if(CollectionUtils.isNotEmpty(billingQueueList)){
			throw new ContractorEmailIdAlreadyExistException(contractorMailId);
		}
	}

	private void checkBillingQueueInActiveExists(BillingQueueDTO bllingQueueDTO) {
		List<BillingQueue> pendingbillingQueueList = pendingBillingQueueRepository
				.findByEngagementIdAndEmployeeIdAndStatusAndIdNot(bllingQueueDTO.getEngagementId(),bllingQueueDTO.getEmployeeId(), "Active", bllingQueueDTO.getBillingQueueId());
		if (!CollectionUtils.isEmpty(pendingbillingQueueList)) {
			pendingbillingQueueList = pendingbillingQueueList.stream()
					.sorted(Comparator.comparing(BillingQueue::getEffectiveEndDate).reversed())
					.collect(Collectors.toList());
			if (InvoiceCommonUtils.convertStringToDate(bllingQueueDTO.getEffectiveStartDate()).before(
					InvoiceCommonUtils.convertStringToDate(pendingbillingQueueList.get(0).getEffectiveEndDate()))) {
				throw new BillProfileNotExpiredException(pendingbillingQueueList.get(0).getEffectiveEndDate());
			}else{
				if(StringUtils.equals("Active", pendingbillingQueueList.get(0).getStatus())){
					updateBillingProfile(pendingbillingQueueList.get(0).getEmployeeEngagementId());
				}
			}
		}
	}

	private void checkBillingQueueExists(String engagementId, long employeeId) {
		List<BillingQueue> pendingbillingQueueList = pendingBillingQueueRepository
				.checkPendingBillingQueue(engagementId, employeeId);
		if (!CollectionUtils.isEmpty(pendingbillingQueueList)) {
			throw new BillProfileAlreadyMappedException("");
		}
	}

	private void checkUpdateBillingQueueExists(String engagementId, long employeeId, UUID billingQueueId) {
		List<BillingQueue> pendingbillingQueueList = pendingBillingQueueRepository.findByEngagementIdAndEmployeeIdAndIdNotAndStatusNot(
				engagementId, employeeId, billingQueueId, "Inactive");

		if (!CollectionUtils.isEmpty(pendingbillingQueueList)) {
			throw new BillProfileAlreadyMappedException("");
		}
	}

	private void validateTaskRate(List<TaskDTO> taskDTOList) {
		if (CollectionUtils.isEmpty(taskDTOList)) {
			throw new TaskNotExistException("");
		}
		List<String> subTaskDetails = new ArrayList<>();
		for (TaskDTO taskDTO : taskDTOList) {
			if (subTaskDetails.contains(taskDTO.getTaskName())) {
				throw new TaskNameAlreadyExistException(taskDTO.getTaskName());
			}
			subTaskDetails.add(taskDTO.getTaskName());
		}

	}
	
	public String updateBillingProfile(String employeeEngagementId) {
		BillingProfileRestTemplate billingProfileRestTemplate = new BillingProfileRestTemplate(
				restTemplate, DiscoveryClientAndAccessTokenUtil
						.discoveryClient(CustomerProfileRestTemplate.ENGAGEMENT_GROUP_KEY, discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		String status = billingProfileRestTemplate.updateBillingProfile(employeeEngagementId);
		if (StringUtils.isNotEmpty(status)) {
			return "OK";
		}
		return "OK";
	}

	@Override
	public EmployeeDTO getEmployeeDetails(Long employeeId) {
		EmployeeRestTemplate employeeRestTemplate = new EmployeeRestTemplate(restTemplate,
				DiscoveryClientAndAccessTokenUtil.discoveryClient(COMMON_GROUP_KEY, discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		EmployeeDTO employeeDTO = employeeRestTemplate.getEmployeeDetails(employeeId);
		if (Objects.nonNull(employeeDTO)) {
			if (Objects.isNull(employeeDTO.getEmployeeId())) {
				log.error("Employee Id not Found");
				throw new InvoiceBadRequestException(EMPLOYEE_ID_IS_REQUIRED);
			}
		} else {
			throw new InvoiceBadRequestException(EMPLOYEE_DATA_IS_AVAILABLE);
		}
		return employeeDTO;
	}

	@Override
	public BillingQueueDTO updateTaskBillingDetails(UUID pendingBillingQueueId, List<TaskDTO> taskDTO) {
		validateTaskRate(taskDTO);
		BillingQueue billingQueue = pendingBillingQueueRepository.findOne(pendingBillingQueueId);
		List<Task> taskList = PendingBillingQueueMapper.INSTANCE.taskListToTaskDTOList(taskDTO);
		billingQueue.setSubTasksDetails(taskList);
		billingQueue = pendingBillingQueueRepository.save(billingQueue);
		return PendingBillingQueueMapper.INSTANCE
				.pendingBillingQueueToPendingBillingQueueDTO(billingQueue);
	}

}
