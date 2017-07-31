package com.tm.timesheet.timesheetview.service;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.domain.ActivityLog;
import com.tm.timesheet.domain.Employee;
import com.tm.timesheet.domain.Engagement;
import com.tm.timesheet.domain.LookUpType;
import com.tm.timesheet.domain.TimeDetail;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.domain.TimesheetDetails;
import com.tm.timesheet.service.dto.CommonEngagementDTO;
import com.tm.timesheet.service.dto.TaskDTO;
import com.tm.timesheet.service.dto.TimesheetDetailsDTO;
import com.tm.timesheet.service.dto.TimesheetStatusCount;
import com.tm.timesheet.timeoff.domain.Timeoff;
import com.tm.timesheet.timeoff.service.TimeoffService;
import com.tm.timesheet.timeoff.service.dto.HolidayDTO;
import com.tm.timesheet.timeoff.service.dto.HolidayResource;
import com.tm.timesheet.timesheetview.exception.InvalidDateRangeException;
import com.tm.timesheet.timesheetview.exception.TimesheetDetailsNotFoundException;
import com.tm.timesheet.timesheetview.repository.ActivityLogViewRepository;
import com.tm.timesheet.timesheetview.repository.TimeoffViewRepository;
import com.tm.timesheet.timesheetview.repository.TimesheetAttachmentsViewRepository;
import com.tm.timesheet.timesheetview.repository.TimesheetDetailsViewRepository;
import com.tm.timesheet.timesheetview.repository.TimesheetViewRepository;
import com.tm.timesheet.timesheetview.service.impl.TimesheetViewServiceImpl;

public class TimesheetViewServiceTest {

	@InjectMocks
	TimesheetViewServiceImpl timesheetViewServiceImpl;
	
	@Mock
	private TimesheetViewRepository timesheetViewRepository;

	@Mock
    private ActivityLogViewRepository activityLogViewRepository;

	@Mock
    private TimesheetDetailsViewRepository timesheetDetailsViewRepository;

	@Mock
    private RestTemplate restTemplate;

	@Mock
    private MongoTemplate mongoTemplate;

	@Mock
    private DiscoveryClient discoveryClient;

	@Mock
    private TimeoffViewRepository timeOffRepository;
	
	@Mock
	private TimesheetAttachmentsViewRepository timesheetAttachmentsViewRepository;
    
	@Mock
	private TimeoffService timeoffService;
	
    @BeforeTest
    public void setUp() {
    	timesheetViewRepository = mock(TimesheetViewRepository.class);
    	activityLogViewRepository = mock(ActivityLogViewRepository.class);
    	timesheetDetailsViewRepository = mock(TimesheetDetailsViewRepository.class);
    	restTemplate = mock(RestTemplate.class);
    	mongoTemplate = mock(MongoTemplate.class);
    	discoveryClient = mock(DiscoveryClient.class);
    	timeOffRepository = mock(TimeoffViewRepository.class);
    	timeoffService = mock(TimeoffService.class);
    	timesheetAttachmentsViewRepository = mock(TimesheetAttachmentsViewRepository.class);
    	timesheetViewServiceImpl = new TimesheetViewServiceImpl(timesheetViewRepository, timesheetDetailsViewRepository, restTemplate, 
    			discoveryClient, activityLogViewRepository, timeOffRepository, mongoTemplate, timesheetAttachmentsViewRepository,timeoffService);
    }

