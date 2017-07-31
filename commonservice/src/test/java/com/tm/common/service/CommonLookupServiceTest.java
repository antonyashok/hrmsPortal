package com.tm.common.service;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.tm.common.domain.Country;
import com.tm.common.domain.StateProvince;
import com.tm.common.repository.CountryRepository;
import com.tm.common.repository.HolidayCalendarRepository;
import com.tm.common.repository.HolidayStateProvinceRepository;
import com.tm.common.repository.StateProvinceRepository;
import com.tm.common.service.impl.CommonLookupServiceImpl;

public class CommonLookupServiceTest {
	
	@InjectMocks
	CommonLookupServiceImpl commonLookupServiceImpl;
	
	@Mock
	private StateProvinceRepository stateProvinceRepository;
	
	@Mock
	private CountryRepository countryRepository;
	
	@Mock
	private HolidayCalendarRepository holidayCalendarRepository;
	
	@Mock
	private HolidayStateProvinceRepository holidayStateProvinceRepository;
	
	@BeforeTest
	public void setUp() {

		stateProvinceRepository = mock(StateProvinceRepository.class);
		countryRepository = mock(CountryRepository.class);
		holidayCalendarRepository = mock(HolidayCalendarRepository.class);
		holidayStateProvinceRepository = mock(HolidayStateProvinceRepository.class);
		commonLookupServiceImpl = new CommonLookupServiceImpl(stateProvinceRepository, countryRepository, holidayCalendarRepository, holidayStateProvinceRepository);
	}
	
	@Test
	public void testHolidaySettingsBasedCountries() {

		UUID holidayCalendarId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		List<Country> countries = new ArrayList<>();
		Country country = new Country();
		country.setCountryId(1);
		countries.add(country);
		when(countryRepository.findAll()).thenReturn(countries);
		
		when(holidayCalendarRepository.getCountryId(holidayCalendarId)).thenReturn(1);
		AssertJUnit.assertEquals(countries, commonLookupServiceImpl.holidaySettingsBasedCountries(holidayCalendarId));
		
		when(holidayCalendarRepository.getCountryId(holidayCalendarId)).thenReturn(2);
		AssertJUnit.assertEquals(countries, commonLookupServiceImpl.holidaySettingsBasedCountries(holidayCalendarId));
		
		when(countryRepository.findAll()).thenReturn(Arrays.asList());
		AssertJUnit.assertNotNull(commonLookupServiceImpl.holidaySettingsBasedCountries(holidayCalendarId));
		
		when(countryRepository.findAll()).thenReturn(countries);
		AssertJUnit.assertNotNull(commonLookupServiceImpl.holidaySettingsBasedCountries(null));
	}
	
	@Test
	public void testGetAllStateProvince() {

		long countryId = 100L;
		UUID holidayCalendarId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		StateProvince stateProvince = new StateProvince();
		stateProvince.setStateProvinceId(10L);
		stateProvince.setIsActiveFlg("N");
		List<StateProvince> stateProvinces = new ArrayList<StateProvince>();
		stateProvinces.add(stateProvince);
		when(stateProvinceRepository.findAllStateProvince(countryId)).thenReturn(stateProvinces);
		when(holidayStateProvinceRepository.stateProvinceIds(holidayCalendarId)).thenReturn(Arrays.asList(10L, 11L));
		AssertJUnit.assertNotNull(commonLookupServiceImpl.getAllStateProvince(countryId, holidayCalendarId));
		
		stateProvince.setIsActiveFlg("N");
		stateProvince.setStateProvinceId(20L);
		AssertJUnit.assertNotNull(commonLookupServiceImpl.getAllStateProvince(countryId, holidayCalendarId));
		
		AssertJUnit.assertNotNull(commonLookupServiceImpl.getAllStateProvince(countryId, null));
		
		when(stateProvinceRepository.findAllStateProvince(countryId)).thenReturn(null);
		AssertJUnit.assertNull(commonLookupServiceImpl.getAllStateProvince(countryId, holidayCalendarId));
	}
}
