package com.tm.common.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.domain.CompanyOfficeLocation;
import com.tm.common.service.dto.CompanyOfficeLocationDTO;

@Mapper
public interface CompanyOfficeLocationMapper {

	CompanyOfficeLocationMapper INSTANCE = Mappers.getMapper(CompanyOfficeLocationMapper.class);

	CompanyOfficeLocation companyOfficeDTOToCompanyOffice(CompanyOfficeLocationDTO companyOfficeDTO);

	CompanyOfficeLocationDTO companyOfficeTocompanyOfficeDTO(CompanyOfficeLocation companyOffice);

}
