package com.tm.timesheet.service;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tm.timesheet.domain.Contractor;
import com.tm.timesheet.domain.Employee;
import com.tm.timesheet.domain.EngagementDetail;
import com.tm.timesheet.domain.ExpenseDetailedView;
import com.tm.timesheet.domain.ExpenseSummaryView;
import com.tm.timesheet.domain.InternalEmployee;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.domain.TimesheetDetails;
import com.tm.timesheet.repository.ContractorRepository;
import com.tm.timesheet.repository.EngagementRepository;
import com.tm.timesheet.repository.ExpenseDetailedViewRepository;
import com.tm.timesheet.repository.ExpenseSummaryViewRepository;
import com.tm.timesheet.repository.InternalEmployeeRepository;
import com.tm.timesheet.service.impl.TimesheetReportServiceImpl;
import com.tm.timesheet.timesheetview.repository.TimesheetDetailsViewRepository;
import com.tm.timesheet.timesheetview.repository.TimesheetViewRepository;
import com.tm.timesheet.timesheetview.repository.impl.TimesheetViewRepositoryImpl;

public class TimesheetReportServiceTest {

	@Mock
	private InternalEmployeeRepository internalEmployeeRepository;

	@Mock
	private ContractorRepository contractorRepository;

	@Mock
	private TimesheetViewRepository timesheetViewRepository;

	@Mock
	private TimesheetDetailsViewRepository timesheetDetailsViewRepository;

	@Mock
	private EngagementRepository engagementRepository;

	@Mock
	private ExpenseSummaryViewRepository expenseSummaryViewRepository;

	@Mock
	private ExpenseDetailedViewRepository expenseDetailedViewRepository;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private DiscoveryClient discoveryClient;

	@InjectMocks
	private TimesheetReportServiceImpl timesheetReportServiceImpl;
	
	@BeforeMethod
	public void timesheetTestConfig() {
		internalEmployeeRepository = Mockito.mock(InternalEmployeeRepository.class);
		contractorRepository = Mockito.mock(ContractorRepository.class);
		timesheetViewRepository = Mockito.mock(TimesheetViewRepository.class);
		timesheetDetailsViewRepository = Mockito.mock(TimesheetDetailsViewRepository.class);
		engagementRepository = Mockito.mock(EngagementRepository.class);
		expenseSummaryViewRepository = Mockito.mock(ExpenseSummaryViewRepository.class);
		expenseDetailedViewRepository = Mockito.mock(ExpenseDetailedViewRepository.class);
		restTemplate = Mockito.mock(RestTemplate.class);
		discoveryClient = Mockito.mock(DiscoveryClient.class);
		timesheetReportServiceImpl = new TimesheetReportServiceImpl(restTemplate, discoveryClient, internalEmployeeRepository, 
				contractorRepository, timesheetViewRepository, timesheetDetailsViewRepository, engagementRepository, 
				expenseSummaryViewRepository, expenseDetailedViewRepository);
	}

	@Test
	public void testGetTimesheetMonthlyReportExport() throws IOException {
		
		UUID projectId = UUID.randomUUID();
		List<Contractor> contractors = new ArrayList<>();
		Contractor contractor = mock(Contractor.class);
		when(contractor.getEmployeeId()).thenReturn(1L);
		when(contractor.getDesignation()).thenReturn("Manager");
		contractors.add(contractor);
		when(contractorRepository.findByProjectIdOrderByName(projectId.toString())).thenReturn(contractors);
		List<Timesheet> timesheetList = new ArrayList<>();
		Timesheet timesheet = mock(Timesheet.class);
		Employee employee = mock(Employee.class);
		when(timesheet.getEmployee()).thenReturn(employee);
		when(employee.getId()).thenReturn(1L);
		timesheetList.add(timesheet);
		when(timesheetViewRepository.getTimesheetsReports(anyListOf(Long.class), anyString(), anyString(), anyString(), 
				(UUID)anyObject(), anyString(), anyString())).thenReturn(timesheetList);
		timesheetReportServiceImpl.getTimesheetMonthlyReportExport(projectId, "Approved", "05", "2017", "pdf");
		
		List<InternalEmployee> internalEmployees = new ArrayList<>();
		InternalEmployee internalEmployee = new InternalEmployee();
		internalEmployees.add(internalEmployee);		
		Page<InternalEmployee> internalEmployeesPage = new PageImpl<InternalEmployee>(internalEmployees); 
		when(internalEmployeeRepository.findAll((Pageable)anyObject())).thenReturn(internalEmployeesPage);
		timesheetReportServiceImpl.getTimesheetMonthlyReportExport(null, "Approved", "05", "2017", "pdf");
	}
	
