package com.tm.engagement.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.tm.engagement.domain.Status;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;
import com.tm.engagement.configuration.service.hystrix.commands.EmployeeRestTemplate;
import com.tm.engagement.configuration.service.hystrix.commands.OfficeLocationCommand;
import com.tm.engagement.constants.EngagementConstants;
import com.tm.engagement.domain.CustomerLocations;
import com.tm.engagement.domain.CustomerProfile;
import com.tm.engagement.domain.CustomerProfile.ActiveFlagEnum;
import com.tm.engagement.domain.CustomerProfileView;
import com.tm.engagement.domain.EngagementOffice;
import com.tm.engagement.exception.CustomerProfileException;
import com.tm.engagement.exception.EngagementException;
import com.tm.engagement.repository.CustomerLocationsRepository;
import com.tm.engagement.repository.CustomerProfileRepository;
import com.tm.engagement.repository.CustomerProfileViewRepository;
import com.tm.engagement.repository.EngagementOfficeRepository;
import com.tm.engagement.service.CustomerProfileService;
import com.tm.engagement.service.dto.CompanyLocationDTO;
import com.tm.engagement.service.dto.CompanyProfileDTO;
import com.tm.engagement.service.dto.CustomerProfileDTO;
import com.tm.engagement.service.dto.CustomerProfileViewDTO;
import com.tm.engagement.service.dto.EmployeeProfileDTO;
import com.tm.engagement.service.mapper.CustomerProfileMapper;
import com.tm.engagement.web.rest.util.DateUtil;

@Service
@Transactional
public class CustomerProfileServiceImpl implements CustomerProfileService {

	private CustomerProfileRepository customerRepository;
	private CustomerProfileViewRepository customerViewRepository;
	private CustomerLocationsRepository customerLocationsRepository;
	
	private RestTemplate restTemplate;
	private DiscoveryClient discoveryClient;
	
	private EngagementOfficeRepository engagementOfficeRepository;

	private static final String CUSTOMER_DATA_IS_NOT_AVAILABLE = "Customer is not available";
	private static final String CUSTOMER_NUMBER_EXISTS = "Customer Number Already Exists";
	private static final String EMPLOYEE_ID_IS_REQUIRED = "Employee Id is required";
	private static final String EMPLOYEE_DATA_IS_AVAILABLE = "Employee Datum is not available";
	private static final String OFFICE_LOCATION_IS_REQUIRED = "OfficeLocation is required";

	private static final String START_END_CANNOT_BE_SAME_DATE = "Start Date and End Date can not be same date";
	
	@Inject
	public CustomerProfileServiceImpl(@NotNull final CustomerProfileRepository customerRepository,
			CustomerProfileViewRepository customerViewRepository,
			CustomerLocationsRepository customerLocationsRepository,
			EngagementOfficeRepository engagementOfficeRepository,
			@LoadBalanced final RestTemplate restTemplate,
			@Qualifier("discoveryClient") final DiscoveryClient discoveryClient) {
		this.customerRepository = customerRepository;
		this.customerViewRepository=customerViewRepository;
		this.customerLocationsRepository = customerLocationsRepository;
		this.engagementOfficeRepository = engagementOfficeRepository;
		this.restTemplate = restTemplate;
		this.discoveryClient = discoveryClient;
	}

	@Override
	public CustomerProfileDTO getCustomerDetails(Long customerId) {
		CustomerProfile customerProfile = customerRepository.findOne(customerId);
		if (Objects.isNull(customerProfile)) {
			throw new CustomerProfileException(CUSTOMER_DATA_IS_NOT_AVAILABLE);
		}
		List<Long> officeIds = customerLocationsRepository.getOfficeLocationsByCustomerId(customerId);
		if(CollectionUtils.isNotEmpty(officeIds)) {
			CompanyProfileDTO customerProfileDTO = new CompanyProfileDTO();
			customerProfileDTO.setOfficeIds(officeIds);
			List<EngagementOffice> engagementOfficeList = engagementOfficeRepository
					.getEngagementOfficeByOfficeId(customerProfileDTO
							.getOfficeIds());
			List<CompanyLocationDTO> companyLocationDTOs = getOfficeLocationByCustomerId(
					customerProfileDTO, engagementOfficeList);
			customerProfile.setCompanyLocationDTO(companyLocationDTOs);
		}
		
		return CustomerProfileMapper.INSTANCE.customerToCustomerEditDTO(customerProfile);
	}

