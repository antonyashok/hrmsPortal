package com.tm.invoice.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.invoice.domain.EngagementContractors;
import com.tm.invoice.dto.EngagementContractorsDTO;

@Mapper
public interface EngagementMapper {

	EngagementMapper INSTANCE = Mappers.getMapper(EngagementMapper.class);

	EngagementContractorsDTO engagementContractorsToengagementContractorsDTO(
			EngagementContractors engagementContractors);

	List<EngagementContractorsDTO> engagementContractorsListToengagementContractorsDTOList(
			List<EngagementContractors> engagementContractorsList);

}
