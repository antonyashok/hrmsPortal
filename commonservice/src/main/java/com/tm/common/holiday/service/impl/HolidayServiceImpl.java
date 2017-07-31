package com.tm.common.holiday.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.tm.common.holiday.domain.Holiday;
import com.tm.common.holiday.exception.InvalidDateRangeException;
import com.tm.common.holiday.repository.HolidayRepository;
import com.tm.common.holiday.service.HolidayService;
import com.tm.common.holiday.service.dto.HolidayDTO;
import com.tm.common.holiday.service.mapper.HolidayMapper;
import com.tm.commonapi.web.rest.util.CommonUtils;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@Service
public class HolidayServiceImpl implements HolidayService {

	private HolidayRepository holidayRepository;

	@Inject
	public HolidayServiceImpl(HolidayRepository holidayRepository) {
		this.holidayRepository = holidayRepository;
	}

	@Override
	public List<HolidayDTO> getHolidays(Long provinceId, String startDate,
			String endDate) throws ParseException {
		Date convertedStartDate;
		Date convertedEndDate;
		List<Holiday> holidays;
		if (StringUtils.isNotBlank(startDate)
				&& StringUtils.isNotBlank(endDate)) {
			convertedStartDate = CommonUtils.convertStringToDate(startDate);
			convertedEndDate = CommonUtils.convertStringToDate(endDate);
			if (!CommonUtils.isValidDateRange(convertedStartDate,
					convertedEndDate)) {
				throw new InvalidDateRangeException();
			}
			holidays = holidayRepository
					.findHolidayByStateProvinceIdAndHolidayDateBetween(
							provinceId, convertedStartDate, convertedEndDate);
		} else {
			holidays = holidayRepository
					.findHolidayByStateProvinceIdOrderByHolidayDate(provinceId);
		}
		if (CollectionUtils.isNotEmpty(holidays)) {
			return HolidayMapper.INSTANCE.holidaysToHolidayDTOs(holidays);
		}
		return null;
	}
}
