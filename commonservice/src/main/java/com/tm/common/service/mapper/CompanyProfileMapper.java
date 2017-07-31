package com.tm.common.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.domain.CompanyProfile;
import com.tm.common.domain.CompanyProfileAttachment;
import com.tm.common.employee.service.dto.CompanyProfileAttachmentDTO;
import com.tm.common.service.dto.CompanyProfileDTO;

@Mapper
public interface CompanyProfileMapper {

	CompanyProfileMapper INSTANCE = Mappers.getMapper(CompanyProfileMapper.class);

	CompanyProfile companyProfileDTOToCompanyProfile(CompanyProfileDTO companyProfileDTO);

	CompanyProfileDTO companyProfileTocompanyProfileDTO(CompanyProfile companyProfile);

	CompanyProfileAttachmentDTO companyProfileAttachmentToCompanyProfileDTO(
			CompanyProfileAttachment companyProfileAttachment);

	List<CompanyProfileAttachmentDTO> companyProfileAttachmentsToCompanyProfileAttachmentDTOs(
			List<CompanyProfileAttachment> companyProfileAttachment);

}
