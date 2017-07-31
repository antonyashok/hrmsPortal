package com.tm.common.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.tm.common.domain.HolidayCalendar;
import com.tm.common.domain.HolidayCalendarDetail;
import com.tm.common.domain.HolidaySettingsView;
import com.tm.common.domain.HolidayStateProvince;
import com.tm.common.domain.StateProvince;
import com.tm.common.repository.HolidayCalendarDetailRepository;
import com.tm.common.repository.HolidayCalendarRepository;
import com.tm.common.repository.HolidaySettingsViewRepository;
import com.tm.common.repository.StateProvinceRepository;
import com.tm.common.service.dto.HolidayCalendarDTO;
import com.tm.common.service.dto.HolidayCalendarDetailDTO;
import com.tm.common.service.dto.HolidayStateProvinceDTO;
import com.tm.common.service.impl.HolidayCalenderServiceImpl;

public class HolidayCalenderServiceTest {
	
	@InjectMocks
	HolidayCalenderServiceImpl holidayCalenderServiceImpl;

	@Mock
	private HolidayCalendarRepository holidayCalendarRepository;

	@Mock
	private HolidayCalendarDetailRepository holidayCalanderDetailRepository;

	@Mock
	private HolidaySettingsViewRepository holidaySettingsViewRepository;
	
	@Mock
	private StateProvinceRepository provinceRepository;
	
	@BeforeTest
	public void setUp() {
		
		holidayCalendarRepository = mock(HolidayCalendarRepository.class);
		holidayCalanderDetailRepository = mock(HolidayCalendarDetailRepository.class);
		holidaySettingsViewRepository = mock(HolidaySettingsViewRepository.class);
		provinceRepository = mock(StateProvinceRepository.class);
		holidayCalenderServiceImpl = new HolidayCalenderServiceImpl(holidayCalendarRepository, holidayCalanderDetailRepository, holidaySettingsViewRepository, provinceRepository);
	}

