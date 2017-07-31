package com.tm.timesheetgeneration.writer;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tm.common.repository.LookupViewRepository;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.util.DateUtil;

public class RecruiterTimesheetItemWriterTest {
	
	@InjectMocks
	private RecruiterTimesheetItemWriter recruiterTimesheetItemWriter;
	@Mock
	private LookupViewRepository lookupViewRepository;
	@Mock
	RecruiterTSRuleCalculationUtil recruiterTSRuleCalculationUtil;
	
	@BeforeMethod
	private void setUp(){
		this.lookupViewRepository = Mockito.mock(LookupViewRepository.class);
		this.recruiterTSRuleCalculationUtil = Mockito.mock(RecruiterTSRuleCalculationUtil.class);
		this.recruiterTimesheetItemWriter = new RecruiterTimesheetItemWriter();
		recruiterTimesheetItemWriter.setLookupViewRepository(lookupViewRepository);
		recruiterTimesheetItemWriter.setRecruiterTSRuleCalculationUtil(recruiterTSRuleCalculationUtil);
	}
	
	@Test
	private void recuriterWriter(){
		String HOURS = "Hours";
		String TS_ENTRY_TYPE_ID = "TS_ENTRY_TYPE_ID'";
		
		UUID engagementId = UUID.randomUUID();
		Timesheet timesheet = new Timesheet();
		timesheet.setEmployeeEngagementId(engagementId);
		timesheet.setStartDate(DateUtil.convertStringToDate("05/14/2017"));
		timesheet.setEndDate(DateUtil.convertStringToDate("05/20/2017"));
		
		List<Timesheet> timesheetList = new ArrayList<>();
		timesheetList.add(timesheet);
		List<? super List<Timesheet>> timesheetslist = new ArrayList<>();
		timesheetslist.add(timesheetList);
//		when(activityLogRepository.save(Mockito.anyList())).thenReturn(activityLogList);
		List<? extends List<Timesheet>> result = (List<? extends List<Timesheet>>) timesheetslist;
		when(lookupViewRepository.getByAttributeNameAndAttributeValue(TS_ENTRY_TYPE_ID, HOURS)).thenReturn("good");
		when(recruiterTSRuleCalculationUtil.getRuleCalculatedTimesheet(Mockito.anyObject())).thenReturn(Mockito.anyObject());
		recruiterTimesheetItemWriter.write(result);
	}

}