	@Test
	public void testGetTimesheetMonthlyReport() {

		UUID projectId = UUID.randomUUID();
		Pageable pageable = new PageRequest(2, 5);
		List<Contractor> contractors = new ArrayList<>();
		Contractor contractor = mock(Contractor.class);
		Page<Contractor> contractorsPage = new PageImpl<Contractor>(contractors); 
		when(contractor.getEmployeeId()).thenReturn(1L);
		when(contractor.getDesignation()).thenReturn("Manager");
		contractors.add(contractor);
		when(contractorRepository.findByProjectId(anyString(), (Pageable)anyObject())).thenReturn(contractorsPage);
		List<Timesheet> timesheetList = new ArrayList<>();
		Timesheet timesheet = mock(Timesheet.class);
		Employee employee = mock(Employee.class);
		when(timesheet.getEmployee()).thenReturn(employee);
		when(employee.getId()).thenReturn(1L);
		timesheetList.add(timesheet); 
		when(timesheetViewRepository.getTimesheetsReports(anyListOf(Long.class), anyString(), anyString(), anyString(), 
						(UUID)anyObject(), anyString(), anyString())).thenReturn(timesheetList);
		timesheetReportServiceImpl.getTimesheetMonthlyReport(pageable, projectId, "Approved", "05", "2017");
	}
	
	@Test
	public void testGetMonthlyDetailedReportHeaders() {
		timesheetReportServiceImpl.getMonthlyDetailedReportHeaders("01/05/2017", "01/10/2017");
	}
	
	@Test
	public void testGetMonthlyDetailedReport() {
		
		UUID projectId = UUID.randomUUID();
		UUID timesheetID = UUID.randomUUID();
		Pageable pageable = new PageRequest(2, 5);
		List<Contractor> contractors = new ArrayList<>();
		Contractor contractor = mock(Contractor.class);
		when(contractor.getEmployeeId()).thenReturn(1L);
		when(contractor.getDesignation()).thenReturn("Manager");
		contractors.add(contractor);
		Page<Contractor> contractorsPage = new PageImpl<Contractor>(contractors); 
		when(contractorRepository.findByProjectId(anyString(), (Pageable)anyObject())).thenReturn(contractorsPage);
		List<Timesheet> timesheetList = new ArrayList<>();
		Timesheet timesheet = mock(Timesheet.class);
		Employee employee = mock(Employee.class);
		when(timesheet.getEmployee()).thenReturn(employee);
		when(timesheet.getId()).thenReturn(timesheetID);
		when(employee.getId()).thenReturn(1L);
		timesheetList.add(timesheet); 
		when(timesheetViewRepository.getTimesheetsReports(anyListOf(Long.class), anyString(), anyString(), anyString(), 
						(UUID)anyObject(), anyString(), anyString())).thenReturn(timesheetList);
		
		List<TimesheetDetails> timesheetDetails = new ArrayList<>();
		TimesheetDetails timesheetDetail = mock(TimesheetDetails.class);
		when(timesheetDetail.getTimesheetId()).thenReturn(timesheetID);
		when(timesheetDetail.getTimesheetDate()).thenReturn(new Date("01/05/2017"));
		when(timesheetDetail.getHours()).thenReturn(10D);
		when(timesheetDetail.getUnits()).thenReturn(2L);
		timesheetDetails.add(timesheetDetail);
		when(timesheetDetailsViewRepository.findByTimesheetId(anyListOf(UUID.class))).thenReturn(timesheetDetails);
		
		timesheetReportServiceImpl.getMonthlyDetailedReport(pageable, "01/05/2017", "01/10/2017", projectId, "Approved");
		
		List<InternalEmployee> internalEmployees = new ArrayList<>();
		InternalEmployee internalEmployee = new InternalEmployee();
		internalEmployees.add(internalEmployee);		
		Page<InternalEmployee> internalEmployeesPage = new PageImpl<InternalEmployee>(internalEmployees); 
		when(internalEmployeeRepository.findAll((Pageable)anyObject())).thenReturn(internalEmployeesPage);
		timesheetReportServiceImpl.getMonthlyDetailedReport(pageable, "01/05/2017", "01/10/2017", null, "Approved");
	}
	
