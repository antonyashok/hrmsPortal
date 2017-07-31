package com.tm.engagement.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;
import com.tm.engagement.configuration.service.hystrix.commands.EmployeeRestTemplate;
import com.tm.engagement.configuration.service.hystrix.commands.OfficeLocationCommand;
import com.tm.engagement.constants.EngagementConstants;
import com.tm.engagement.domain.CustomerLocations;
import com.tm.engagement.domain.CustomerProfile;
import com.tm.engagement.domain.Engagement;
import com.tm.engagement.domain.EngagementHolidays;
import com.tm.engagement.domain.EngagementHolidaysView;
import com.tm.engagement.domain.EngagementOffice;
import com.tm.engagement.domain.EngagementView;
import com.tm.engagement.domain.ReportingMgrEngmtView;
import com.tm.engagement.exception.EngagementException;
import com.tm.engagement.repository.CustomerLocationsRepository;
import com.tm.engagement.repository.CustomerProfileRepository;
import com.tm.engagement.repository.EngagementContractorsRepository;
import com.tm.engagement.repository.EngagementHolidayRepository;
import com.tm.engagement.repository.EngagementOfficeRepository;
import com.tm.engagement.repository.EngagementRepository;
import com.tm.engagement.repository.EngagementViewRepository;
import com.tm.engagement.repository.ReportingMgrEngmtViewRepository;
import com.tm.engagement.service.EngagementService;
import com.tm.engagement.service.dto.EmployeeProfileDTO;
import com.tm.engagement.service.dto.EngagementDTO;
import com.tm.engagement.service.dto.EngagementViewDTO;
import com.tm.engagement.service.dto.InvoiceAttachmentsDTO;
import com.tm.engagement.service.dto.OfficeLocationDTO;
import com.tm.engagement.service.dto.ReportingMgrEngmtViewDTO;
import com.tm.engagement.service.mapper.EngagementMapper;
import com.tm.engagement.service.mapper.ReportingMgrEngmtViewMapper;
import com.tm.engagement.service.resources.HolidayResourceDTO;
import com.tm.engagement.web.rest.util.DateUtil;

@Service
@Transactional
public class EngagementServiceImpl implements EngagementService {

	private EngagementRepository engagementRepository;
	private EngagementOfficeRepository engagementOfficeRepository;
	private EngagementViewRepository engagementViewRepository;
	private CustomerProfileRepository customerRepository;
	private RestTemplate restTemplate;
	private DiscoveryClient discoveryClient;
	private EngagementHolidayRepository engagementHolidayRepository;
	private CustomerLocationsRepository customerLocationsRepository;
	
	private EngagementContractorsRepository engagementContractorsRepository;
	private MongoTemplate mongoTemplate;
	private static final String REVENUE = "Revenue";
    private static final String EXPENSE = "Expense";

	//private static final String CUSTOMER_DATA_IS_NOT_AVAILABLE = "Engagement is not available";
	//private static final String ENGAGEMENT_NAME_EXISTS = "Engagement Name already exists";
	//private static final String EMPLOYEE_ID_IS_REQUIRED = "Employee Id is required";
	//private static final String EMPLOYEE_DATA_IS_AVAILABLE = "Employee Datum is not available";
	//private static final String ACTIVE_CONFIGURED = "Y";
	//private static final String EMGAGEMENT_HOLIDAY = "Engagement Holiday already exists for the date";
	
	private ReportingMgrEngmtViewRepository contractorRepository;
	
	private static final String CUSTOMER_DATA_IS_NOT_AVAILABLE = "Project is not available";
	private static final String ENGAGEMENT_NAME_EXISTS = "Project Name already exists";
	private static final String EMPLOYEE_ID_IS_REQUIRED = "Employee Id is required";
	private static final String EMPLOYEE_DATA_IS_AVAILABLE = "Employee Datum is not available";
	private static final String ACTIVE_CONFIGURED = "Y";
	private static final String EMGAGEMENT_HOLIDAY = "Project Holiday already exists for the date";
	private static final String INVALID_DATE_FOR_THIS_ENGAGEMENT = "Invalid Date for this Engagement";
	
