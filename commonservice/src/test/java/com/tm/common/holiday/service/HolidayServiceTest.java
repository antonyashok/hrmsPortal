package com.tm.common.holiday.service;

import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.tm.common.holiday.domain.Holiday;
import com.tm.common.holiday.exception.InvalidDateRangeException;
import com.tm.common.holiday.repository.HolidayRepository;
import com.tm.common.holiday.service.impl.HolidayServiceImpl;
import com.tm.commonapi.web.rest.util.CommonUtils;

public class HolidayServiceTest {

	@InjectMocks
	HolidayServiceImpl holidayServiceImpl;
	
	@Mock
	private HolidayRepository holidayRepository;
	
	@BeforeTest
	public void setUp() throws Exception {
		this.holidayRepository = mock(HolidayRepository.class);
		holidayServiceImpl = new HolidayServiceImpl(holidayRepository);
	}
	
	@Test (expectedExceptions = {InvalidDateRangeException.class})
	public void testGetHolidays() throws ParseException {

		Long stateProvinceId = 1L;
		String startDate = "05/13/2017";
		String endDate = "05/20/2017";
		Date convertedStartDate = CommonUtils.convertStringToDate(startDate);
		Date convertedEndDate = CommonUtils.convertStringToDate(endDate);
		List<Holiday> holidays = new ArrayList<>();
		Holiday holiday = Mockito.mock(Holiday.class);
		holidays.add(holiday);
		when(holidayRepository.findHolidayByStateProvinceIdAndHolidayDateBetween(stateProvinceId, convertedStartDate, convertedEndDate)).thenReturn(holidays);
		AssertJUnit.assertNotNull(holidayServiceImpl.getHolidays(stateProvinceId, startDate, endDate));
		
		when(holidayRepository.findHolidayByStateProvinceIdOrderByHolidayDate(stateProvinceId)).thenReturn(null);
		AssertJUnit.assertNull(holidayServiceImpl.getHolidays(stateProvinceId, null, null));
		
		AssertJUnit.assertNull(holidayServiceImpl.getHolidays(stateProvinceId, startDate, null));
		
		holidayServiceImpl.getHolidays(stateProvinceId, endDate, startDate);
	}
}