	@Test (expectedExceptions = {InvalidMediaTypeException.class})
	public void testGetMonthlyDetailedReportExport() {
		
		UUID projectId = UUID.randomUUID();
		UUID timesheetID = UUID.randomUUID();
		ReflectionTestUtils.setField(timesheetReportServiceImpl, "jasperFileBasePath", "templates/temp/" );
		List<Contractor> contractors = new ArrayList<>();
		Contractor contractor = mock(Contractor.class);
		when(contractor.getEmployeeId()).thenReturn(1L);
		when(contractor.getName()).thenReturn("TestEmployee");
		when(contractor.getDesignation()).thenReturn("Manager");
		contractors.add(contractor);
		when(contractorRepository.findByProjectIdOrderByName(projectId.toString())).thenReturn(contractors);
		List<Timesheet> timesheetList = new ArrayList<>();
		Timesheet timesheet = mock(Timesheet.class);
		Employee employee = mock(Employee.class);
		when(timesheet.getEmployee()).thenReturn(employee);
		when(timesheet.getId()).thenReturn(timesheetID);
		when(employee.getId()).thenReturn(1L);
		timesheetList.add(timesheet);
		when(timesheetViewRepository.getTimesheetsReports(anyListOf(Long.class), anyString(), anyString(), anyString(), 
				(UUID)anyObject(), anyString(), anyString())).thenReturn(timesheetList);
		
		List<TimesheetDetails> timesheetDetails = new ArrayList<>();
		TimesheetDetails timesheetDetail = mock(TimesheetDetails.class);
		when(timesheetDetail.getTimesheetId()).thenReturn(timesheetID);
		when(timesheetDetail.getTimesheetDate()).thenReturn(new Date("01/05/2017"));
		when(timesheetDetail.getHours()).thenReturn(10D);
		when(timesheetDetail.getUnits()).thenReturn(2L);
		timesheetDetails.add(timesheetDetail);
		when(timesheetDetailsViewRepository.findByTimesheetId(anyListOf(UUID.class))).thenReturn(timesheetDetails);
		
		timesheetReportServiceImpl.getMonthlyDetailedReportExport("01/05/2017", "01/10/2017", projectId, "Approved", "pdf");
	}
	
	@Test
	public void testGetMonthlyReportList() {
		
		UUID projectId = UUID.randomUUID();
		Pageable pageable = new PageRequest(2, 5);
		timesheetReportServiceImpl.getMonthlyReportList(pageable, projectId, "Approved", 05, 2017);
	}
	
	@Test
	public void testGetMonthlySummaryReportHeaders() {
		timesheetReportServiceImpl.getMonthlySummaryReportHeaders("01/05/2017", "01/10/2017");
	}