	private static final String EFFECTIVE_END_DATE = "effectiveEndDate";
	private static final String EFFECTIVE_START_DATE = "effectiveStartDate";

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Inject
	public EngagementServiceImpl(@NotNull final EngagementRepository engagementRepository,
			@LoadBalanced final RestTemplate restTemplate,
			@Qualifier("discoveryClient") final DiscoveryClient discoveryClient,
			EngagementViewRepository engagementViewRepository,
			@NotNull final EngagementOfficeRepository engagementOfficeRepository,
			@NotNull final CustomerProfileRepository customerRepository,
			EngagementHolidayRepository engagementHolidayRepository,
			ReportingMgrEngmtViewRepository contractorRepository,
			CustomerLocationsRepository customerLocationsRepository,
			EngagementContractorsRepository engagementContractorsRepository,
			MongoTemplate mongoTemplate) {
		this.engagementRepository = engagementRepository;
		this.restTemplate = restTemplate;
		this.discoveryClient = discoveryClient;
		this.engagementViewRepository = engagementViewRepository;
		this.engagementOfficeRepository = engagementOfficeRepository;
		this.customerRepository = customerRepository;
		this.engagementHolidayRepository = engagementHolidayRepository;
		this.contractorRepository = contractorRepository;
		this.customerLocationsRepository = customerLocationsRepository;
		this.engagementContractorsRepository = engagementContractorsRepository;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Page<EngagementViewDTO> getEngagementList(Pageable pageable) {
		Pageable pageableRequest = pageable;
		Page<EngagementView> engagementList = engagementViewRepository.findAll(pageableRequest);
		List<EngagementViewDTO> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(engagementList.getContent())) {
			engagementList.forEach(engagement -> result.add(mapEngagementToEngagementDTO(engagement)));
		}
		return new PageImpl<>(result, pageable, engagementList.getTotalElements());
	}

	private synchronized EngagementViewDTO mapEngagementToEngagementDTO(EngagementView engagementView) {
		return EngagementMapper.INSTANCE.engagementViewToengagementViewDTO(engagementView);
	}

	@Override
	public Engagement createEngagement(EngagementDTO engagementDTO) throws ParseException {
		Engagement engagement = EngagementMapper.INSTANCE.engagementDTOToEngagement(engagementDTO);
		engagement.setEngagementName(engagementDTO.getEngagementName().trim());
		engagement.setBillToMgrName(engagementDTO.getBillToMgrName().trim());
		engagement.setProjectMgrName(engagementDTO.getProjectMgrName().trim());
		engagement.setHiringMgrName(engagementDTO.getHiringMgrName().trim());
		engagement.setBillAddress(engagementDTO.getBillAddress().trim());
		engagement.setStateId(engagementDTO.getStateId());
		engagement.setCityId(engagementDTO.getCityId());
		engagement.setPostalCode(engagementDTO.getPostalCode().trim());

		EmployeeProfileDTO employee = getLoggedInUser();
		Long loggedUserId = null;
		if (Objects.nonNull(employee)) {
			loggedUserId = employee.getEmployeeId();
		}
		engagement.setCreatedBy(loggedUserId);
		engagement.setLastModifiedBy(loggedUserId);
		String startDateStr = engagementDTO.getEngmtStartDate();
		String endDateStr = engagementDTO.getEngmtEndDate();
		Date engmtStartDate = DateUtil.checkconvertStringToISODate(startDateStr);
		Date engmtEndDate = DateUtil.checkconvertStringToISODate(endDateStr);
		chkEngmtName(engagement);		
		validateEngagementDate(engagementDTO,dateFormat.format(engmtStartDate),dateFormat.format(engmtEndDate));
		engagement.setEngmtStartDate(engmtStartDate);
		engagement.setEngmtEndDate(engmtEndDate);
		Engagement engagementData = engagementRepository.save(engagement);
		List<OfficeLocationDTO> officeList = engagementDTO.getOfficeList();
		saveEngmtOffice(engagement, officeList);
		return engagementData;
	}

	private void validateEngagementDate(EngagementDTO engagementDTO, String startDate, String endDate) throws ParseException {
		CustomerProfile customerProfile=customerRepository.findOne(engagementDTO.getCustomerOrganizationId());
		List<CustomerProfile> customerProfileEndDtList=customerRepository.getCustomerEffectiveDates(engagementDTO.getCustomerOrganizationId(),dateFormat.parse(startDate),dateFormat.parse(endDate));
		if(customerProfileEndDtList.isEmpty()){
			throw new EngagementException("Engagement should be between "+dateFormat.format(customerProfile.getEffectiveStartDate())+" to "+dateFormat.format(customerProfile.getEffectiveEndDate()));
		}
	}

