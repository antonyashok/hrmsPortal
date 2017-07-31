package com.tm.timesheetgeneration.writer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tm.common.domain.ContractorEmployeeEngagementTaskView;
import com.tm.common.repository.ContractorEmployeeEngagementTaskViewRepository;
import com.tm.timesheetgeneration.domain.ActivityLog;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.repository.ActivityLogRepository;
import com.tm.timesheetgeneration.repository.TimesheetRepository;
import com.tm.util.DateUtil;

public class ContractorWriterTest {
	
	private ContractorWriter contractorWriter;
	@Mock
	private TimesheetRepository timesheetRepository;
	@Mock
	private ActivityLogRepository activityLogRepository;
	@Mock
	private ContractorEmployeeEngagementTaskViewRepository conEmpEngagTaskViewRep;
	
	@BeforeMethod
	private void setUp(){
		this.timesheetRepository = mock(TimesheetRepository.class);
		this.conEmpEngagTaskViewRep = mock(ContractorEmployeeEngagementTaskViewRepository.class);
		this.activityLogRepository = mock(ActivityLogRepository.class);
		contractorWriter = new ContractorWriter();
		contractorWriter.setTimesheetRepository(timesheetRepository);
		contractorWriter.setActivityLogRepository(activityLogRepository);
	}
	
	@Test
	private void writeTest(){
		UUID engagementId = UUID.randomUUID();
		Timesheet timesheet = new Timesheet();
		timesheet.setEmployeeEngagementId(engagementId);
		timesheet.setStartDate(DateUtil.convertStringToDate("05/14/2017"));
		timesheet.setEndDate(DateUtil.convertStringToDate("05/20/2017"));
		
		List<Timesheet> timesheetList = new ArrayList<>();
		timesheetList.add(timesheet);
		
		
		List<UUID> empEngagementIdList = new ArrayList<>();
		empEngagementIdList.add(UUID.randomUUID());
		
		ContractorEmployeeEngagementTaskView contEmpEngTaskView = new ContractorEmployeeEngagementTaskView();
		contEmpEngTaskView.setEmployeeEngagementId(engagementId);
		
		List<ContractorEmployeeEngagementTaskView> value = new ArrayList<>();
		when(conEmpEngagTaskViewRep.getContractorEmployeeEngagementTaskViewByEmployeeEngagement(empEngagementIdList))
		.thenReturn(value);
		
		List<? super List<Timesheet>> timesheetslist = new ArrayList<>();
		
		timesheetslist.add(timesheetList);
		when(timesheetRepository.insert(timesheet)).thenReturn(timesheet);
		ActivityLog activelog = new ActivityLog();
//		activelog.set
		List<ActivityLog> activityLogList = new ArrayList<>();
		when(activityLogRepository.save(Mockito.anyList())).thenReturn(activityLogList);
		List<? extends List<Timesheet>> result = (List<? extends List<Timesheet>>) timesheetslist;
		contractorWriter.write(result);
	}

}