	@Test
	public void testGetMonthlySummaryReport() {
		
		UUID projectId = UUID.randomUUID();
		Pageable pageable = new PageRequest(2, 5);
		ReflectionTestUtils.setField(timesheetReportServiceImpl, "jasperFileBasePath", "templates/temp/" );
		UUID timesheetID = UUID.randomUUID();
		List<Contractor> contractors = new ArrayList<>();
		Contractor contractor = mock(Contractor.class);
		when(contractor.getEmployeeId()).thenReturn(1L);
		when(contractor.getName()).thenReturn("TestEmployee");
		when(contractor.getDesignation()).thenReturn("Manager");
		contractors.add(contractor);
		Page<Contractor> contractorPage = new PageImpl<Contractor>(contractors); 
		
		when(contractorRepository.findByProjectId(anyString(), (Pageable)anyObject())).thenReturn(contractorPage);
		List<Timesheet> timesheetList = new ArrayList<>();
		Timesheet timesheet = mock(Timesheet.class);
		Employee employee = mock(Employee.class);
		when(timesheet.getEmployee()).thenReturn(employee);
		when(timesheet.getId()).thenReturn(timesheetID);
		when(employee.getId()).thenReturn(1L);
		timesheetList.add(timesheet);
		when(timesheetViewRepository.getTimesheetsReports(anyListOf(Long.class), anyString(), anyString(), anyString(), 
				(UUID)anyObject(), anyString(), anyString())).thenReturn(timesheetList);
		
		List<TimesheetDetails> timesheetDetails = new ArrayList<>();
		TimesheetDetails timesheetDetail = mock(TimesheetDetails.class);
		when(timesheetDetail.getTimesheetId()).thenReturn(timesheetID);
		when(timesheetDetail.getTimesheetDate()).thenReturn(new Date("01/05/2017"));
		when(timesheetDetail.getHours()).thenReturn(10D);
		when(timesheetDetail.getUnits()).thenReturn(2L);
		timesheetDetails.add(timesheetDetail);
		when(timesheetDetailsViewRepository.findByTimesheetId(anyListOf(UUID.class))).thenReturn(timesheetDetails);
		
		timesheetReportServiceImpl.getMonthlySummaryReport(pageable, "01/05/2017", "01/10/2017", projectId, "Approved");
	}
	
	@Test (expectedExceptions = {InvalidMediaTypeException.class})
	public void testGetMonthlySummaryReportExport() {
		
		UUID projectId = UUID.randomUUID();
		UUID timesheetID = UUID.randomUUID();
		ReflectionTestUtils.setField(timesheetReportServiceImpl, "jasperFileBasePath", "templates/temp/" );
		List<Contractor> contractors = new ArrayList<>();
		Contractor contractor = mock(Contractor.class);
		when(contractor.getEmployeeId()).thenReturn(1L);
		when(contractor.getName()).thenReturn("TestEmployee");
		when(contractor.getDesignation()).thenReturn("Manager");
		contractors.add(contractor);
		when(contractorRepository.findByProjectIdOrderByName(projectId.toString())).thenReturn(contractors);
		List<Timesheet> timesheetList = new ArrayList<>();
		Timesheet timesheet = mock(Timesheet.class);
		Employee employee = mock(Employee.class);
		when(timesheet.getEmployee()).thenReturn(employee);
		when(timesheet.getId()).thenReturn(timesheetID);
		when(employee.getId()).thenReturn(1L);
		timesheetList.add(timesheet);
		when(timesheetViewRepository.getTimesheetsReports(anyListOf(Long.class), anyString(), anyString(), anyString(), 
				(UUID)anyObject(), anyString(), anyString())).thenReturn(timesheetList);
		EngagementDetail engagementDetail = mock(EngagementDetail.class); 
		when(engagementDetail.getName()).thenReturn("TestEngagement");
		when(engagementRepository.findOne(projectId.toString())).thenReturn(engagementDetail);
		List<TimesheetDetails> timesheetDetails = new ArrayList<>();
		TimesheetDetails timesheetDetail = mock(TimesheetDetails.class);
		when(timesheetDetail.getTimesheetId()).thenReturn(timesheetID);
		when(timesheetDetail.getTimesheetDate()).thenReturn(new Date("01/05/2017"));
		when(timesheetDetail.getHours()).thenReturn(10D);
		when(timesheetDetail.getUnits()).thenReturn(2L);
		timesheetDetails.add(timesheetDetail);
		when(timesheetDetailsViewRepository.findByTimesheetId(anyListOf(UUID.class), anyString(), anyString())).thenReturn(timesheetDetails);
		
		timesheetReportServiceImpl.getMonthlySummaryReportExport("01/05/2017", "01/10/2017", projectId, "Approved", "pdf");
	}
	