	@Override
	public synchronized CustomerProfile createCustomer(CustomerProfileDTO customerProfileDTO) {
		CustomerProfile customerProfile = CustomerProfileMapper.INSTANCE
				.customerProfileDTOToCustomer(customerProfileDTO);
		
		EmployeeProfileDTO employee = getLoggedInUser();
		Long loggedUserId = null;
		if (Objects.nonNull(employee)) {
			loggedUserId = employee.getEmployeeId();
		}
		customerProfile.setCreatedBy(loggedUserId);
		customerProfile.setLastModifiedBy(loggedUserId);
		String startDateStr = customerProfileDTO.getEffectiveStartDate();
		String endDateStr = customerProfileDTO.getEffectiveEndDate();
		if(startDateStr.equalsIgnoreCase(endDateStr)){
			throw new CustomerProfileException(START_END_CANNOT_BE_SAME_DATE);
		}
		Date effectiveStartDate = DateUtil.checkconvertStringToISODate(startDateStr);
		Date effectiveEndDate = DateUtil.checkconvertStringToISODate(endDateStr);
		customerProfile.setEffectiveStartDate(effectiveStartDate);
		customerProfile.setEffectiveEndDate(effectiveEndDate);
		customerProfile.setCustomerName(customerProfileDTO.getCustomerName().trim());

		if (customerProfile.getCustomerId() != null) {
			checkNumberBycustomerId(customerProfile);
		} else {
			checkNumberByCustomerName(customerProfile);
		}
		
		customerProfile = customerRepository.saveAndFlush(customerProfile);
		CompanyProfileDTO companyProfileDTO = populateCompanyProfileDTO(customerProfileDTO);
		companyProfileDTO.setServiceFor("ENGAGEMENT");
		companyProfileDTO.setCompanyProfileId(customerProfile.getCustomerId());
		List<CompanyLocationDTO> companyLocationDTO = saveOfficeLocationForCustomer(companyProfileDTO);
		saveCustomerLocation(customerProfile, companyLocationDTO);
		customerProfile.setCompanyLocationDTO(companyLocationDTO);
		return customerRepository.save(customerProfile);
	}

	private void saveCustomerLocation(CustomerProfile customerProfile,
			List<CompanyLocationDTO> companyLocationDTOs) {
		List<CustomerLocations> customerLocationsInDB = customerLocationsRepository.findCustomerLocationsByCustomerId(customerProfile.getCustomerId());
		List<Long> officeIds = new ArrayList<>();
		customerLocationsInDB.forEach(customerLocationInDB -> officeIds.add(customerLocationInDB.getOfficeId()));
		
		List<CustomerLocations> customerLocations = new ArrayList<>();
		for(CompanyLocationDTO companyLocationDTO:companyLocationDTOs) {
			if(CollectionUtils.isNotEmpty(customerLocationsInDB)) {				
				if(!officeIds.contains(companyLocationDTO.getOfficeId())) {
					populateCustomerLocationMapping(customerProfile, customerLocations, companyLocationDTO);
				}
			} else {
				populateCustomerLocationMapping(customerProfile, customerLocations, companyLocationDTO);
			}
		}
		
			
		customerLocationsRepository.save(customerLocations);
	}

	private void populateCustomerLocationMapping(CustomerProfile customerProfile,
			List<CustomerLocations> customerLocations, CompanyLocationDTO companyLocationDTO) {
		CustomerLocations customerLocation = new CustomerLocations();
		customerLocation.setOfficeId(companyLocationDTO.getOfficeId());
		customerLocation.setCustomerId(customerProfile.getCustomerId());
		customerLocations.add(customerLocation);
	}

	private CompanyProfileDTO populateCompanyProfileDTO(
			CustomerProfileDTO customerProfileDTO) {
		CompanyProfileDTO companyProfileDTO = new CompanyProfileDTO();
		companyProfileDTO.setCompanyLocationDTO(customerProfileDTO.getCompanyLocationDTO());		
		return companyProfileDTO;
	}

	private void checkNumberByCustomerName(CustomerProfile customerProfile) {
		CustomerProfile custProfile = customerRepository
				.checkExistByCustomerNumber(customerProfile.getCustomerNumber());
		if (Objects.nonNull(custProfile)) {
			throw new CustomerProfileException(CUSTOMER_NUMBER_EXISTS);
		}
	}

