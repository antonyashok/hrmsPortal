package com.tm.common.holiday.service;

import java.text.ParseException;
import java.util.List;

import com.tm.common.holiday.service.dto.HolidayDTO;

public interface HolidayService {

	public List<HolidayDTO> getHolidays(Long provinceId, String startDate,
			String endDate) throws ParseException;

}
