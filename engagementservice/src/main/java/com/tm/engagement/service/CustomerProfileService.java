package com.tm.engagement.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.engagement.domain.CustomerProfile;
import com.tm.engagement.domain.Status;
import com.tm.engagement.service.dto.CustomerProfileDTO;
import com.tm.engagement.service.dto.CustomerProfileViewDTO;

public interface CustomerProfileService {

	Page<CustomerProfileViewDTO> getCustomerProfileList(Pageable pageable, String searchParam, String activeFlag);

	CustomerProfile createCustomer(CustomerProfileDTO customerProfile);

	CustomerProfileDTO getCustomerDetails(Long timeoffId);

	List<CustomerProfile> getActiveCustomersList();
	
	Status deleteCustomerOfficeLocation(Long customerId,Long officeId);

}