	private void saveEngmtOffice(Engagement engagement, List<OfficeLocationDTO> officeList) {
		if (CollectionUtils.isNotEmpty(officeList)) {
			engagementOfficeRepository.deleteByEngagementId(engagement.getEngagementId());
			List<CustomerLocations> customerLocations = customerLocationsRepository
					.findCustomerLocationsByCustomerId(engagement
							.getCustomerOrganizationId());
			List<EngagementOffice> engmtOffices = new ArrayList<>();
			for (OfficeLocationDTO officeDTO : officeList) {
				EngagementOffice engmtOffice = new EngagementOffice();
				engmtOffice.setEngagementId(engagement.getEngagementId());
				engmtOffice.setOfficeId(officeDTO.getOfficeId());
				customerLocations.forEach(customerLocation -> {
					if(customerLocation.getOfficeId().equals(officeDTO.getOfficeId())) {
//						removeEngagementOfficeLocationByEngagementId
						engmtOffice.setCustomerLocationId(customerLocation.getCustomerLocationId());
					}
				});
				engmtOffice.setActiveFlag(EngagementOffice.ActiveFlagEnum.Y);
				engmtOffices.add(engmtOffice);
			}
			engagementOfficeRepository.save(engmtOffices);
		}
	}

	private void chkEngmtName(Engagement engagement) {
		if (engagement.getEngagementId() != null) {
			checkByEngagementId(engagement);
		} else {
			checkByEngagementName(engagement);
		}
	}

	private void checkByEngagementName(Engagement engagement) {
		Engagement engagements = engagementRepository.checkExistByEngagementName(engagement.getEngagementName(),engagement.getCustomerOrganizationId());
		if (Objects.nonNull(engagements)) {
			throw new EngagementException(ENGAGEMENT_NAME_EXISTS);
		}
	}

	private void checkByEngagementId(Engagement engagement) {
		List<String> engagementNames = engagementRepository.checkExistByEngagementId(engagement.getEngagementId(),engagement.getCustomerOrganizationId());
		if (engagementNames.contains(engagement.getEngagementName())) {
			throw new EngagementException(ENGAGEMENT_NAME_EXISTS);
		}
	}

	@Override
	public EngagementDTO getEngagementDetails(UUID engagementId) {
		Engagement engagement = engagementRepository.findOne(engagementId);
		if (Objects.isNull(engagement)) {
			throw new EngagementException(CUSTOMER_DATA_IS_NOT_AVAILABLE);
		}
		return EngagementMapper.INSTANCE.engagementToEngagementDTO(engagement);
	}

	@Override
	public List<OfficeLocationDTO> getConfiguredOfficeLocations(UUID engagementId) {
		List<Long> configuredOfficeLocationIds = new ArrayList<>();
		List<OfficeLocationDTO> officeLocationDTOs = new ArrayList<>();

		if (null != engagementId) {
			Engagement engagement = engagementRepository.findOne(engagementId);
			List<Long> userGroupOfficeLocationIds = engagementRepository.findConfiguredOfficeIds(engagementId);
			if (Objects.nonNull(engagement)) {
				List<EngagementOffice> configuredOfficeLocations = engagementRepository.findOfficeIds(engagementId);
				configuredOfficeLocations.forEach(configLocation -> {
					configuredOfficeLocationIds.add(configLocation.getOfficeId());
					userGroupOfficeLocationIds.remove(configLocation.getOfficeId());
				});
				officeLocationDTOs = getOfficeLocationsWithAll(userGroupOfficeLocationIds);
				officeLocationDTOs.forEach(officeLocationDTO -> {
					if (configuredOfficeLocationIds.contains((Long) officeLocationDTO.getOfficeId())) {
						officeLocationDTO.setIsConfigured(ACTIVE_CONFIGURED);
					}
				});
			}
		} else {
			officeLocationDTOs = new OfficeLocationCommand(
					restTemplate, DiscoveryClientAndAccessTokenUtil
							.discoveryClient(EngagementConstants.COMMON_GROUP_KEY, discoveryClient),
					DiscoveryClientAndAccessTokenUtil.getAccessToken()).getAllOfficeLocation();
		}
		return officeLocationDTOs;
	}

