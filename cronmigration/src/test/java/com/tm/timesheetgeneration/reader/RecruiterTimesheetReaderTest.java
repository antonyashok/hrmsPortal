package com.tm.timesheetgeneration.reader;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.data.domain.Sort;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tm.common.domain.RecruiterProfileView;
import com.tm.common.repository.RecruiterProfileViewRepository;
import com.tm.timesheet.configuration.domain.ConfigurationGroup;
import com.tm.timesheet.configuration.repository.ConfigurationGroupRepository;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.repository.TimesheetRepository;
import com.tm.util.DateUtil;

public class RecruiterTimesheetReaderTest {
	
	@InjectMocks
	private RecruiterTimesheetReader recruiterTimesheetReader;
	@Mock
	private RecruiterProfileViewRepository rcruiterProfileViewRepository;
	@Mock
	private TimesheetRepository timesheetRepository; 
	@Mock
	private ConfigurationGroupRepository configurationGroupRepository;
	
	@BeforeMethod
	private void setUp(){
		this.rcruiterProfileViewRepository = Mockito.mock(RecruiterProfileViewRepository.class);
		this.timesheetRepository = Mockito.mock(TimesheetRepository.class);
		this.configurationGroupRepository = Mockito.mock(ConfigurationGroupRepository.class);
		recruiterTimesheetReader = new RecruiterTimesheetReader();
		recruiterTimesheetReader.setRcruiterProfileViewRepository(rcruiterProfileViewRepository);
		recruiterTimesheetReader.setTimesheetRepository(timesheetRepository);
		recruiterTimesheetReader.setConfigurationGroupRepository(configurationGroupRepository);
	}
	
	@Test
	private void recuriterReader() throws Exception{
		Date liveDate = new Date();
		Calendar scalendar = Calendar.getInstance(); 
		scalendar.setTime(liveDate); 
		scalendar.add(Calendar.DATE, -9);
		liveDate = scalendar.getTime();
		
		recruiterTimesheetReader.fromId = 0;
		recruiterTimesheetReader.toId = 10;
		recruiterTimesheetReader.applicationLiveDate = liveDate;
		recruiterTimesheetReader.weekEndDate = DateUtil.convertStringToISODate("05/20/2017");
		recruiterTimesheetReader.weekStartDate = DateUtil.convertStringToISODate("05/14/2017");
		recruiterTimesheetReader.weekStartDay = "Sun";
		recruiterTimesheetReader.weekEndDay = "Sat";
		 
		Pageable pageableReq = new PageRequest(recruiterTimesheetReader.fromId, recruiterTimesheetReader.toId);
		
		Long employeeId = 123L;
		List<Long> employeeIds = new ArrayList<>();
		employeeIds.add(employeeId);
		RecruiterProfileView recruiterProfileView = new RecruiterProfileView();
		recruiterProfileView.setEmployeeId(employeeId);
		recruiterProfileView.setEmployeeName("Never give up");
		recruiterProfileView.setActiveFlag("Y");
		recruiterProfileView.setOfficeId(2528L);
		recruiterProfileView.setAccountManagerId(5689L);
		
		List<RecruiterProfileView> recuriterProfileViewList = new ArrayList<>();
		recuriterProfileViewList.add(recruiterProfileView);
		
		Page<RecruiterProfileView> recruiterProfileList = new PageImpl<>(recuriterProfileViewList, pageableReq,
				recuriterProfileViewList.size());
		when(rcruiterProfileViewRepository.getByEmployeeTypeAndJoiningDateInBetweenApplicaitonStartDate(pageableReq, 
				DateUtil.parseUtilDateFormatWithDefaultTime(recruiterTimesheetReader.applicationLiveDate))).thenReturn(recruiterProfileList);
		
		Timesheet timesheet = new Timesheet();
		timesheet.setId(UUID.randomUUID());
		timesheet.setTotalHours(125487.00);
		List<Timesheet> createdTimesheetList = new ArrayList<>();
		createdTimesheetList.add(timesheet);
		when(timesheetRepository.getCreatedTimesheetsDetailByEmployeeIds(DateUtil.parseLocalDateFormatWithDefaultTime(recruiterTimesheetReader.weekEndDate),
				DateUtil.parseLocalDateFormatWithDefaultTime(recruiterTimesheetReader.weekEndDate), employeeIds)).thenReturn(createdTimesheetList);
		recruiterTimesheetReader.read();
		
		ConfigurationGroup configurationGroup = new ConfigurationGroup();
		configurationGroup.setEffectiveEndDate(new Date());
		
		List<ConfigurationGroup> recruiterConfigForAll = new ArrayList<>();
		recruiterConfigForAll.add(configurationGroup);
		 Pageable pageable = new PageRequest(0, 1, Sort.Direction.DESC, "effectiveEndDate");
		when(configurationGroupRepository.getRecuriterByofficeIdNullAndIsWeekDayNotNull(pageable)).thenReturn(recruiterConfigForAll);
		
		List<ConfigurationGroup> recruiterConfigForOfficeId = new ArrayList<>();
		recruiterConfigForOfficeId.add(configurationGroup);
		when(configurationGroupRepository.findConfigurationGroupByOfficeId(recruiterProfileView.getOfficeId(), pageable))
			.thenReturn(recruiterConfigForOfficeId);
	}

}
