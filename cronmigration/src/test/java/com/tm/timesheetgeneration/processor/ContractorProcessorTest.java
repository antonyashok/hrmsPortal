package com.tm.timesheetgeneration.processor;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tm.common.domain.ContractorEmployeeEngagementView;
import com.tm.common.domain.Employee;
import com.tm.timesheetgeneration.domain.Engagement;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.service.dto.ContractorEngagementBatchDTO;
import com.tm.util.DateUtil;
import com.tm.util.Week;

public class ContractorProcessorTest {

	@InjectMocks
	private ContractorProcessor contractorProcessor;
	
	@BeforeMethod
	private void setUp(){
		contractorProcessor = new ContractorProcessor();
	}
	
	@Test
	private void processorTest(){
		Long empId = 1245L;
		UUID engagementId = UUID.randomUUID();
		ContractorEngagementBatchDTO conEngBatchDTO = new ContractorEngagementBatchDTO();
		conEngBatchDTO.setEmployeeId(12457L);
		Week week = new Week();
		Date startUtilDate = DateUtil.convertStringToDate("05/14/2017");
		week.setStartUtilDate(startUtilDate);
		week.setStartDate(DateUtil.convertStringToISODate("05/14/2017"));
		week.setEndDate(DateUtil.convertStringToISODate("05/20/2017"));
		
		
		List<Week> allWeekList = new ArrayList<>();
		allWeekList.add(week);
		
		conEngBatchDTO.setTodayWeek(week);
		conEngBatchDTO.setAllWeekList(allWeekList);
		ContractorEmployeeEngagementView contEmpEngView = new ContractorEmployeeEngagementView();
		contEmpEngView.setEmplEffStartDate(DateUtil.convertStringToDate("05/14/2017"));
		contEmpEngView.setEmplEffEndDate(DateUtil.convertStringToDate("05/20/2017"));
		contEmpEngView.setEmplId(empId);
		contEmpEngView.setEngagementId(engagementId);
		contEmpEngView.setTsMethod("Good");
		conEngBatchDTO.setContractorEmployeeEngagementView(contEmpEngView);
		
		List<ContractorEmployeeEngagementView> contractorEngagementList = new ArrayList<>();
		contractorEngagementList.add(contEmpEngView);
		conEngBatchDTO.setContractorEngagementList(contractorEngagementList);
		
		Timesheet timesheet = new Timesheet();
		Employee employee = new Employee();
		employee.setId(empId);
		employee.setName("good");
		timesheet.setEmployee(employee);
		Engagement engagementObject = new Engagement();
		engagementObject.setId(engagementId.toString());
		timesheet.setEngagement(engagementObject);
		
		conEngBatchDTO.setWeek(week);
		
		List<Timesheet> timesheetList = new ArrayList<>();
		timesheetList.add(timesheet);
		conEngBatchDTO.setTimesheetList(timesheetList);
		contractorProcessor.process(conEngBatchDTO);
	}
}