	@Test
	public void testDeleteHolidayCalenderDetail() {
		holidayCalenderServiceImpl.deleteHolidayCalenderDetail(UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00"));
	}
	
	@Test
	public void testDeleteHolidayCalenderDetailList() {
		
		List<UUID> list = mock(List.class);
		holidayCalenderServiceImpl.deleteHolidayCalenderDetail(list);
	}
	
	@Test
	public void testGetAllHolidaySettings() {

		Pageable pageable = mock(Pageable.class);
		Sort sort = mock(Sort.class);
		Page<HolidaySettingsView> holidaySettingsViewPage = mock(Page.class);
		List<HolidaySettingsView> holidaySettingsViews = new ArrayList<>();
		HolidaySettingsView holidaySettingsView = new HolidaySettingsView();
		holidaySettingsView.setCountryId(22L);
		holidaySettingsView.setCountryName("TestCountryName");
		holidaySettingsViews.add(holidaySettingsView);
		when(pageable.getSort()).thenReturn(sort);
		when(holidaySettingsViewRepository.findAll(pageable)).thenReturn(holidaySettingsViewPage);
		when(holidaySettingsViewPage.getContent()).thenReturn(holidaySettingsViews);
		AssertJUnit.assertNotNull(holidayCalenderServiceImpl.getAllHolidaySettings(pageable));
		
		when(holidaySettingsViewPage.getContent()).thenReturn(null);
		AssertJUnit.assertNotNull(holidayCalenderServiceImpl.getAllHolidaySettings(pageable));
		
		when(holidaySettingsViewRepository.findAll((Pageable)anyObject())).thenReturn(holidaySettingsViewPage);
		when(holidaySettingsViewPage.getContent()).thenReturn(holidaySettingsViews);
		when(pageable.getSort()).thenReturn(null);
		when(pageable.getPageSize()).thenReturn(2);
		AssertJUnit.assertNotNull(holidayCalenderServiceImpl.getAllHolidaySettings(pageable));
	}
	
	@Test
	public void testGetHolidayCalendarDTO() {
		
		UUID holidayCalendarId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		HolidayCalendar holidayCalendar = mock(HolidayCalendar.class);
		HolidayCalendarDetail holidayCalendarDetail = mock(HolidayCalendarDetail.class);
		List<HolidayCalendarDetail> calendarDetails = Arrays.asList(holidayCalendarDetail);
		HolidayStateProvince holidayStateProvince = mock(HolidayStateProvince.class);
		List<HolidayStateProvince> holidayStateProvinces = Arrays.asList(holidayStateProvince);
		when(holidayCalendarRepository.findOne(holidayCalendarId)).thenReturn(holidayCalendar);
		when(holidayCalendar.getHolidayCalendarDetail()).thenReturn(calendarDetails);
		when(holidayCalendar.getHolidayStateProvince()).thenReturn(holidayStateProvinces);
		when(holidayCalendarDetail.getHolidayCalendarDetailId()).thenReturn(holidayCalendarId);
		when(holidayCalendarDetail.getHolidayDate()).thenReturn(new Date("05/03/2017"));
		when(holidayCalendarDetail.getHolidayname()).thenReturn("TestHolidayName");
		AssertJUnit.assertNotNull(holidayCalenderServiceImpl.getHolidayCalendarDTO(holidayCalendarId));
		
		when(holidayCalendarDetail.getHolidayDate()).thenReturn(new Date("05/03/2018"));
		when(holidayCalendarDetail.getHolidayname()).thenReturn("TestHolidayName");
		AssertJUnit.assertNotNull(holidayCalenderServiceImpl.getHolidayCalendarDTO(holidayCalendarId));
	}
	
	@Test
	public void testSaveHolidayCalender() {
		
		UUID holidayCalendarId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		UUID holidayCalendarDetailId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff11");
		UUID holidayStateProvinceId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff22");
		
		UUID holidayDetailId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff22"); 
		
		HolidayCalendarDTO holidayCalendarDTO = mock(HolidayCalendarDTO.class);
		HolidayCalendarDetailDTO holidayCalendarDetailDTO = mock(HolidayCalendarDetailDTO.class);
		List<HolidayCalendarDetailDTO> calendarDetailDTOs = Arrays.asList(holidayCalendarDetailDTO);
		HolidayStateProvinceDTO holidayStateProvinceDTO = mock(HolidayStateProvinceDTO.class);
		List<HolidayStateProvinceDTO> holidayStateProvinceDTOs = Arrays.asList(holidayStateProvinceDTO);
		
		when(holidayCalendarDTO.getCountryId()).thenReturn(100L);
		when(holidayCalendarDTO.getSettingName()).thenReturn("TestSetting");
		when(holidayCalendarDTO.getHolidayCalendarId()).thenReturn(holidayCalendarId);
		when(holidayCalendarDTO.getLastModifiedBy()).thenReturn(1L);
		
		when(holidayCalendarDTO.getHolidayCalendarDetailDTO()).thenReturn(calendarDetailDTOs);
		when(holidayCalendarDTO.getHolidayStateProvinceDTO()).thenReturn(holidayStateProvinceDTOs);
		
		when(holidayCalendarDetailDTO.getHolidayCalendarDetailId()).thenReturn(holidayCalendarId);
		when(holidayCalendarDetailDTO.getHolidayDate()).thenReturn("05/03/2017");
		when(holidayCalendarDetailDTO.getHolidayname()).thenReturn("TestHolidayName");
		when(holidayCalendarDetailDTO.getActiveFlag()).thenReturn(true);
		when(holidayCalendarDetailDTO.getHolidayCalendarDetailId()).thenReturn(holidayCalendarDetailId);
		when(holidayCalendarDetailDTO.getHolidayEndDate()).thenReturn("05/13/2017");
		when(holidayCalendarDetailDTO.getLastModifiedBy()).thenReturn(1L);
		when(holidayCalendarDetailDTO.getHolidayDetailIds()).thenReturn(Arrays.asList(holidayDetailId));
		
		when(holidayStateProvinceDTO.getStateProvinceId()).thenReturn(2L);
		when(holidayStateProvinceDTO.getHolidayStateProvinceId()).thenReturn(holidayStateProvinceId);
		
		HolidayCalendar holidayCalendar = mock(HolidayCalendar.class);
		HolidayCalendarDetail holidayCalendarDetail = mock(HolidayCalendarDetail.class);
		List<HolidayCalendarDetail> calendarDetails = Arrays.asList(holidayCalendarDetail);
		HolidayStateProvince holidayStateProvince = mock(HolidayStateProvince.class);
		List<HolidayStateProvince> holidayStateProvinces = Arrays.asList(holidayStateProvince);
		
		when(holidayCalendar.getCountryId()).thenReturn(100L);
		when(holidayCalendar.getSettingName()).thenReturn("TestSetting");
		when(holidayCalendar.getHolidayCalendarId()).thenReturn(holidayCalendarId);
		when(holidayCalendar.getLastModifiedBy()).thenReturn(1L);
		
		when(holidayCalendar.getHolidayCalendarDetail()).thenReturn(calendarDetails);
		when(holidayCalendar.getHolidayStateProvince()).thenReturn(holidayStateProvinces);
		
		when(holidayCalendarDetail.getHolidayCalendarDetailId()).thenReturn(holidayCalendarId);
		when(holidayCalendarDetail.getHolidayDate()).thenReturn(new Date("05/03/2017"));
		when(holidayCalendarDetail.getHolidayname()).thenReturn("TestHolidayName");
		when(holidayCalendarDetail.getHolidayCalendarDetailId()).thenReturn(holidayCalendarDetailId);
		when(holidayCalendarDetail.getLastModifiedBy()).thenReturn(1L);
		
		when(holidayStateProvince.getStateProvinceId()).thenReturn(2L);
		when(holidayStateProvince.getHolidayStateProvinceId()).thenReturn(holidayStateProvinceId);
		
		when(holidayCalendarRepository.save((HolidayCalendar)Mockito.anyObject())).thenReturn(holidayCalendar);
		AssertJUnit.assertNotNull(holidayCalenderServiceImpl.saveHolidayCalender(holidayCalendarDTO));
		
		when(holidayStateProvinceDTO.getStateProvinceId()).thenReturn(0L);
		StateProvince stateProvince = mock(StateProvince.class);
		List<StateProvince> stateProvinces = Arrays.asList(stateProvince);
		when(stateProvince.getStateProvinceId()).thenReturn(5L);
		when(provinceRepository.findAllStateProvince(holidayCalendar.getCountryId())).thenReturn(stateProvinces);
		AssertJUnit.assertNotNull(holidayCalenderServiceImpl.saveHolidayCalender(holidayCalendarDTO));
	}
}
