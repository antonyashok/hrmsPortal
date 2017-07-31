package com.tm.common.employee.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.employee.service.dto.EmployeeProfileAttachmentDTO;
import com.tm.common.engagement.domain.EmployeeProfileAttachment;

@Mapper
public interface EmployeeProfileAttachmentMapper {
	
	EmployeeProfileAttachmentMapper INSTANCE = Mappers.getMapper(EmployeeProfileAttachmentMapper.class);
	
	EmployeeProfileAttachmentDTO employeeProfileAttachmentToEmployeeProfileAttachmentDTO(
			EmployeeProfileAttachment employeeProfileAttachment);

	List<EmployeeProfileAttachmentDTO> employeeProfileAttachmentsToEmployeeProfileAttachmentDTOs(
			List<EmployeeProfileAttachment> employeeProfileAttachment);

}
