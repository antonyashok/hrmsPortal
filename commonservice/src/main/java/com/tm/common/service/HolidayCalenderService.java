package com.tm.common.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.common.service.dto.HolidayCalendarDTO;
import com.tm.common.service.dto.HolidaySettingsViewDTO;

public interface HolidayCalenderService {

  HolidayCalendarDTO saveHolidayCalender(HolidayCalendarDTO holidayCalendarDTO);

  HolidayCalendarDTO getHolidayCalendarDTO(UUID holidayCalendarId);

  void deleteHolidayCalenderDetail(UUID holidayCalendarDetailId);

  void deleteHolidayCalenderDetail(List<UUID> holidayCalendarDetailIds);

  Page<HolidaySettingsViewDTO> getAllHolidaySettings(Pageable pageable);

}
