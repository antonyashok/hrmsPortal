package com.tm.common.holiday.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.holiday.domain.Holiday;
import com.tm.common.holiday.service.dto.HolidayDTO;

@Mapper
public interface HolidayMapper {

	HolidayMapper INSTANCE = Mappers.getMapper(HolidayMapper.class);

	HolidayDTO holidayToHolidayDTO(Holiday holiday);

	List<HolidayDTO> holidaysToHolidayDTOs(List<Holiday> holidays);
}
