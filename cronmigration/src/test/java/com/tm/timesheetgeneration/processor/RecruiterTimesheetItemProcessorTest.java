package com.tm.timesheetgeneration.processor;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tm.common.domain.Employee;
import com.tm.common.domain.RecruiterProfileView;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.dto.RecruiterTimeDTO;
import com.tm.timesheetgeneration.service.dto.RecruiterBatchDTO;
import com.tm.util.DateConversionUtil;
import com.tm.util.DateUtil;
import com.tm.util.Week;

public class RecruiterTimesheetItemProcessorTest {
	
	@InjectMocks
	private RecruiterTimesheetItemProcessor recruiterTimesheetItemProcessor;
	@Mock
	private RecruiterTimeSheetMigrationProcessor timeSheetMigrationProcessor;
	
	
	@BeforeMethod
	private void setUp(){
		this.timeSheetMigrationProcessor = Mockito.mock(RecruiterTimeSheetMigrationProcessor.class);
		this.recruiterTimesheetItemProcessor = new RecruiterTimesheetItemProcessor();
	}
	
	@Test
	private void recuriterTimesheetProcess(){
		Long employeeId = 4578L;
		Long accountManagerId = 4569L;
		
		Week week = new Week();
		Date startUtilDate = DateUtil.convertStringToDate("05/14/2017");
		week.setStartUtilDate(startUtilDate);
		week.setStartDate(DateUtil.convertStringToISODate("05/14/2017"));
		week.setEndDate(DateUtil.convertStringToISODate("05/20/2017"));
		
		LocalDate weekStartDate = week.getStartDate();
		LocalDate weekEndDate = week.getEndDate();
		
		Employee employee = new Employee();
		employee.setId(employeeId);
		employee.setName("Indian");
		
		List<Week> allWeekList = new ArrayList<>();
		allWeekList.add(week);
		RecruiterBatchDTO recruiterBatchDTO = new RecruiterBatchDTO();
		recruiterBatchDTO.setAllWeekList(allWeekList);
		
		RecruiterProfileView recruiterProfileView = new RecruiterProfileView();
		recruiterProfileView.setEmployeeId(employeeId);
		List<RecruiterProfileView> recruiterProfileViewList = new ArrayList<>();
		recruiterProfileViewList.add(recruiterProfileView);
		
		UUID configurationId = UUID.randomUUID();
		RecruiterTimeDTO recruiterTimeDTO = new RecruiterTimeDTO();
		recruiterTimeDTO.setJoiningDate(DateConversionUtil.convertToDate(weekEndDate));
		recruiterTimeDTO.setBreakStartTime("1");
		recruiterTimeDTO.setBreakEndTime("2");
		recruiterTimeDTO.setEmployeeId(employeeId);
		recruiterTimeDTO.setAccountManagerId(accountManagerId);
		recruiterTimeDTO.setEmployeeName("qwert");
		recruiterTimeDTO.setOfficeName("smi");
		recruiterTimeDTO.setOfficeId(7896L);
		recruiterTimeDTO.setConfigurationId(configurationId);
		
		
		List<RecruiterTimeDTO> recruiterTimeDTOs = new ArrayList<>();
		
		recruiterBatchDTO.setRecruiterTimeDTOs(recruiterTimeDTOs);
		recruiterBatchDTO.setRecruiterProfileViewList(recruiterProfileViewList);
		Timesheet timesheet = new Timesheet();
		timesheet.setEmployee(employee);
		List<Timesheet> timesheetList = new ArrayList<>();
		timesheetList.add(timesheet);
		
		recruiterBatchDTO.setTimesheetList(timesheetList);
		
		when(timeSheetMigrationProcessor.processMigration(recruiterBatchDTO.getRecruiterTimeDTOs(),	weekStartDate, weekEndDate)).thenReturn(null);
		recruiterTimesheetItemProcessor.process(recruiterBatchDTO);
	}
	

}