    @Test(dataProvider = "employeeRestTemplateDataProvider")
    public void getTestStatusCount(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

    	MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		TimesheetStatusCount timesheetStatusCount = mock(TimesheetStatusCount.class);
		List<TimesheetStatusCount> statusCountList = Arrays.asList(timesheetStatusCount);
		when(timesheetViewRepository.getStatusCount(anyObject(), anyObject(), anyListOf(Long.class), anyString(), anyString(), 
				anyString(), anyString(), anyString(),anyString())).thenReturn(statusCountList);
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getStatusCount("approver", "01/01/2017", "01/31/2017", "E", "searchParam", "Proxy"));
    }
    
    @Test(dataProvider = "employeeRestTemplateDataProvider")
    public void testgetRecruiterStatusCount(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws InvalidDateRangeException, ParseException {

    	MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		String manageEmployeeURL = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/am-employees?managerEmployeeId=100";
        EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();        
        employeeProfileDTO.setEmployeeId(100L);
        List<EmployeeProfileDTO> employeeProfileDTOs = Arrays.asList(employeeProfileDTO);
        ResponseEntity<List<EmployeeProfileDTO>> resp = new ResponseEntity<>(employeeProfileDTOs, HttpStatus.OK);
        ParameterizedTypeReference<List<EmployeeProfileDTO>> respType = new ParameterizedTypeReference<List<EmployeeProfileDTO>>() {};
		when(restTemplate.exchange(manageEmployeeURL, HttpMethod.GET, new HttpEntity<>(httpHeaders), respType)).thenReturn(resp);
		
		TimesheetStatusCount timesheetStatusCount = mock(TimesheetStatusCount.class);
		List<TimesheetStatusCount> statusCountList = Arrays.asList(timesheetStatusCount);
		when(timesheetStatusCount.getStatus()).thenReturn("Verified");
		when(timesheetViewRepository.getStatusCount(anyObject(), anyObject(), anyListOf(Long.class), anyString(), anyString(), 
				anyString(), anyString(), anyString(), anyString())).thenReturn(statusCountList);
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("approver", "01/01/2017", "01/31/2017", "E", "searchParam", "approver"));
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("approver", "01/01/2017", "01/31/2017", "E", "searchParam", "submitter"));
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("approver", "01/01/2017", "01/31/2017", "E", "searchParam", "Proxy"));
		
		when(timesheetStatusCount.getStatus()).thenReturn("Not Verified");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("approver", "01/01/2017", "01/31/2017", "E", "searchParam", "Proxy"));
		
		when(timesheetStatusCount.getStatus()).thenReturn("Not Submitted");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("approver", "01/01/2017", "01/31/2017", "E", "searchParam", "Proxy"));
		
		when(timesheetStatusCount.getStatus()).thenReturn("Overdue");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("approver", "01/01/2017", "01/31/2017", "E", "searchParam", "Proxy"));
		
		when(timesheetStatusCount.getStatus()).thenReturn("Dispute");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("approver", "01/01/2017", "01/31/2017", "E", "searchParam", "Proxy"));
		
		when(timesheetStatusCount.getStatus()).thenReturn("Verified");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("approver", "01/01/2017", "01/31/2017", "E", "searchParam", "Verification"));
		
		when(timesheetStatusCount.getStatus()).thenReturn("Not Verified");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("approver", "01/01/2017", "01/31/2017", "E", "searchParam", "Verification"));
		
		when(timesheetStatusCount.getStatus()).thenReturn("Dispute");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("approver", "01/01/2017", "01/31/2017", "E", "searchParam", "Verification"));
		
		when(timesheetStatusCount.getStatus()).thenReturn("Awaiting Approval");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("approver", "01/01/2017", "01/31/2017", "E", "searchParam", "approver"));
		
		when(timesheetStatusCount.getStatus()).thenReturn("Approved");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("approver", "01/01/2017", "01/31/2017", "E", "searchParam", "approver"));
		
		when(timesheetStatusCount.getStatus()).thenReturn("Rejected");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("submitter", "01/01/2017", "01/31/2017", "E", "searchParam", "approver"));
		
		when(timesheetStatusCount.getStatus()).thenReturn("Not Submitted");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("submitter", "01/01/2017", "01/31/2017", "E", "searchParam", "approver"));
		
		when(timesheetStatusCount.getStatus()).thenReturn("Overdue");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getRecruiterStatusCount("submitter", "01/01/2017", "01/31/2017", "E", "searchParam", "approver"));
		
		timesheetViewServiceImpl.getRecruiterStatusCount("submitter", "01/01/2017", "01/31/2017", "C", "searchParam", "approver");
    }
    
    @Test(dataProvider = "employeeRestTemplateDataProvider")
    public void testGetAllTimesheets(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {
    	
    	MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		String manageEmployeeURL = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/am-employees?managerEmployeeId=100";
        EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();        
        employeeProfileDTO.setEmployeeId(100L);
        List<EmployeeProfileDTO> employeeProfileDTOs = Arrays.asList(employeeProfileDTO);
        ResponseEntity<List<EmployeeProfileDTO>> resp = new ResponseEntity<>(employeeProfileDTOs, HttpStatus.OK);
        ParameterizedTypeReference<List<EmployeeProfileDTO>> respType = new ParameterizedTypeReference<List<EmployeeProfileDTO>>() {};
		when(restTemplate.exchange(manageEmployeeURL, HttpMethod.GET, new HttpEntity<>(httpHeaders), respType)).thenReturn(resp);
		
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getAllTimesheets(new PageRequest(5, 5), "TestStatus", "01/01/2017", "01/31/2017", 
				"TestSearchParam", "approver", "accountManager" , "TestOffice", "Proxy"));
		
		Timesheet timesheet = mock(Timesheet.class);
		Employee employee = Mockito.mock(Employee.class);
		Engagement engagement = Mockito.mock(Engagement.class);
		when(engagement.getId()).thenReturn("000181d4-2b11-8702-0035-111f1f15f733");
		when(employee.getId()).thenReturn(100L);
		when(employee.getReportingManagerId()).thenReturn(100L);
		when(employee.getType()).thenReturn("R");
		when(timesheet.getEmployee()).thenReturn(employee);
		when(timesheet.getEngagement()).thenReturn(engagement);
		when(timesheet.getStartDate()).thenReturn(new Date("01/05/2016"));
		when(timesheet.getEndDate()).thenReturn(new Date("01/05/2018"));
		when(timesheet.getWorkHours()).thenReturn(70d);
		when(timesheet.getPtoHours()).thenReturn(10d);
		when(timesheet.getTotalHours()).thenReturn(100d);
		Page<Timesheet> timesheetList = new PageImpl<Timesheet>(Arrays.asList(timesheet));
		
		when(timesheetViewRepository.getAllTimesheetForAccountManager(anyLong(), anyString(), anyObject(), anyObject(), anyString(), 
				(Pageable)anyObject(), anyString(), anyString())).thenReturn(timesheetList);
		when(timeoffService.calculateTimeOffHours(anyLong(), anyString(), anyString(), anyString())).thenReturn(10D);
    	AssertJUnit.assertNotNull(timesheetViewServiceImpl.getAllTimesheets(new PageRequest(5, 5), "TestStatus", "01/01/2017", "01/31/2017", 
				"TestSearchParam", "approver", "accountManager" , "TestOffice", "Proxy"));
    	
		when(timesheetViewRepository.getAllTimesheetForAccountManager(anyLong(), anyString(), anyObject(), anyObject(), 
				(Pageable)anyObject(), anyString(), anyString(), anyString())).thenReturn(timesheetList);
    	AssertJUnit.assertNotNull(timesheetViewServiceImpl.getAllTimesheets(new PageRequest(5, 5), "TestStatus", "01/01/2017", "01/31/2017", 
				"TestSearchParam", "approver", "payrollManager" , "TestOffice", "Proxy"));

		when(timesheetViewRepository.getAllTimesheetForVerification(anyLong(), anyString(), anyObject(), anyObject(), anyString(), 
				(Pageable)anyObject(), anyString(), anyString())).thenReturn(timesheetList);
    	AssertJUnit.assertNotNull(timesheetViewServiceImpl.getAllTimesheets(new PageRequest(5, 5), "TestStatus", "01/01/2017", "01/31/2017", 
				"TestSearchParam", "approver", "accountManager" , "TestOffice", "Verification"));
    	
		when(timesheetViewRepository.getAllTimesheetForVerification(anyLong(), anyString(), anyObject(), anyObject(), 
				(Pageable)anyObject(), anyString(), anyString(), anyString())).thenReturn(timesheetList);
    	AssertJUnit.assertNotNull(timesheetViewServiceImpl.getAllTimesheets(new PageRequest(5, 5), "TestStatus", "01/01/2017", "01/31/2017", 
				"TestSearchParam", "approver", "payrollManager" , "TestOffice", "Verification"));
    }
    
    @Test(dataProvider = "employeeRestTemplateDataProvider")
    public void testGetAllRecruiterTimesheets(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {
    	
    	MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		String manageEmployeeURL = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/am-employees?managerEmployeeId=100";
        EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();        
        employeeProfileDTO.setEmployeeId(100L);
        List<EmployeeProfileDTO> employeeProfileDTOs = Arrays.asList(employeeProfileDTO);
        ResponseEntity<List<EmployeeProfileDTO>> resp = new ResponseEntity<>(employeeProfileDTOs, HttpStatus.OK);
        ParameterizedTypeReference<List<EmployeeProfileDTO>> respType = new ParameterizedTypeReference<List<EmployeeProfileDTO>>() {};
		when(restTemplate.exchange(manageEmployeeURL, HttpMethod.GET, new HttpEntity<>(httpHeaders), respType)).thenReturn(resp);
		
		Timesheet timesheet = mock(Timesheet.class);
		Employee employee = Mockito.mock(Employee.class);
		Engagement engagement = Mockito.mock(Engagement.class);
		when(engagement.getId()).thenReturn("000181d4-2b11-8702-0035-111f1f15f733");
		when(employee.getId()).thenReturn(100L);
		when(employee.getReportingManagerId()).thenReturn(100L);
		when(employee.getType()).thenReturn("R");
		when(timesheet.getEmployee()).thenReturn(employee);
		when(timesheet.getEngagement()).thenReturn(engagement);
		when(timesheet.getStartDate()).thenReturn(new Date("01/05/2016"));
		when(timesheet.getEndDate()).thenReturn(new Date("01/05/2018"));
		when(timesheet.getWorkHours()).thenReturn(70d);
		when(timesheet.getPtoHours()).thenReturn(10d);
		when(timesheet.getTotalHours()).thenReturn(100d);
		Page<Timesheet> timesheetList = new PageImpl<Timesheet>(Arrays.asList(timesheet));
		
    	when(timesheetViewRepository.getAllTimesheet(anyListOf(Long.class), anyString(), anyObject(), anyObject(), 
				(Pageable)anyObject(), anyString(), anyString(), anyString(), anyString())).thenReturn(timesheetList);
    	AssertJUnit.assertNotNull(timesheetViewServiceImpl.getAllRecruiterTimesheets(new PageRequest(5, 5), "TestStatus", "01/01/2017", "01/31/2017", 
				"TestSearchParam", "approver", "accountManager" , "TestOffice", "approver"));
    	
		when(timesheetViewRepository.getAllPayrollTimesheet(anyLong(), anyString(), anyObject(), anyObject(), 
				anyString(), (Pageable)anyObject(), anyString(), anyString(), anyString())).thenReturn(timesheetList);
    	AssertJUnit.assertNotNull(timesheetViewServiceImpl.getAllRecruiterTimesheets(new PageRequest(5, 5), "TestStatus", "01/01/2017", "01/31/2017", 
				"TestSearchParam", "approver", "payrollManager" , "TestOffice", "approver"));
    	
    	AssertJUnit.assertNotNull(timesheetViewServiceImpl.getAllRecruiterTimesheets(new PageRequest(5, 5), "TestStatus", "01/01/2017", "01/31/2017", 
				"TestSearchParam", "approver", "payrollManager" , "TestOffice", "submitter"));
    }

    @Test(dataProvider = "employeeRestTemplateDataProvider")
    public void testGetTimesheetDetails(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {
    	
    	UUID timesheetUUId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
    	try {
    		timesheetViewServiceImpl.getTimesheetDetails(timesheetUUId, true);
    	} catch (TimesheetDetailsNotFoundException timesheetDetailsNotFoundException) {}
    	
    	Timesheet timesheet = mock(Timesheet.class);
    	when(timesheetViewRepository.getTimesheetByTimesheetId(timesheetUUId)).thenReturn(timesheet);
    	
    	MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		TimesheetDetails timesheetDetails = mock(TimesheetDetails.class);
		when(timesheetDetails.getTimesheetDate()).thenReturn(new Date("05/05/2017"));
		when(timesheetDetails.getTaskName()).thenReturn("TestTaskName");
		List<TimesheetDetails> timesheetDetailsList = Arrays.asList(timesheetDetails);
		when(timesheetDetailsViewRepository.findByTimesheetId(timesheetUUId)).thenReturn(timesheetDetailsList);
    	when(timesheet.getStartDate()).thenReturn(new Date("01/05/2015"));
    	when(timesheet.getEndDate()).thenReturn(new Date("01/05/2017"));
    	when(timesheet.getOtHours()).thenReturn(10D);
    	when(timesheet.getPtoHours()).thenReturn(20D);
    	when(timesheet.getDtHours()).thenReturn(30D);
    	when(timesheet.getTotalHours()).thenReturn(100D);
    	LookUpType lookUpType = mock(LookUpType.class); 
    	when(timesheet.getLookupType()).thenReturn(lookUpType);
    	when(lookUpType.getValue()).thenReturn("Units");
    	Engagement engagement = mock(Engagement.class);
    	Employee employee = mock(Employee.class);
    	when(timesheet.getEngagement()).thenReturn(engagement);
    	when(engagement.getId()).thenReturn("1212");
    	when(timesheet.getEmployee()).thenReturn(employee);
    	when(employee.getType()).thenReturn("R");
    	when(employee.getId()).thenReturn(111L);
    	
    	String holidayUrl = "http://COMMONSERVICEMANAGEMENT/common/holidays/province/1000?startDate=01/05/2015&endDate=01/05/2017";
		ParameterizedTypeReference<List<HolidayDTO>> respType = new ParameterizedTypeReference<List<HolidayDTO>>() {
		};
		HolidayDTO holidayDTO = mock(HolidayDTO.class);        
		when(holidayDTO.getHolidayDate()).thenReturn(new Date("01/05/2017"));
        List<HolidayDTO> holidayDTOs = Arrays.asList(holidayDTO);
        ResponseEntity<List<HolidayDTO>> resp = new ResponseEntity<>(holidayDTOs, HttpStatus.OK);
		when(restTemplate.exchange(holidayUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), respType)).thenReturn(resp);
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getTimesheetDetails(timesheetUUId, true));
		
		when(timesheetDetailsViewRepository.findByTimesheetId(timesheetUUId)).thenReturn(Arrays.asList());
		when(timesheetViewRepository.getPreviousTimesheetForApprover(anyLong(), anyString(), anyObject())).thenReturn(timesheet);
		when(timesheetViewRepository.getNextTimesheetForApprover(anyLong(), anyString(), anyObject())).thenReturn(timesheet);
		
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getTimesheetDetails(timesheetUUId, true));
		
		String commandUrl = "http://COMMONSERVICEMANAGEMENT/common/engagements/1212/tasks?contractorId=111";
		ParameterizedTypeReference<CommonEngagementDTO> resType = new ParameterizedTypeReference<CommonEngagementDTO>() {
		};
		CommonEngagementDTO commonEngagementDTO = mock(CommonEngagementDTO.class);
		TaskDTO taskDTO = mock(TaskDTO.class);
		when(taskDTO.getTaskName()).thenReturn("TestTaskName");
		List<TaskDTO> taskDTOs = Arrays.asList(taskDTO);
		when(commonEngagementDTO.getTaskDTO()).thenReturn(taskDTOs);
        ResponseEntity<CommonEngagementDTO> res = new ResponseEntity<>(commonEngagementDTO, HttpStatus.OK);
		when(restTemplate.exchange(commandUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), resType)).thenReturn(res);
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getTimesheetDetails(timesheetUUId, false));
		
		when(timesheetViewRepository.getPreviousTimesheetForApprover(anyLong(), anyString(), anyObject())).thenReturn(timesheet);
		when(timesheetViewRepository.getNextTimesheetForSubmitter(anyLong(), anyString(), anyObject())).thenReturn(timesheet);
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getTimesheetDetails(timesheetUUId, false));
		
		when(lookUpType.getValue()).thenReturn("Timestamp");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getTimesheetDetails(timesheetUUId, false));
		
		when(lookUpType.getValue()).thenReturn("Timer");
		TimeDetail timeDetail = mock(TimeDetail.class);
		when(timeDetail.getActiveFlag()).thenReturn(true);
		List<TimeDetail> timeDetails = Arrays.asList(timeDetail);
		when(timesheetDetails.getTimeDetail()).thenReturn(timeDetails);
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getTimesheetDetails(timesheetUUId, false));
		
		when(lookUpType.getValue()).thenReturn("Tim");
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getTimesheetDetails(timesheetUUId, false));
		
		when(engagement.getId()).thenReturn(null);
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getTimesheetDetails(timesheetUUId, false));
		