	private void checkNumberBycustomerId(CustomerProfile customerProfile) {
		List<String> custNumber = customerRepository.checkExistByCustomerId(customerProfile.getCustomerId());
		if (custNumber.contains(customerProfile.getCustomerNumber())) {
			throw new CustomerProfileException(CUSTOMER_NUMBER_EXISTS);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public Page<CustomerProfileViewDTO> getCustomerProfileList(Pageable pageable, String searchParam,
			String activeFlag) {
		ActiveFlagEnum active = ActiveFlagEnum.Y;
		Pageable pageableRequest = pageable;
		if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
			pageableRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.Direction.ASC, "updatedDate");
        }
		
		Page<CustomerProfileView> customerProfileList;
		if (null != activeFlag) {
			customerProfileList = customerViewRepository.getAllCustomerDetailByActive(active, searchParam, pageableRequest);
		} else {
			customerProfileList = customerViewRepository.findAll(pageableRequest);
		}
		List<CustomerProfileViewDTO> result = new ArrayList<>();
		if (Objects.nonNull(customerProfileList)) {
			if (CollectionUtils.isNotEmpty(customerProfileList.getContent())) {
				customerProfileList.forEach(customerProfile -> result.add(mapCustomerToCustomerDTO(customerProfile)));
			}
			return new PageImpl<>(result, pageableRequest, customerProfileList.getTotalElements());
		}
		return null;
	}

	private synchronized CustomerProfileViewDTO mapCustomerToCustomerDTO(CustomerProfileView customerProfileView) {
		return CustomerProfileMapper.INSTANCE.customerViewToCustomerViewDTO(customerProfileView);
	}

	@Override
	public List<CustomerProfile> getActiveCustomersList() {
		return customerRepository.getActiveCustomerProfile();
	}

	public List<CompanyLocationDTO> saveOfficeLocationForCustomer(CompanyProfileDTO companyProfileDTO) {
		OfficeLocationCommand officeLocationCommand = new OfficeLocationCommand(restTemplate,
				DiscoveryClientAndAccessTokenUtil.discoveryClient(EngagementConstants.COMMON_GROUP_KEY,
						discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		List<CompanyLocationDTO> companyLocationDTOs = officeLocationCommand.saveOfficeLocation(companyProfileDTO);
		if (Objects.nonNull(companyLocationDTOs) && CollectionUtils.isNotEmpty(companyLocationDTOs) &&  
			Objects.isNull(companyLocationDTOs.get(0))) {
				throw new EngagementException(OFFICE_LOCATION_IS_REQUIRED);
		}
		return companyLocationDTOs;
	}
	
	public Map<String, Object> getOfficeLocationForCustomer(String companyId) {
		OfficeLocationCommand officeLocationCommand = new OfficeLocationCommand(restTemplate,
				DiscoveryClientAndAccessTokenUtil.discoveryClient(EngagementConstants.COMMON_GROUP_KEY,
						discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		Map<String, Object> companyProfileDTO = officeLocationCommand.getCompanyProfile(companyId);
		if (Objects.nonNull(companyProfileDTO) && 
			Objects.isNull(companyProfileDTO.get("companyLocationDTO"))) {
				throw new EngagementException(OFFICE_LOCATION_IS_REQUIRED);
		}
		return companyProfileDTO;
	}	
	
	public List<CompanyLocationDTO> getOfficeLocationByCustomerId(CompanyProfileDTO customerProfileDTO,
			List<EngagementOffice> engagementOfficeList) {
		OfficeLocationCommand officeLocationCommand = new OfficeLocationCommand(restTemplate,
				DiscoveryClientAndAccessTokenUtil.discoveryClient(EngagementConstants.COMMON_GROUP_KEY,
						discoveryClient),
				DiscoveryClientAndAccessTokenUtil.getAccessToken());
		List<CompanyLocationDTO> companyProfileDTO = officeLocationCommand.getOfficeLocationByCustomerId(customerProfileDTO);
		List<Long> officeIds = new ArrayList<>();
		companyProfileDTO.forEach(companyLocationDTO -> 
			engagementOfficeList.forEach(engagementOffice -> {
				if(engagementOffice.getOfficeId().equals(companyLocationDTO.getOfficeId())  && 
						!officeIds.contains(companyLocationDTO.getOfficeId())) {
					officeIds.add(companyLocationDTO.getOfficeId());
					companyLocationDTO.setIsDelete("N");
					companyLocationDTO.setIsActive("N");
				}
		}));		
		return companyProfileDTO;
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
	public Status deleteCustomerOfficeLocation(Long customerId,Long officeId){
		customerLocationsRepository.deleteCustomerOfficeLocation(customerId,officeId);
		return new Status("ok");
	}
}