	@Test
	public void testGetExpenseSummaryReport() throws ParseException {

		UUID projectId = UUID.randomUUID();
		Pageable pageable = new PageRequest(2, 5);
		List<ExpenseSummaryView> expenseSummaryViews = new ArrayList<>();
		ExpenseSummaryView expenseSummaryView = mock(ExpenseSummaryView.class);
		when(expenseSummaryView.getEmployeeId()).thenReturn("1");
		when(expenseSummaryView.getEmployeeName()).thenReturn("TestEmployee");
		when(expenseSummaryView.getDesignation()).thenReturn("Manager");
		expenseSummaryViews.add(expenseSummaryView);
		Page<ExpenseSummaryView> expenseSummaryViewList = new PageImpl<ExpenseSummaryView>(expenseSummaryViews);
		when(expenseSummaryViewRepository.getExpenseSummaryDetails((Pageable)anyObject(), anyString(), anyString(), anyString(), 
				anyString(), anyString())).thenReturn(expenseSummaryViewList);
		timesheetReportServiceImpl.getExpenseSummaryReport(pageable, 1L, "MAY", "2017", projectId.toString(), "Approved");
		
	}
	
	@Test
	public void testGetExpenseSummaryInReport() throws IOException, ParseException {
		
		UUID projectId = UUID.randomUUID();
		Pageable pageable = new PageRequest(2, 5);
		List<ExpenseSummaryView> expenseSummaryViews = new ArrayList<>();
		ExpenseSummaryView expenseSummaryView = mock(ExpenseSummaryView.class);
		when(expenseSummaryView.getEmployeeId()).thenReturn("1");
		when(expenseSummaryView.getEmployeeName()).thenReturn("TestEmployee");
		when(expenseSummaryView.getDesignation()).thenReturn("Manager");
		expenseSummaryViews.add(expenseSummaryView);
		when(expenseSummaryViewRepository.getExpenseSummaryInDetails(anyString(), anyString(), anyString(), 
				anyString(), anyString())).thenReturn(expenseSummaryViews);
		timesheetReportServiceImpl.getExpenseSummaryInReport(pageable, 1L, "MAY", "2017", projectId.toString(), "Approved", "pdf");
		
		when(expenseSummaryViewRepository.getExpenseSummaryInDetails(anyString(), anyString())).thenReturn(expenseSummaryViews);
		timesheetReportServiceImpl.getExpenseSummaryInReport(pageable, null, "MAY", "2017", projectId.toString(), "Approved", "pdf");
	}
	
	@Test
	public void testGetExpenseDetailedReport() {
		
		UUID expenseReportUUID = UUID.randomUUID();
		Pageable pageable = new PageRequest(2, 5);
		
		List<ExpenseDetailedView> expenseDetailedViews = new ArrayList<>();
		ExpenseDetailedView expenseDetailedView = mock(ExpenseDetailedView.class);
		expenseDetailedViews.add(expenseDetailedView);
		Page<ExpenseDetailedView> expenseDetailedViewList = new PageImpl<ExpenseDetailedView>(expenseDetailedViews);
		when(expenseDetailedViewRepository.getExpenseDetailedReport((Pageable)anyObject(), (UUID)anyObject())).thenReturn(expenseDetailedViewList);
		
		timesheetReportServiceImpl.getExpenseDetailedReport(pageable, expenseReportUUID);
	}
	
	@Test
	public void tesGgetExpenseByIdReport() throws IOException {
		
		UUID expenseReportUUID = UUID.randomUUID();
		Pageable pageable = new PageRequest(2, 5);
		
		List<ExpenseDetailedView> expenseDetailedViews = new ArrayList<>();
		ExpenseDetailedView expenseDetailedView = mock(ExpenseDetailedView.class);
		expenseDetailedViews.add(expenseDetailedView);
		Page<ExpenseDetailedView> expenseDetailedViewList = new PageImpl<ExpenseDetailedView>(expenseDetailedViews);
		when(expenseDetailedViewRepository.getExpenseDetailedReport((Pageable)anyObject(), (UUID)anyObject())).thenReturn(expenseDetailedViewList);
		
		timesheetReportServiceImpl.getExpenseByIdReport(pageable, expenseReportUUID, "pdf");
	}
}
