package com.tm.common.engagement.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.engagement.domain.CntrEngmt;
import com.tm.common.engagement.domain.CntrHolidays;
import com.tm.common.engagement.service.dto.ContractorHolidayDTO;
import com.tm.common.engagement.service.dto.EngagementDTO;
import com.tm.common.engagement.service.dto.TaskDTO;

@Mapper
public interface EngagementMapper {
	EngagementMapper INSTANCE = Mappers.getMapper(EngagementMapper.class);

	// employeeEngagementId
	EngagementDTO engagementToEngagementDTO(CntrEngmt engagement);

	ContractorHolidayDTO holidayToHolidayDTO(CntrHolidays cntrHolidays);

	TaskDTO engagementToTaskDTO(EngagementDTO engagement);
}