//		when(timesheetDetailsViewRepository.findByTimesheetId(anyObject())).thenReturn(timesheetDetailsList);
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getTimesheetDetails(timesheetUUId, false));
		
		String contractorHolidayCommandUrl = "http://ENGAGEMENTMANAGEMENT/engagements/1212/holidays?startDate=01/05/2015&endDate=01/05/2017";
		ParameterizedTypeReference<List<HolidayResource>> holidayResponseType = new ParameterizedTypeReference<List<HolidayResource>>() {
		};
		HolidayResource holidayResource = mock(HolidayResource.class);
		List<HolidayResource> holidayResources = Arrays.asList(holidayResource);
        ResponseEntity<List<HolidayResource>> holidayResponse = new ResponseEntity<>(holidayResources, HttpStatus.OK);
		when(restTemplate.exchange(contractorHolidayCommandUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), holidayResponseType)).thenReturn(holidayResponse);
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getTimesheetDetails(timesheetUUId, false));
    }
    
	@Test
	public void testIsValidFileType() {

		MultipartFile file = mock(MultipartFile.class);
		when(file.getOriginalFilename()).thenReturn("TestFile.PDF");
		AssertJUnit.assertTrue(timesheetViewServiceImpl.isValidFileType(file));
		
		when(file.getOriginalFilename()).thenReturn("TestFile.PNG");
		AssertJUnit.assertTrue(timesheetViewServiceImpl.isValidFileType(file));
		
		when(file.getOriginalFilename()).thenReturn("TestFile.JPEG");
		AssertJUnit.assertTrue(timesheetViewServiceImpl.isValidFileType(file));
		
		when(file.getOriginalFilename()).thenReturn("TestFile.JPG");
		AssertJUnit.assertTrue(timesheetViewServiceImpl.isValidFileType(file));
		
		when(file.getOriginalFilename()).thenReturn("TestFile.PF");
		AssertJUnit.assertTrue(!timesheetViewServiceImpl.isValidFileType(file));
	}
	
	@Test
	public void testSaveTimehseetActivityLog() {
		EmployeeProfileDTO employee = mock(EmployeeProfileDTO.class);
		when(employee.getFirstName()).thenReturn("TestFirstName");
		when(employee.getLastName()).thenReturn("TestLastName");
		timesheetViewServiceImpl.saveTimehseetActivityLog(employee, UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00"), "01/01/2017", "TestComment");
	}
	
	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testGetTimesheetsDetail(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		Timesheet timesheet = mock(Timesheet.class);
		List<Timesheet> timesheets = Arrays.asList(timesheet);
		when(timesheetViewRepository.getTimesheetsDetail(anyLong(), anyObject(), anyObject(), anyObject())).thenReturn(timesheets);
		timesheetViewServiceImpl.getTimesheetsDetail("01/05/2016", "01/05/2017");
	}
    
	@Test
	public void testSortingByTimesheetDate() {

		TimesheetDetailsDTO timesheetDetailsDTO1 = mock(TimesheetDetailsDTO.class);
		TimesheetDetailsDTO timesheetDetailsDTO2 = mock(TimesheetDetailsDTO.class);
		when(timesheetDetailsDTO1.getTimesheetDate()).thenReturn("01/05/2017");
		when(timesheetDetailsDTO2.getTimesheetDate()).thenReturn("01/04/2017");
		List<TimesheetDetailsDTO> timesheetDetailsDTOs = Arrays.asList(timesheetDetailsDTO1, timesheetDetailsDTO2);
		timesheetViewServiceImpl.sortingByTimesheetDate(timesheetDetailsDTOs);
	}
	
	@Test
	public void testGetActivityLog() {
		timesheetViewServiceImpl.getActivityLog(UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00"));
		ActivityLog activityLog = mock(ActivityLog.class);
		List<ActivityLog> activityLogs = Arrays.asList(activityLog); 
		when(activityLogViewRepository.findBySourceReferenceIdOrderByUpdatedOnDesc(anyObject())).thenReturn(activityLogs);
		timesheetViewServiceImpl.getActivityLog(UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00"));
	}

	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testGetOfficeLocations(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) {

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		timesheetViewServiceImpl.getOfficeLocations("approver");
	}
	
	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testGetTimesheetDetailsInMobile(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) {
		
		UUID timesheetUUId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		Timesheet timesheet = mock(Timesheet.class);
    	when(timesheetViewRepository.getTimesheetByTimesheetId(timesheetUUId)).thenReturn(timesheet);
    	
		TimesheetDetails timesheetDetails = mock(TimesheetDetails.class);
		when(timesheetDetails.getTimesheetDate()).thenReturn(new Date("05/05/2017"));
		when(timesheetDetails.getTaskName()).thenReturn("TestTaskName");
		List<TimesheetDetails> timesheetDetailsList = Arrays.asList(timesheetDetails);
		when(timesheetDetailsViewRepository.findByTimesheetId(timesheetUUId)).thenReturn(timesheetDetailsList);
    	when(timesheet.getStartDate()).thenReturn(new Date("01/05/2015"));
    	when(timesheet.getEndDate()).thenReturn(new Date("01/05/2017"));
    	when(timesheet.getOtHours()).thenReturn(10D);
    	when(timesheet.getPtoHours()).thenReturn(20D);
    	when(timesheet.getDtHours()).thenReturn(30D);
    	when(timesheet.getTotalHours()).thenReturn(100D);
    	LookUpType lookUpType = mock(LookUpType.class); 
    	when(timesheet.getLookupType()).thenReturn(lookUpType);
    	when(lookUpType.getValue()).thenReturn("Units");
    	Engagement engagement = mock(Engagement.class);
    	Employee employee = mock(Employee.class);
    	when(timesheet.getEngagement()).thenReturn(engagement);
    	when(engagement.getId()).thenReturn("1212");
    	when(timesheet.getEmployee()).thenReturn(employee);
    	when(employee.getType()).thenReturn("R");
    	when(employee.getId()).thenReturn(111L);
    	
    	String holidayUrl = "http://COMMONSERVICEMANAGEMENT/common/holidays/province/1000?startDate=01/05/2015&endDate=01/05/2017";
		ParameterizedTypeReference<List<HolidayDTO>> respType = new ParameterizedTypeReference<List<HolidayDTO>>() {
		};
		HolidayDTO holidayDTO = mock(HolidayDTO.class);        
		when(holidayDTO.getHolidayDate()).thenReturn(new Date("01/05/2017"));
        List<HolidayDTO> holidayDTOs = Arrays.asList(holidayDTO);
        ResponseEntity<List<HolidayDTO>> resp = new ResponseEntity<>(holidayDTOs, HttpStatus.OK);
		when(restTemplate.exchange(holidayUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), respType)).thenReturn(resp);
		when(timesheetDetailsViewRepository.findByTimesheetId(timesheetUUId)).thenReturn(Arrays.asList());
		when(timesheetViewRepository.getPreviousTimesheetForApprover(anyLong(), anyString(), anyObject())).thenReturn(timesheet);
		when(timesheetViewRepository.getNextTimesheetForApprover(anyLong(), anyString(), anyObject())).thenReturn(timesheet);
		
		String commandUrl = "http://COMMONSERVICEMANAGEMENT/common/engagements/1212/tasks?contractorId=111";
		ParameterizedTypeReference<CommonEngagementDTO> resType = new ParameterizedTypeReference<CommonEngagementDTO>() {
		};
		CommonEngagementDTO commonEngagementDTO = mock(CommonEngagementDTO.class);
		TaskDTO taskDTO = mock(TaskDTO.class);
		when(taskDTO.getTaskName()).thenReturn("TestTaskName");
		List<TaskDTO> taskDTOs = Arrays.asList(taskDTO);
		when(commonEngagementDTO.getTaskDTO()).thenReturn(taskDTOs);
        ResponseEntity<CommonEngagementDTO> res = new ResponseEntity<>(commonEngagementDTO, HttpStatus.OK);
		when(restTemplate.exchange(commandUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), resType)).thenReturn(res);
		
		when(timesheetViewRepository.getPreviousTimesheetForApprover(anyLong(), anyString(), anyObject())).thenReturn(timesheet);
		when(timesheetViewRepository.getNextTimesheetForSubmitter(anyLong(), anyString(), anyObject())).thenReturn(timesheet);
		
		when(timesheetDetailsViewRepository.findByTimesheetIdAndTimesheetDate(anyObject(), anyObject())).thenReturn(timesheetDetailsList);
		AssertJUnit.assertNotNull(timesheetViewServiceImpl.getTimesheetDetailsInMobile(timesheetUUId, "01/05/2017", true));
	}

	@Test
	public void testGetComments() throws ParseException {

		UUID id = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		Timesheet timesheet = mock(Timesheet.class);
    	when(timesheet.getStartDate()).thenReturn(new Date("01/05/2015"));
    	when(timesheet.getEndDate()).thenReturn(new Date("01/05/2017"));
    	when(timesheet.getOtHours()).thenReturn(10D);
    	when(timesheet.getPtoHours()).thenReturn(20D);
    	when(timesheet.getDtHours()).thenReturn(30D);
    	when(timesheet.getTotalHours()).thenReturn(100D);
		when(timesheetViewRepository.findOne(id)).thenReturn(timesheet);
		LookUpType lookUpType = mock(LookUpType.class); 
    	when(timesheet.getLookupType()).thenReturn(lookUpType);
    	when(lookUpType.getValue()).thenReturn("Units");
    	Engagement engagement = mock(Engagement.class);
    	Employee employee = mock(Employee.class);
    	when(timesheet.getEngagement()).thenReturn(engagement);
    	when(engagement.getId()).thenReturn("1212");
    	when(timesheet.getEmployee()).thenReturn(employee);
    	when(employee.getType()).thenReturn("R");
    	when(employee.getId()).thenReturn(111L);
    	
    	TimesheetDetails timesheetDetails = mock(TimesheetDetails.class);
		when(timesheetDetails.getTimesheetDate()).thenReturn(new Date("05/05/2017"));
		when(timesheetDetails.getTaskName()).thenReturn("TestTaskName");
		List<TimesheetDetails> timesheetDetailsList = Arrays.asList(timesheetDetails);
		when(timesheetDetailsViewRepository.getTimesheetComments(id)).thenReturn(timesheetDetailsList);
    	
    	Timeoff timeoff = mock(Timeoff.class);
    	List<Timeoff> timeOffs = Arrays.asList(timeoff);
    	when(timeOffRepository.getTimeoffComments(anyLong(), anyObject(), anyObject())).thenReturn(timeOffs);
    	
		timesheetViewServiceImpl.getComments(id);
	}

	@DataProvider(name = "employeeRestTemplateDataProvider")
    public static Iterator<Object[]> employeeRestTemplate() {
    	
        String url = "http://COMMONSERVICEMANAGEMENT/common/employee-profile?emailId=allinall@techmango.net";
        String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
        EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();        
        employeeProfileDTO.setEmployeeId(100L);
        employeeProfileDTO.setProvinceId(1000L);
        employeeProfileDTO.setJoiningDate(new Date("01/04/2017"));
        employeeProfileDTO.setEmployeeType("C");
        ParameterizedTypeReference<EmployeeProfileDTO> responseType = new ParameterizedTypeReference<EmployeeProfileDTO>() {};
        ResponseEntity<EmployeeProfileDTO> responseEntity = new ResponseEntity<>(employeeProfileDTO, HttpStatus.OK);
        HttpHeaders httpHeaders = new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
			}
		};
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {responseType, responseEntity, httpHeaders, url});
        return testData.iterator();
    }
}

