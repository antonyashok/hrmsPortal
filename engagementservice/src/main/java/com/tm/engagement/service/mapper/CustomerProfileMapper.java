package com.tm.engagement.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.engagement.domain.CustomerProfile;
import com.tm.engagement.domain.CustomerProfileView;
import com.tm.engagement.service.dto.CustomerProfileDTO;
import com.tm.engagement.service.dto.CustomerProfileViewDTO;

@Mapper
public interface CustomerProfileMapper {

	CustomerProfileMapper INSTANCE = Mappers.getMapper(CustomerProfileMapper.class);

	@Mappings(value = { @Mapping(source = "customerId", target = "customerId"),
			@Mapping(source = "createdDate", target = "createdDate", dateFormat = "MMM dd, yyyy HH:mm:ss a"),
			@Mapping(source = "lastModifiedDate", target = "updatedDate", dateFormat = "MMM dd, yyyy HH:mm:ss a"),
			@Mapping(source = "effectiveStartDate", target = "effectiveStartDate", dateFormat = "MMM dd, yyyy"),
			@Mapping(source = "effectiveEndDate", target = "effectiveEndDate", dateFormat = "MMM dd, yyyy") })
	CustomerProfileDTO customerToCustomerProfileDTO(CustomerProfile customerProfile);

	@Mappings(value = { @Mapping(source = "customerId", target = "customerId"),
			@Mapping(source = "effectiveStartDate", target = "effectiveStartDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "effectiveEndDate", target = "effectiveEndDate", dateFormat = "MM/dd/yyyy") })
	CustomerProfile customerProfileDTOToCustomer(CustomerProfileDTO customerProfileDTO);

	@Mappings(value = { @Mapping(source = "customerId", target = "customerId"),
			@Mapping(source = "effectiveStartDate", target = "effectiveStartDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "effectiveEndDate", target = "effectiveEndDate", dateFormat = "MM/dd/yyyy") })
	CustomerProfileDTO customerToCustomerEditDTO(CustomerProfile customerProfile);

	CustomerProfileViewDTO customerViewToCustomerViewDTO(CustomerProfileView customerProfileView);

}