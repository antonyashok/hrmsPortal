package com.tm.common.employee.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.common.domain.EmployeeProfileView;
import com.tm.common.employee.service.dto.EmployeeProfileAttachmentDTO;
import com.tm.common.employee.service.dto.EmployeeProfileDTO;
import com.tm.common.engagement.domain.EmployeeProfile;
import com.tm.common.engagement.domain.EmployeeProfileAttachment;
import com.tm.common.service.dto.EmployeeProfileViewDTO;

@Mapper
public interface EmployeeProfileMapper {

	EmployeeProfileMapper INSTANCE = Mappers.getMapper(EmployeeProfileMapper.class);

	@Mappings(value = { @Mapping(source = "dob", target = "dob", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "dateJoin", target = "dateJoin", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "confirmDate", target = "confirmDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "passportExpiryDate", target = "passportExpiryDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "visaExpiryDate", target = "visaExpiryDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "workPermitExpiryDate", target = "workPermitExpiryDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "ptoAllotedHours", target = "ptoAllotedHours", numberFormat = "0.00") })
	EmployeeProfile employeeProfileDTOToEmployeeProfile(EmployeeProfileDTO employeeProfileDTO);

	@Mappings(value = { @Mapping(source = "dob", target = "dob", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "dateJoin", target = "dateJoin", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "confirmDate", target = "confirmDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "passportExpiryDate", target = "passportExpiryDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "visaExpiryDate", target = "visaExpiryDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(source = "workPermitExpiryDate", target = "workPermitExpiryDate", dateFormat = "MM/dd/yyyy"),
			@Mapping(target="employeeName", expression = "java(employeeProfile.getFirstName() + \" \" + employeeProfile.getLastName())")})
	EmployeeProfileDTO employeeProfileToEmployeeProfileDTO(EmployeeProfile employeeProfile);
	
	List<EmployeeProfileDTO> employeeProfileToEmployeeProfileDTO(List<EmployeeProfile> employeeProfile);

	EmployeeProfileAttachmentDTO employeeProfileAttachmentToEmployeeProfileAttachmentDTO(
			EmployeeProfileAttachment employeeProfileAttachment);

	List<EmployeeProfileAttachmentDTO> employeeProfileAttachmentsToEmployeeProfileAttachmentDTOs(
			List<EmployeeProfileAttachment> employeeProfileAttachment);

	EmployeeProfileViewDTO employeeProfileToemployeeProfileDTO(EmployeeProfileView employeeProfileView);

}
