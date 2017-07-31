package com.tm.timesheetgeneration.reader;

import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tm.common.domain.EmployeeProfileView;
import com.tm.common.domain.EmployeeProfileView.EmployeeType;
import com.tm.common.repository.EmployeeProfileViewRepository;
import com.tm.timesheet.configuration.domain.EmployeeConfigSettingsView;
import com.tm.timesheet.configuration.repository.EmployeeConfigSettingsViewRepository;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.repository.TimesheetRepository;
import com.tm.util.DateUtil;

public class EmployeeReaderTest {
	
	@InjectMocks
	private EmployeeReader employeeReader;
	
	@Mock
	private EmployeeProfileViewRepository employeeProfileViewRepository;
	@Mock
	private TimesheetRepository timesheetRepository;
	@Mock
	private EmployeeConfigSettingsViewRepository employeeConfigSettingsViewRepository;
	
	
	@BeforeMethod
	private void setUp(){
		this.employeeProfileViewRepository = Mockito.mock(EmployeeProfileViewRepository.class);
		this.timesheetRepository = Mockito.mock(TimesheetRepository.class);
		this.employeeConfigSettingsViewRepository = Mockito.mock(EmployeeConfigSettingsViewRepository.class);
				
		this.employeeReader = new EmployeeReader();
		employeeReader.setTimesheetRepository(timesheetRepository);
		employeeReader.setEmployeeProfileViewRepository(employeeProfileViewRepository);
		employeeReader.setEmployeeConfigSettingsViewRepository(employeeConfigSettingsViewRepository);
		
	}
	
	@Test
	private void employeeReader() throws Exception{
		

		employeeReader.fromId = 0;
		employeeReader.toId = 10;
		employeeReader.weekEndDate = DateUtil.convertStringToISODate("05/20/2017");
		employeeReader.weekStartDate = DateUtil.convertStringToISODate("05/14/2017");
		employeeReader.weekStartDay = "Sun";
		employeeReader.weekEndDay = "Sat";
		employeeReader.startDayofWeek = DayOfWeek.SUNDAY;	
		
		Long employeeId = 1245L;
		List<Long> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		
		Pageable pageable = new PageRequest(0, 10);
		EmployeeProfileView employeeProfileView = new EmployeeProfileView();
		employeeProfileView.setConfigurationGroupId(UUID.randomUUID());
		employeeProfileView.setEmployeeId(employeeId);
		employeeProfileView.setOfficeId(78965l);
		
		List<EmployeeProfileView> employeeProfileviewList = new ArrayList<>();
		employeeProfileviewList.add(employeeProfileView);
		Page<EmployeeProfileView> employeeProfileList = new PageImpl<>(employeeProfileviewList, pageable, employeeProfileviewList.size());
		when(employeeProfileViewRepository.getByEmployeeTypeAndJoiningDateInBetweenApplicaitonStartDate(pageable, EmployeeType.E,
				DateUtil.parseLocalDateFormatWithDefaultTime(employeeReader.weekStartDate))).thenReturn(employeeProfileList);
		Timesheet timesheet = new Timesheet();
		timesheet.setId(UUID.randomUUID());
		timesheet.setTotalHours(12.00);
		List<Timesheet> timesheetList = new ArrayList<>();
		timesheetList.add(timesheet);
		
		when(timesheetRepository.getCreatedTimesheetsDetailByEmployeeIds(DateUtil.parseLocalDateFormatWithDefaultTime(
				employeeReader.weekStartDate),
				DateUtil.parseLocalDateFormatWithDefaultTime(employeeReader.weekEndDate), employeeIds)).thenReturn(timesheetList);
		List<Long> allOfficeId = new ArrayList<>();
		allOfficeId.add(0l);
		
		EmployeeConfigSettingsView employeeConfSettingView = new EmployeeConfigSettingsView();
		employeeConfSettingView.setConfigurationGroupId(UUID.randomUUID());
		employeeConfSettingView.setEffectiveEndDate(new Date());
		
		List<EmployeeConfigSettingsView> employeeConfigSettingsViews = new ArrayList<>();
		employeeConfigSettingsViews.add(employeeConfSettingView);
		
		when(employeeConfigSettingsViewRepository.getLatestConfigSetting(allOfficeId,
					EmployeeConfigSettingsView.UserGroupCategoryEnum.NONRCTR)).thenReturn(employeeConfigSettingsViews);
		
		
		when(employeeConfigSettingsViewRepository.getLatestConfigSettingByEndDate(
				allOfficeId, EmployeeConfigSettingsView.UserGroupCategoryEnum.NONRCTR,
				employeeConfSettingView.getEffectiveEndDate())).thenReturn(employeeConfigSettingsViews);
		
		
		employeeReader.read();
	}

}
