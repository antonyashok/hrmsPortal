package com.tm.common.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.domain.OfficeLocation;
import com.tm.common.service.dto.OfficeLocationDTO;

@Mapper
public interface OfficeLocationMapper {

	OfficeLocationMapper INSTANCE = Mappers.getMapper(OfficeLocationMapper.class);

	OfficeLocation officeLocationDTOToOfficeLocation(OfficeLocationDTO officeDTO);

	OfficeLocationDTO officeLocationToOfficeLocationDTO(OfficeLocation office);

	List<OfficeLocationDTO> listOfficeDTOtoOffice(List<OfficeLocation> officeList);

	List<OfficeLocation> listOfficetoOfficeDTO(List<OfficeLocationDTO> officeList);
}