	private List<OfficeLocationDTO> getOfficeLocationsWithAll(List<Long> configuredOfficeLocationIds) {
		List<OfficeLocationDTO> officeLocationDTOs = new OfficeLocationCommand(restTemplate,
				DiscoveryClientAndAccessTokenUtil.discoveryClient(EngagementConstants.COMMON_GROUP_KEY,
						discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken()).getAllOfficeLocation();
		List<OfficeLocationDTO> returnOfficeLocationDTOs = new ArrayList<>();

		officeLocationDTOs.forEach(officeLocationDTO -> {
			if (configuredOfficeLocationIds.contains((Long) officeLocationDTO.getOfficeId())) {
				officeLocationDTO.setIsConfigured(ACTIVE_CONFIGURED);
			}
		});
		returnOfficeLocationDTOs.addAll(officeLocationDTOs);
		return returnOfficeLocationDTOs;
	}

	public EmployeeProfileDTO getLoggedInUser() {
		EmployeeRestTemplate employeeRestTemplate = new EmployeeRestTemplate(restTemplate,
				DiscoveryClientAndAccessTokenUtil.discoveryClient(EngagementConstants.COMMON_GROUP_KEY,
						discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		EmployeeProfileDTO employeeProfileDTO = employeeRestTemplate.getEmployeeProfileDTO();
		if (Objects.nonNull(employeeProfileDTO)) {
			if (Objects.isNull(employeeProfileDTO.getEmployeeId())) {
				throw new EngagementException(EMPLOYEE_ID_IS_REQUIRED);
			}
		} else {
			throw new EngagementException(EMPLOYEE_DATA_IS_AVAILABLE);
		}
		return employeeProfileDTO;
	}
	
	@Override
	public List<EngagementDTO> getEngagements() {
		EmployeeProfileDTO employee = getLoggedInUser();
		Long loggedUserId = null;
		if (Objects.nonNull(employee)) {
			loggedUserId = employee.getEmployeeId();
		}
		List<Engagement> engagements = null;
		if (null != loggedUserId) {
			engagements = engagementRepository.findByEmployeeId(loggedUserId);
		}
		return EngagementMapper.INSTANCE.engagementListToengagementListDTO(engagements);
	}
	
	@Override
	public Page<EngagementHolidaysView> getEngagementHolidayList(Pageable pageable) {
		Pageable pageableRequest = pageable;
		Page<EngagementHolidaysView> engagementList = engagementHolidayRepository.getEngagementHolidays(pageableRequest);
		return new PageImpl<>(engagementList.getContent(), pageableRequest, engagementList.getTotalElements());
	}
	
	@Override
	public HolidayResourceDTO createEngagementHoliday(HolidayResourceDTO engagementHolidays) throws ParseException {
		EmployeeProfileDTO employee = getLoggedInUser();
		Long loggedUserId = null;
		if (Objects.nonNull(employee)) {
			loggedUserId = employee.getEmployeeId();
		}
		if(Objects.isNull(engagementHolidays.getEngagementHolidayId()) && Objects.nonNull(engagementHolidays.getEngagementId()) && Objects.nonNull(engagementHolidays.getHolidayDate())){
			EngagementHolidaysView engagement = engagementHolidayRepository.checkEngagementHoliday(engagementHolidays.getEngagementId(),DateUtil.checkconvertStringToISODate(engagementHolidays.getHolidayDate()));
			if(Objects.nonNull(engagement)){
				throw new EngagementException(EMGAGEMENT_HOLIDAY);
			}
		}
		EngagementHolidays engagementHoliday=new EngagementHolidays();
		if(Objects.nonNull(engagementHolidays.getEngagementHolidayId())){
			EngagementHolidaysView engagement = engagementHolidayRepository.checkUpdateEngagementHoliday(
					engagementHolidays.getEngagementId(),DateUtil.checkconvertStringToISODate(engagementHolidays.getHolidayDate()),engagementHolidays.getEngagementHolidayId());
			if(Objects.nonNull(engagement)){
				throw new EngagementException(EMGAGEMENT_HOLIDAY);
			}
			engagementHoliday.setEngagementHolidayId(engagementHolidays.getEngagementHolidayId());
		}
		
		Map<String, Object> days = getDays(engagementHolidays.getEngagementId());
		Date effectiveStartDate = (Date) days.get(EngagementServiceImpl.EFFECTIVE_START_DATE);
		Date effectiveEndDate = (Date) days.get(EngagementServiceImpl.EFFECTIVE_END_DATE);
		if (DateUtil.checkconvertStringToISODate(engagementHolidays.getHolidayDate()).before(effectiveStartDate) || DateUtil.checkconvertStringToISODate(engagementHolidays.getHolidayDate()).after(effectiveEndDate)) {
			throw new EngagementException(EngagementServiceImpl.INVALID_DATE_FOR_THIS_ENGAGEMENT
					+" "
					+ DateUtil.parseWordFromDateToString(effectiveStartDate)
					+" - "+ DateUtil.parseWordFromDateToString(effectiveEndDate));
		}
		
		engagementHoliday.setEngagementId(engagementHolidays.getEngagementId());
		engagementHoliday.setHolidayDate(DateUtil.checkconvertStringToISODate(engagementHolidays.getHolidayDate()));
		engagementHoliday.setHolidayDescription(engagementHolidays.getHolidayDescription().trim());
		engagementHoliday.setCreatedBy(loggedUserId);
		engagementHoliday.setLastModifiedBy(loggedUserId);
		EngagementHolidays engagementData = engagementHolidayRepository.save(engagementHoliday);
		
		return EngagementMapper.INSTANCE.engagementHolidaysToHolidayResourceDTO(engagementData);
	}
	
	
	private Map<String, Object> getDays(String engagementId) {
		/*ContractorEmployeeEngagementView contractorEmployeeEngagementView = getEmployeeEngagementDetails(userId,
				engagementId);*/
		Engagement engagement = engagementRepository.findOne(UUID.fromString(engagementId));
		Map<String, Object> mapKey = new HashMap<>();
		if (Objects.nonNull(engagement)) {
			Date effectiveStartDate = engagement.getEngmtStartDate();
			Date effectiveEndDate = engagement.getEngmtEndDate();
			mapKey.put(EngagementServiceImpl.EFFECTIVE_START_DATE, effectiveStartDate);
			mapKey.put(EngagementServiceImpl.EFFECTIVE_END_DATE, effectiveEndDate);
			return mapKey;
		} else {
			throw new EngagementException("Contractor Data is not Avaliable");
		}
	}
	
	@Override
	public HolidayResourceDTO getEngagementHolidayDetails(UUID engagementHolidayId) {
		EngagementHolidaysView engagement = engagementHolidayRepository.getEngagementHoliday(engagementHolidayId);
		if (Objects.isNull(engagement)) {
			throw new EngagementException(CUSTOMER_DATA_IS_NOT_AVAILABLE);
		}
		return EngagementMapper.INSTANCE.engagementHolidaysViewEditToHolidayResourceDTO(engagement);
	}

    @Override
    @Transactional
    public int updateEngagement(BigDecimal initialAmount, BigDecimal totalAmount,
            BigDecimal balanceAmount, UUID engagementId, UUID poId, String poNumber, String type,
            String engmntDate,String poIssueDate) throws ParseException {
        int updateValue = 0;
        Date engagementEndDate = CommonUtils.convertStringDateToUtilDateInDefaultFormat(engmntDate);
        Date issueDate=CommonUtils.convertStringDateToUtilDateInDefaultFormat(poIssueDate);
        if (type.equals(REVENUE)) {
            updateValue = engagementRepository.updateEngagementForRevenuePO(initialAmount,
                    totalAmount, balanceAmount, engagementId, poId, poNumber, engagementEndDate,issueDate);
        } else if (type.equals(EXPENSE)) {
            updateValue = engagementRepository.updateEngagementForExpensePO(initialAmount,
                    totalAmount, balanceAmount, engagementId, poId, poNumber, engagementEndDate,issueDate);
        }
        Engagement engagement = engagementRepository.findOne(engagementId);
        if (null != engagement) {
            updateValue = customerRepository.updateCustomerDate(engagementEndDate,
                    engagement.getCustomerOrganizationId());
        }
        return updateValue;
    }

    @Override
	public List<ReportingMgrEngmtViewDTO> getReportMgrengagements(String activeFlag) {
		EmployeeProfileDTO employee = getLoggedInUser();
		Long loggedUserId = null;
		if (Objects.nonNull(employee)) {
			loggedUserId = employee.getEmployeeId();
		}
		List<ReportingMgrEngmtView> contractorUsersDetailView= contractorRepository.findEngmtByEmployeeId(loggedUserId,activeFlag);
		return ReportingMgrEngmtViewMapper.INSTANCE.contractorUsersDetailViewDTOToContractorUsersDetailView(contractorUsersDetailView);
	}
    
    @Override
	public void updateFileDetails(
			String sourceReferenceId, List<InvoiceAttachmentsDTO> fileDetails) {
		BasicDBObject query = new BasicDBObject();
		GridFS gridFS = new GridFS(mongoTemplate.getDb(),
				InvoiceConstants.INVOICE);
		fileDetails.forEach(fileDetail -> {
			query.put(InvoiceConstants.INVOICE_ATTACHMENT_ID,
					fileDetail.getInvoiceAttachmentId());
			GridFSDBFile file = gridFS.findOne(query);
			file.put(InvoiceConstants.SRC_REF_ID, sourceReferenceId);
			file.save();
		});
	}
    
}