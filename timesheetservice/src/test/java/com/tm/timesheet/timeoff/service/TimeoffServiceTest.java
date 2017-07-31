package com.tm.timesheet.timeoff.service;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.domain.AuditFields;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.repository.ActivityLogRepository;
import com.tm.timesheet.service.TimesheetService;
import com.tm.timesheet.timeoff.domain.PtoAvailable;
import com.tm.timesheet.timeoff.domain.PtoAvailableView;
import com.tm.timesheet.timeoff.domain.Timeoff;
import com.tm.timesheet.timeoff.domain.TimeoffActivityLog;
import com.tm.timesheet.timeoff.domain.TimeoffPto;
import com.tm.timesheet.timeoff.domain.TimeoffRequestDetail;
import com.tm.timesheet.timeoff.exception.TimeoffBadRequestException;
import com.tm.timesheet.timeoff.exception.TimeoffException;
import com.tm.timesheet.timeoff.repository.PtoAvailableRepository;
import com.tm.timesheet.timeoff.repository.TimeoffActivityLogRepository;
import com.tm.timesheet.timeoff.repository.TimeoffPtoRepository;
import com.tm.timesheet.timeoff.repository.TimeoffRepository;
import com.tm.timesheet.timeoff.service.dto.ContractorEmployeeEngagementView;
import com.tm.timesheet.timeoff.service.dto.EntityAttributeDTO;
import com.tm.timesheet.timeoff.service.dto.HolidayDTO;
import com.tm.timesheet.timeoff.service.dto.HolidayResource;
import com.tm.timesheet.timeoff.service.dto.PtoAvailableDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffActivityLogDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffRequestDetailDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffStatus;
import com.tm.timesheet.timeoff.service.impl.TimeoffServiceImpl;
import com.tm.timesheet.timeoff.service.impl.TimeoffServiceImpl.TimeoffType;
import com.tm.timesheet.timeoff.service.mapper.TimeoffMapper;
import com.tm.timesheet.timeoff.web.rest.util.DateUtil;
import com.tm.timesheet.timesheetview.repository.TimesheetViewRepository;
import com.tm.timesheet.web.rest.util.MailManagerUtil;
import com.tm.timesheet.web.rest.util.TimesheetMailAsync;

public class TimeoffServiceTest {

	private static final String AWAITINGAPPROVAL = "Awaiting Approval";
	private static final String MY_TIMEOFF = "my-timeoff";
	
	private TimeoffRepository timeoffRepository;
	private TimeoffPtoRepository timeoffPtoRepository;
	private TimeoffServiceImpl timeoffServiceImpl;
	private TimeoffActivityLogRepository timeoffActivityLogRepository;
	private TimesheetViewRepository timesheetViewRepository;
	private static String timesheetId = "000181d4-2b11-8702-0035-111f1f15f771";
	private static String timeoffId = "000181d4-2b11-8702-0035-111f1f15f772";
	private RestTemplate restTemplate;
	private DiscoveryClient discoveryClient; 
	private PtoAvailableRepository ptoAvailableRepository;
	private ActivityLogRepository activityLogRepository;
	private TimesheetService timesheetService;
	private static final String APPROVED = "APPROVED";
	private static final String REJECTED = "REJECTED";
	private static final String UI_DATE_FORMAT_REQUEST = "MM/dd/yyyy";
	
	public static final String HTTP = "http://";
	private static final String BEARER = "Bearer ";
	private static final String COMTRACK_HOLIDAY_URI = "/common/entity-values/lookup?attribute=TIMEOFF_PTO_TYPE_ID&entity=TIME_OFF";
	private MailManagerUtil mailManagerUtil;
	@InjectMocks
    private EntityAttributeDTO entityAttribute;
	TimesheetMailAsync timesheetMailAsync;
	
	@BeforeMethod
	public void configurationTimeoffServiceTest(){
		timeoffRepository = Mockito.mock(TimeoffRepository.class);
		timesheetViewRepository = Mockito.mock(TimesheetViewRepository.class);
		timeoffActivityLogRepository = Mockito.mock(TimeoffActivityLogRepository.class);
		timeoffPtoRepository = Mockito.mock(TimeoffPtoRepository.class);
		ptoAvailableRepository = Mockito.mock(PtoAvailableRepository.class);
		restTemplate = Mockito.mock(RestTemplate.class);
		discoveryClient = Mockito.mock(DiscoveryClient.class);
		mailManagerUtil = Mockito.mock(MailManagerUtil.class);
		activityLogRepository = Mockito.mock(ActivityLogRepository.class);
		//timesheetService = Mockito.mock(TimesheetService.class);
		
		timeoffServiceImpl = new TimeoffServiceImpl(timeoffRepository, ptoAvailableRepository, timeoffActivityLogRepository,
				restTemplate, discoveryClient, timesheetViewRepository, activityLogRepository, timeoffPtoRepository, 
				mailManagerUtil,timesheetMailAsync);
	}
	
	@BeforeClass
	public static void beforeClass() {
	    System.setProperty("weekoff.startday", "SUNDAY");
	    System.setProperty("weekoff.endday", "SATURDAY");
	}

	@AfterClass
	public static void afterClass() {
		System.clearProperty("weekoff.startday");
	    System.clearProperty("weekoff.endday");
	}
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "getAllTimeoffWithDate", description = "")
	public void getAllTimeoffWithDate(Page<Timeoff> pageTimeoff, Pageable pageable, String group) throws Exception {
		String startDate = "05/05/2014";
		String endDate = "06/05/2017";
		String status = "all";
		Long userid = (long) 1;
		String[] statusAllArray = new String[] { AWAITINGAPPROVAL, APPROVED, REJECTED };
		when(timeoffRepository.getMyTimeoffList(userid, statusAllArray, checkconvertStringToISODate(startDate),
				checkconvertStringToISODate(endDate), pageable,null)).thenReturn(pageTimeoff);
		timeoffServiceImpl.getMyTimeoffList(pageable, startDate, endDate, status, userid,null);
	}
	
	public static Date checkconvertStringToISODate(String date) throws ParseException {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date today = df.parse(date);
		return today;
	}
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "getTimeoffStatusCountWithDateParam", description = "")
	public void getTimeoffStatusCountWithDateParam(TimeoffStatus timeoffStatus, String group) throws Exception {
		String startDate = "05/05/2014";
		String endDate = "05/05/2017";
		Long userid = (long) 1;
		String searchParam=null;
		when(timeoffRepository.getTimeoffStatusCountWithDate(APPROVED, userid, checkconvertStringToISODate(startDate),
				checkconvertStringToISODate(endDate),searchParam)).thenReturn(timeoffStatus.getApprovalCount());
		when(timeoffRepository.getTimeoffStatusCountWithDate(AWAITINGAPPROVAL, userid,
				checkconvertStringToISODate(startDate), checkconvertStringToISODate(endDate),searchParam))
						.thenReturn(timeoffStatus.getAwaitingApprovalCount());
		when(timeoffRepository.getTimeoffStatusCountWithDate(REJECTED, userid, checkconvertStringToISODate(startDate),
				checkconvertStringToISODate(endDate),searchParam)).thenReturn(timeoffStatus.getRejectedCount());
		timeoffServiceImpl.getMyTimeoffStatusCount(userid, startDate, endDate,searchParam);
	}
	
	private EmployeeProfileDTO getEmployeeDetails() {
		EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();
		employeeProfileDTO.setEmployeeId(1l);
		employeeProfileDTO.setFirstName("Mukesh");
		employeeProfileDTO.setLastName("Ambani");
		employeeProfileDTO.setReportingManagerId(1l);
		employeeProfileDTO.setReportingManagerName("Mukesh Ambani");
		employeeProfileDTO.setPrimaryEmailId("mukeshambani@test.com");
		employeeProfileDTO.setReportingManagerEmailId("mukeshambani@test.com");
		employeeProfileDTO.setProvinceId(10000L);
		return employeeProfileDTO;
	}
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "getMyPtoTimeoffDetails", description = "")
	public void getMyPtoTimeoffDetails(List<PtoAvailable> ptoAvailableDetails) {

		EmployeeProfileDTO employeeProfileDTO = getEmployeeDetails();
		
		when(ptoAvailableRepository.getMyPtoTimeoffDetails(employeeProfileDTO.getEmployeeId()))
				.thenReturn(ptoAvailableDetails);

		timeoffServiceImpl.getMyPtoTimeoffDetails(employeeProfileDTO);
		
		timeoffServiceImpl.getMyPtoTimeoffDetails(employeeProfileDTO,null);
	}
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "getMyTeamTimeoff", description = "")
	public void getMyTeamTimeoff(Page<Timeoff> pageTimeoff, Pageable pageable, String group) throws Exception {
		String startDate = "05/05/2014";
		String endDate = "06/05/2017";
		String status = "all";
		Long userid = (long) 1;
		String[] statusAllArray = new String[] { AWAITINGAPPROVAL, APPROVED, REJECTED };
		when(timeoffRepository.getMyTeamTimeoff(userid, statusAllArray, checkconvertStringToISODate(startDate),
				checkconvertStringToISODate(endDate), pageable,null)).thenReturn(pageTimeoff);
		timeoffServiceImpl.getMyTeamTimeoff(pageable, startDate, endDate, status, userid,null);
	}
	
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "getMyTeamTimeoffStatusCount", description = "")
	public void getMyTeamTimeoffStatusCount(TimeoffStatus timeoffStatus, String group) throws Exception {
		String startDate = "05/05/2014";
		String endDate = "05/05/2017";
		Long userid = (long) 1;
		String searchParam=null;
		when(timeoffRepository.getMyTeamTimeoffStatusCountWithDate(APPROVED, userid, checkconvertStringToISODate(startDate),
				checkconvertStringToISODate(endDate),searchParam)).thenReturn(timeoffStatus.getApprovalCount());
		when(timeoffRepository.getMyTeamTimeoffStatusCountWithDate(AWAITINGAPPROVAL, userid,
				checkconvertStringToISODate(startDate), checkconvertStringToISODate(endDate),searchParam))
						.thenReturn(timeoffStatus.getAwaitingApprovalCount());
		when(timeoffRepository.getMyTeamTimeoffStatusCountWithDate(REJECTED, userid, checkconvertStringToISODate(startDate),
				checkconvertStringToISODate(endDate),searchParam)).thenReturn(timeoffStatus.getRejectedCount());
		timeoffServiceImpl.getMyTeamTimeoffStatusCount(userid, startDate, endDate,searchParam);
	}
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "createTimeOff", expectedExceptions = TimeoffException.class, description = "")
	public void createTimeoffException1(TimeoffDTO timeoffDTO,Timeoff timeoff) throws ParseException  {
		when(ptoAvailableRepository.findOneByEmployeeId(1l)).thenReturn(null);
		EmployeeProfileDTO employeeProfileDTO = getEmployeeDetails();
		String applicationLiveDate = "applicationLiveDate";
		String timeoffRequestHours = "timeoffRequestHours";
		ReflectionTestUtils.setField(timeoffServiceImpl, applicationLiveDate, "02/02/2017" );
		ReflectionTestUtils.setField(timeoffServiceImpl, timeoffRequestHours, "8" );
		ReflectionTestUtils.setField(timeoffServiceImpl, "SUNDAY", "Sunday" );
		ReflectionTestUtils.setField(timeoffServiceImpl, "SATURDAY", "Saturday" );
		
		when(timeoffRepository.save(timeoff)).thenReturn(timeoff);
				
		timeoffServiceImpl.createTimeoff(timeoffDTO, employeeProfileDTO);
	}
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "createTimeOff", description = "")
	public void testCreateTimesheetTimeoff(TimeoffDTO timeoffDTO,Timeoff timeoff) throws ParseException {
		
		when(ptoAvailableRepository.findOneByEmployeeId(1l)).thenReturn(null);
		//EmployeeProfileDTO employeeProfileDTO = getEmployeeDetails();
		String applicationLiveDate = "applicationLiveDate";
		String timeoffRequestHours = "timeoffRequestHours";
		ReflectionTestUtils.setField(timeoffServiceImpl, applicationLiveDate, "02/02/2017" );
		ReflectionTestUtils.setField(timeoffServiceImpl, timeoffRequestHours, "8" );
		ReflectionTestUtils.setField(timeoffServiceImpl, "SUNDAY", "Sunday" );
		ReflectionTestUtils.setField(timeoffServiceImpl, "SATURDAY", "Saturday" );
		
		when(timeoffRepository.save(timeoff)).thenReturn(timeoff);
		
		String url = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/100";
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
        EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();        
        employeeProfileDTO.setEmployeeId(100L);
        employeeProfileDTO.setProvinceId(1000L);
        employeeProfileDTO.setJoiningDate(new Date("01/04/2017"));
        employeeProfileDTO.setEmployeeType("C");
        employeeProfileDTO.setReportingManagerId(100L);
        ParameterizedTypeReference<EmployeeProfileDTO> empResponseType = new ParameterizedTypeReference<EmployeeProfileDTO>() {};
        ResponseEntity<EmployeeProfileDTO> responseEntity = new ResponseEntity<>(employeeProfileDTO, HttpStatus.OK);
        HttpHeaders httpHeaders = new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
			}
		};
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		request.setAttribute("token", requestedToken);
		
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), empResponseType)).thenReturn(responseEntity);
		
		String holidayUrl = "http://COMMONSERVICEMANAGEMENT/common/entity-values/lookup?attribute=TIMEOFF_PTO_TYPE_ID&entity=TIME_OFF";
		ParameterizedTypeReference<List<EntityAttributeDTO>> responseType = new ParameterizedTypeReference<List<EntityAttributeDTO>>() {
		};
		EntityAttributeDTO entityAttributeDTO = new EntityAttributeDTO();
		entityAttributeDTO.setAttributeValue(TimeoffType.PTO.name());
		List<EntityAttributeDTO> entityAttributeDTOs = new ArrayList<>();
		entityAttributeDTOs.add(entityAttributeDTO);
        ResponseEntity<List<EntityAttributeDTO>> resp = new ResponseEntity<>(entityAttributeDTOs, HttpStatus.OK);

		when(restTemplate.exchange(holidayUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(resp);
		
		PtoAvailable ptoAvailable = Mockito.mock(PtoAvailable.class);
		when(ptoAvailable.getAllotedHours()).thenReturn(100D);
		when(ptoAvailableRepository.findOneByEmployeeId(Mockito.anyLong())).thenReturn(ptoAvailable);
		when(ptoAvailableRepository.findOneByEmployeeIdAndEngagementId(Mockito.anyLong(), Mockito.anyObject())).thenReturn(ptoAvailable);
		
		Timeoff appliedTimeoff = prepareTimeoffData(getTimeOff());
		appliedTimeoff.setStartDate(new Date("02/10/2017"));
		appliedTimeoff.setEndDate(new Date("02/12/2017"));
		when(timeoffRepository.getTimeoffByRequestDate(anyLong(), anyObject(), anyObject(), anyString())).thenReturn(appliedTimeoff);
		
		String holidayDTOUrl = "http://COMMONSERVICEMANAGEMENT/common/holidays/province/1000?startDate=02/10/2017&endDate=02/10/2017";
		ParameterizedTypeReference<List<HolidayDTO>> respType = new ParameterizedTypeReference<List<HolidayDTO>>() {
		};
		HolidayDTO holidayDTO = mock(HolidayDTO.class);        
		when(holidayDTO.getHolidayDate()).thenReturn(new Date("01/05/2017"));
        List<HolidayDTO> holidayDTOs = Arrays.asList(holidayDTO);
        ResponseEntity<List<HolidayDTO>> holidayResp = new ResponseEntity<>(holidayDTOs, HttpStatus.OK);
		when(restTemplate.exchange(holidayDTOUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), respType)).thenReturn(holidayResp);
		
		timeoffServiceImpl.createTimesheetTimeoff(Arrays.asList(timeoffDTO, getTimeOff()), employeeProfileDTO);
		
		appliedTimeoff.setStartDate(new Date("02/08/2017"));
		appliedTimeoff.setEndDate(new Date("02/10/2017"));
		timeoffServiceImpl.createTimesheetTimeoff(Arrays.asList(timeoffDTO, getTimeOff()), employeeProfileDTO);
		
		appliedTimeoff.setEndDate(new Date("02/11/2017"));
		timeoffServiceImpl.createTimesheetTimeoff(Arrays.asList(timeoffDTO, getTimeOff()), employeeProfileDTO);
	}
	
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "GetMyTeamPtoTimeoffDetails", description = "")
	public void GetMyTeamPtoTimeoffDetails(Timeoff timeoff,List<PtoAvailable> ptoAvailableDetails) {
		Long employeeId = 101L;
		String engagementId = "000181d4-2b11-8702-0035-511f1f15f772";
		when(timeoffRepository.findOne(UUID.fromString(timeoffId))).thenReturn(timeoff);
		when(ptoAvailableRepository.getMyPtoTimeoffDetails(employeeId)).thenReturn(ptoAvailableDetails);
		when(ptoAvailableRepository.findOneByEmployeeIdAndEngagementIdList(employeeId, UUID.fromString(engagementId))).thenReturn(ptoAvailableDetails);
		when(ptoAvailableRepository.getMyPtoTimeoffDetails(employeeId)).thenReturn(ptoAvailableDetails);
		when(ptoAvailableRepository.findOneByEmployeeIdAndEngagementIdList(employeeId,UUID.fromString(engagementId))).thenReturn(ptoAvailableDetails);
		
		timeoffServiceImpl.getMyTeamPtoTimeoffDetails(employeeId.toString(), timeoffId, engagementId);
	}
	
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "getMyTeamTimeoffAvaliableList", description = "")
	public void getMyTeamTimeoffAvaliableList(Page<PtoAvailableView> ptoAvaliables,Pageable pageable) {
		Long employeeId = 101L;
		String searchParam=null;
		
		when(ptoAvailableRepository.getMyTeamTimeoffAvaliableList(employeeId, pageable)).thenReturn(ptoAvaliables);
		
		timeoffServiceImpl.getMyTeamTimeoffAvaliableList(employeeId,pageable,searchParam);
	}
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "getMyTimeoff", description = "", expectedExceptions = {NullPointerException.class})
	public void getMyTimeoff(Timeoff timeoff,List<TimeoffActivityLog> timeoffActivityLogs) {
		String navigationScreen = null;
		
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
			
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(timeoffRepository.findOne(UUID.fromString(timeoffId))).thenReturn(timeoff);
		when(timeoffActivityLogRepository.findByTimeoffIdQuery(timeoff.getId())).thenReturn(timeoffActivityLogs);
		
		String holidayUrl = "http://COMMONSERVICEMANAGEMENT/common/holidays/province/1000?startDate=01/04/2017&endDate=01/05/2017";
		ParameterizedTypeReference<List<HolidayDTO>> respType = new ParameterizedTypeReference<List<HolidayDTO>>() {
		};
		HolidayDTO holidayDTO = mock(HolidayDTO.class);        
		when(holidayDTO.getHolidayDate()).thenReturn(new Date("01/05/2017"));
        List<HolidayDTO> holidayDTOs = Arrays.asList(holidayDTO);
        ResponseEntity<List<HolidayDTO>> resp = new ResponseEntity<>(holidayDTOs, HttpStatus.OK);
		when(restTemplate.exchange(holidayUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), respType)).thenReturn(resp);
		
		timeoffServiceImpl.getMyTimeoff(timeoffId, navigationScreen);
	}
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "deleteMyTimeoff", description = "")
	public void deleteMyTimeoff(Timeoff timeoff) {
		
		when(timeoffRepository.findOne(UUID.fromString(timeoffId))).thenReturn(timeoff);
		
		when(ptoAvailableRepository.updateByDraftHours("",Double.valueOf(0))).thenReturn(null);
		
		timeoffServiceImpl.deleteMyTimeoff(timeoffId);

	}
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "getTimeoffDates", description = "")
	public void getTimeoffDates(String startDate, String endDate,List<Timesheet> timesheet) {
		
		ReflectionTestUtils.setField(timeoffServiceImpl, "SUNDAY", "Sunday" );
		ReflectionTestUtils.setField(timeoffServiceImpl, "SATURDAY", "Saturday" );
		
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
			
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		String holidayUrl = "http://COMMONSERVICEMANAGEMENT/common/holidays/province/1000?startDate=02/01/2017&endDate=02/07/2017";
		ParameterizedTypeReference<List<HolidayDTO>> respType = new ParameterizedTypeReference<List<HolidayDTO>>() {
		};
		HolidayDTO holidayDTO = mock(HolidayDTO.class);        
		when(holidayDTO.getHolidayDate()).thenReturn(new Date("02/05/2017"));
        List<HolidayDTO> holidayDTOs = Arrays.asList(holidayDTO);
        ResponseEntity<List<HolidayDTO>> resp = new ResponseEntity<>(holidayDTOs, HttpStatus.OK);
		when(restTemplate.exchange(holidayUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), respType)).thenReturn(resp);
		
		when(timesheetViewRepository.getTimesheetsDetailByStatus(1l, DateUtil.convertStringToDate(startDate), 
				DateUtil.convertStringToDate(endDate), null)).thenReturn(timesheet);
		
		AssertJUnit.assertNotNull(timeoffServiceImpl.getTimeoffDates(employeeProfileDTO, startDate, endDate, null));
		
		TimeoffPto timeoffPto = Mockito.mock(TimeoffPto.class);
		when(timeoffPto.getMax_pto_hrs()).thenReturn(8D);
		when(timeoffPtoRepository.findByEmployeeIdAndEngagementId(anyLong(), (UUID)anyObject())).thenReturn(timeoffPto);
		
		String contractorUrl = "http://COMMONSERVICEMANAGEMENT/common/engagements/employeeengagementdetails?userId=100&engagementId=000181d4-2b11-8702-0035-111f1f15f771";
		ParameterizedTypeReference<ContractorEmployeeEngagementView> contractorResponseType = new ParameterizedTypeReference<ContractorEmployeeEngagementView>() {
		};
		ContractorEmployeeEngagementView contractorEmployeeEngagementView = mock(ContractorEmployeeEngagementView.class);
		when(contractorEmployeeEngagementView.getStartDay()).thenReturn(ContractorEmployeeEngagementView.day.Mon);
		when(contractorEmployeeEngagementView.getEndDay()).thenReturn(ContractorEmployeeEngagementView.day.Sat);
		when(contractorEmployeeEngagementView.getEmplEffStartDate()).thenReturn(new Date("03/10/2017"));
		when(contractorEmployeeEngagementView.getEmplEffEndDate()).thenReturn(new Date("06/10/2017"));
        ResponseEntity<ContractorEmployeeEngagementView> contractorResp = new ResponseEntity<>(contractorEmployeeEngagementView, HttpStatus.OK);

		when(restTemplate.exchange(contractorUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), contractorResponseType)).thenReturn(contractorResp);
		
		AssertJUnit.assertNotNull(timeoffServiceImpl.getTimeoffDates(employeeProfileDTO, startDate, endDate, "000181d4-2b11-8702-0035-111f1f15f771"));
	}
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "getMyTeamPtoTimeoffDetails", description = "")
	public void getMyTeamPtoTimeoffDetails(String employeeId, Timeoff timeoff, String engagementId) {
		List<PtoAvailable> ptoAvailableList = Mockito.anyList();
		Long empl = Long.parseLong(employeeId);
		when(timeoffRepository.findOne(UUID.fromString(timeoffId))).thenReturn(timeoff);
		when(ptoAvailableRepository.getMyPtoTimeoffDetails(empl)).thenReturn(ptoAvailableList);
		when(ptoAvailableRepository.findOneByEmployeeIdAndEngagementIdList(empl, UUID.fromString(engagementId))).thenReturn(ptoAvailableList);
		when(ptoAvailableRepository.getMyPtoTimeoffDetails(empl)).thenReturn(ptoAvailableList);
		when(ptoAvailableRepository.findOneByEmployeeIdAndEngagementIdList(empl,UUID.fromString(engagementId))).thenReturn(ptoAvailableList);
		
		timeoffServiceImpl.getMyTeamPtoTimeoffDetails(employeeId.toString(), timeoffId, engagementId);
	}
	
	@Test
	public void TestGetMyTeamTimeoffStatusCount() throws ParseException {
		Long userId = 124578L;
		String startDate = "05/05/2017";
		String endDate = "09/05/2017";
		String searchParam = "SMI";
		Long approvedStatusCnt = 10L;
		Long submittedStatusCnt = 15L;
		Long rejectedStatusCnt = 5L;
		Date timeoffStartDate = DateUtil.checkconvertStringToISODate(startDate);
		Date timeoffEndDate = DateUtil.checkconvertStringToISODate(endDate);
		when(timeoffRepository.getMyTeamTimeoffStatusCountWithDate(APPROVED, userId, timeoffStartDate, timeoffEndDate, searchParam)).thenReturn(approvedStatusCnt);
		when(timeoffRepository.getMyTeamTimeoffStatusCountWithDate(AWAITINGAPPROVAL, userId, timeoffStartDate, timeoffEndDate, searchParam)).thenReturn(submittedStatusCnt);
		when(timeoffRepository.getMyTeamTimeoffStatusCountWithDate(REJECTED, userId, timeoffStartDate, timeoffEndDate, searchParam)).thenReturn(rejectedStatusCnt);
		timeoffServiceImpl.getMyTeamTimeoffStatusCount(userId, startDate, endDate, searchParam);
	}
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "createPTOAvailabel", description = "")
	public void createPTOAvailabel(Long employeeId, String engagementId, PtoAvailable ptoAvailable){
		PtoAvailableDTO ptoAvailableDTO = new PtoAvailableDTO();
		ptoAvailableDTO.setEmployeeId(employeeId);
		ptoAvailableDTO.setAllotedHours("0.0");
		ptoAvailableDTO.setApprovedHours("0.0");
		ptoAvailableDTO.setAvailedHours("0.0");
		ptoAvailableDTO.setBalanceHours("0.0");
		ptoAvailableDTO.setDraftHours("0.0");
		ptoAvailableDTO.setRequestedHours("0.0");
		
		when(ptoAvailableRepository.findByEmployeeIdAndEngagementId(employeeId, UUID.fromString(engagementId))).thenReturn(ptoAvailable);
		when(ptoAvailableRepository.save(ptoAvailable)).thenReturn(ptoAvailable);
		timeoffServiceImpl.createPTOAvailable(ptoAvailableDTO);
	}
	
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "removeAndUpdateTimesheetAppliedTimeoff", description = "")
	public void removeAndUpdateTimesheetAppliedTimeoff(Timeoff timeoff, PtoAvailable ptoAvailable){
		String ptoTypeName = "welcome";
		Long employeeId = 101L;
		List<Timeoff> timeoffList = new ArrayList<>();
		timeoffList.add(timeoff);
		when(timeoffRepository.findByPtoTypeNameAndPtoRequestDetailTimesheetId(ptoTypeName, UUID.fromString(timesheetId))).thenReturn(timeoffList);
		when(timeoffRepository.findByPtoRequestDetailTimesheetId(UUID.fromString(timesheetId))).thenReturn(timeoffList);
		when(ptoAvailableRepository.findOneByEmployeeId(employeeId)).thenReturn(ptoAvailable);
		doNothing().when(timeoffRepository).delete(timeoffList);
		when(ptoAvailableRepository.updateByDraftHours("123",- 100.00)).thenReturn(100);
		when(ptoAvailableRepository.updateByRequestedHours("123",- 100.00)).thenReturn(100);
		timeoffServiceImpl.removeAndUpdateTimesheetAppliedTimeoff(ptoTypeName, UUID.fromString(timesheetId));
		
		timeoffServiceImpl.removeAndUpdateTimesheetAppliedTimeoff(null, UUID.fromString(timesheetId));
	}
	 
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "getProAccural", description = "")
	public void getProAccural(List<PtoAvailable> ptoAvailable){
		Long employeeId = 101L;
		String startDate =  "05/02/2017";
		when(ptoAvailableRepository.findAllEmployeeIdAndEngagementId(employeeId,DateUtil.convertStringToDate(startDate))).thenReturn(ptoAvailable);
		timeoffServiceImpl.getPTOAccural(startDate, employeeId);
	}
	
	@Test
	public void updaeTimesheetTimeoffStatus() throws ParseException{
		EmployeeProfileDTO employeeProfileDTO = getEmployeeDetails();
		TimeoffDTO timeoffDTO = getTimeOff();
		Timeoff timeoff = prepareTimeoffData(timeoffDTO);
		List<Timeoff> timeoffList = new ArrayList<>();
		timeoffList.add(timeoff);
		timeoffDTO.setTimesheetId(timesheetId); 
		List<TimeoffDTO> timeoffDTOList = new ArrayList<>();
		timeoffDTOList.add(timeoffDTO);
		when(timeoffRepository.findByPtoRequestDetailTimesheetId(UUID.fromString(timesheetId))).thenReturn(timeoffList);
		when(timeoffRepository.save(timeoff)).thenReturn(timeoff);
		timeoffServiceImpl.updateTimesheetTimeoffStatus(timeoffDTOList, employeeProfileDTO);
	}
	
	private static Timeoff prepareTimeoffData(TimeoffDTO timeoffDTO) throws ParseException {
		Timeoff timeoff = new Timeoff();
		AuditFields auditField = new AuditFields();
		timeoff.setPtoTypeName(timeoffDTO.getPtoTypeName());
		timeoff.setTotalHours(Double.parseDouble(timeoffDTO.getTotalHours()));
		timeoff.setStatus(timeoffDTO.getStatus());
		timeoff.setId(UUID.fromString(timesheetId));
		timeoff.setStatus(timeoffDTO.getStatus());
		timeoff.setTotalHours(Double.parseDouble(timeoffDTO.getTotalHours()));
		timeoff.setEngagementId(timeoffDTO.getEngagementId());
		timeoff.setBillableIndFlag(true);
		timeoff.setComments("TestComments");
		timeoff.setEmployeeId(100L);
		timeoff.setEmployeeName("TestEmployee");
		
		List<TimeoffRequestDetail> timeoffRequestDetailList = new ArrayList<TimeoffRequestDetail>();
		timeoffDTO.getPtoRequestDetailDTO().stream().forEach(a -> 
					timeoffRequestDetailList.add(TimeoffMapper.INSTANCE.timeoffRequestDetailDTOToTimeoffRequestDetail(a)));
		timeoff.setPtoRequestDetail(timeoffRequestDetailList);
		return timeoff;
	}
	
	private static EmployeeProfileDTO getEmployeeProfileDTO(){
		EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();
		employeeProfileDTO.setEmployeeId(101L);
		employeeProfileDTO.setFirstName("SMI");
		employeeProfileDTO.setLastName("SMI2"); 
		employeeProfileDTO.setReportingManagerId(102L);
		employeeProfileDTO.setReportingManagerName("Joburds");
		return employeeProfileDTO;
	}
	
	@Test
	public void delete() throws ParseException{
		String timeoffId = "000181d4-2b11-8702-0035-111f1f15f771";
		TimeoffDTO timeoffDTO = getTimeOff();
		Timeoff timeoff = prepareTimeoffData(timeoffDTO);
		when(timeoffRepository.findOne(UUID.fromString(timeoffId))).thenReturn(timeoff);
		doNothing().when(timeoffRepository).delete(UUID.fromString(timeoffId));
		timeoffServiceImpl.deleteMyTimeoff(timeoffId);
	}
	
	@Test(dataProviderClass = TimeoffTestDataProvider.class, dataProvider = "testCreateTimeoff", description = "", expectedExceptions = {TimeoffBadRequestException.class})
	public void testCreateTimeoff(Timesheet timesheet) throws ParseException {
		TimeoffDTO timeoffDTO = getTimeOff();
		timeoffDTO.setStartDate("05/07/2017");
		timeoffDTO.setEndDate("06/06/2017");
		Timeoff timeoff = prepareTimeoffData(timeoffDTO);
		EmployeeProfileDTO employeeProfileDTO = getEmployeeProfileDTO();
		List<Timesheet> timesheetList = new ArrayList<>();
		timesheetList.add(timesheet);
		
		List<TimeoffRequestDetailDTO> timeoffRequestDetailList=new ArrayList<>();
		TimeoffRequestDetailDTO timeoffRequestDetail=new TimeoffRequestDetailDTO();
		timeoffRequestDetail.setRequestedDate("05/07/2017");
		timeoffRequestDetail.setRequestedHours("8");
		timeoffRequestDetail.setStatus("SUBMITTED");
		timeoffRequestDetailList.add(timeoffRequestDetail);
		timeoffDTO.setPtoRequestDetailDTO(timeoffRequestDetailList);
		
		Date timeoffStartDate = new Date();
		Date timeoffEndDate = new Date();
		String name = "applicationLiveDate";
		String sundayName = "SUNDAY";
		String saturdayName = "SATURDAY";
		String timeoffRequestHours = "timeoffRequestHours";
		ReflectionTestUtils.setField(timeoffServiceImpl, name, "05/07/2017" );
		ReflectionTestUtils.setField(timeoffServiceImpl, sundayName, "sunday" );
		ReflectionTestUtils.setField(timeoffServiceImpl, saturdayName, "saturday" );
		ReflectionTestUtils.setField(timeoffServiceImpl, timeoffRequestHours, "8" );
		
		when(timeoffRepository.save(timeoff)).thenReturn(timeoff);
		when(timesheetViewRepository.getTimesheetsDetailTimeoff(employeeProfileDTO.getEmployeeId(), UUID.fromString(timesheetId),
				timeoffStartDate, timeoffEndDate)).thenReturn(timesheetList);
		
		timeoffServiceImpl.createTimeoff(timeoffDTO, employeeProfileDTO);
	}
	 
	@Test
	public void checkGetMyPtoTimeoffDetails(){
		when(ptoAvailableRepository.getMyPtoTimeoffDetails(101L)).thenReturn(getPtoAvailable());
		EmployeeProfileDTO employeeProfileDTO = getEmployeeProfileDTO();
		timeoffServiceImpl.getMyPtoTimeoffDetails(employeeProfileDTO, null);
		assertEquals(ptoAvailableRepository.getMyPtoTimeoffDetails(101L).get(0).getEmployeeId(), getPtoAvailable().get(0).getEmployeeId());
	}
	
	private static Page<PtoAvailableView> getPtoAvaliables(){
		PtoAvailableView ptoAvailableView = new PtoAvailableView();
		ptoAvailableView.setAllotedHours(0.0);
		ptoAvailableView.setApprovedHours(0.0);
		ptoAvailableView.setAvailedHours(0.0);
		ptoAvailableView.setBalanceHours(0.0);
		ptoAvailableView.setDraftHours(0.0);
		ptoAvailableView.setRequestedHours(0.0);
		ptoAvailableView.setEmployeeId(101L);
		
		List<PtoAvailableView> ptoAvailableViewList = new ArrayList<>();
		ptoAvailableViewList.add(ptoAvailableView);
		return new PageImpl<>(ptoAvailableViewList, null, ptoAvailableViewList.size());
	}
	
	private static List<PtoAvailable> getPtoAvailable(){
		PtoAvailable ptoAvailable = new PtoAvailable();
		ptoAvailable.setAllotedHours(0.0);
		ptoAvailable.setApprovedHours(0.0);
		ptoAvailable.setAvailedHours(0.0);
		ptoAvailable.setBalanceHours(0.0);
		ptoAvailable.setDraftHours(0.0);
		ptoAvailable.setRequestedHours(0.0);
	 
		List<PtoAvailable> ptoAvailableList = new ArrayList<>();
		ptoAvailableList.add(ptoAvailable);
		return ptoAvailableList;
	}
	
	@Test
	public void checkGetMyTimeoffStatusCount() throws ParseException{
		Long approvedStatusCnt = 0L;
		Long submittedStatusCnt = 0L;
		Long rejectedStatusCnt = 0L;
		Long userId = 1001L;
		String searchParam = null;
		String startDate = "01/02/2017";
		String endDate = "01/02/2017";
		Date timeoffStartDate = DateUtil.checkconvertStringToISODate(startDate);
		Date timeoffEndDate = DateUtil.checkconvertStringToISODate(endDate);
		when(timeoffRepository.getTimeoffStatusCountWithDate(APPROVED, userId, timeoffStartDate,timeoffEndDate, searchParam)).thenReturn(approvedStatusCnt);
		when(timeoffRepository.getTimeoffStatusCountWithDate(AWAITINGAPPROVAL, userId,timeoffStartDate, timeoffEndDate, searchParam)).thenReturn(submittedStatusCnt);
		when(timeoffRepository.getTimeoffStatusCountWithDate(REJECTED, userId, timeoffStartDate,timeoffEndDate, searchParam)).thenReturn(rejectedStatusCnt);
		timeoffServiceImpl.getMyTimeoffStatusCount(userId, startDate, endDate, searchParam);
		assertEquals(timeoffRepository.getTimeoffStatusCountWithDate(APPROVED, userId, timeoffStartDate,timeoffEndDate, searchParam), 
				approvedStatusCnt);
		assertEquals(timeoffRepository.getTimeoffStatusCountWithDate(AWAITINGAPPROVAL, userId,timeoffStartDate, timeoffEndDate, searchParam),
				submittedStatusCnt);
		assertEquals(timeoffRepository.getTimeoffStatusCountWithDate(REJECTED, userId, timeoffStartDate,timeoffEndDate, searchParam),
				rejectedStatusCnt);
			
	}
	
	@Test
	public void checkGetMyTeamTimeoffAvaliableList(){
		Pageable pageable = null;
		String searchParam = null;
		Long employeeId = 101L;
		when(ptoAvailableRepository.getMyTeamTimeoffAvaliableList(employeeId, pageable)).thenReturn(getPtoAvaliables());
		timeoffServiceImpl.getMyTeamTimeoffAvaliableList(employeeId, pageable, searchParam);
		assertEquals(ptoAvailableRepository.getMyTeamTimeoffAvaliableList(employeeId, pageable).getTotalElements(), getPtoAvaliables().getTotalElements());
	}
	
	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void checkUpdateTimeoffStatus(ParameterizedTypeReference<EmployeeProfileDTO> responseType, 
			ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		String urlNew = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/100";
		when(restTemplate.exchange(urlNew, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		String lastUpdatedDate = "01/02/2017";
		String employeeId = "101";
		UUID timeoffid = UUID.fromString(timeoffId);
		String status = APPROVED;
		String userName = "Rajesh";
		Long userId = 111011L;
		TimeoffDTO timeoffDTO = getTimeoffDTO();
		timeoffDTO.setStatus(APPROVED);
		timeoffDTO.setTimeoffId(timeoffid);
		List<TimeoffDTO> timeoffDTOList = new ArrayList<>();
		timeoffDTOList.add(timeoffDTO);
		EmployeeProfileDTO employeeProfileDTO = getEmployeeProfileDTO();
		timeoffDTO.setEmployeeId("100");
		Timeoff timeoff = prepareTimeoff();
		timeoff.setPtoRequestDetail(getTimeoffRequestDetail());
		
		doNothing().when(timeoffRepository).updateTimeoffStatus(lastUpdatedDate, employeeId, timeoffid, status,userName, userId);
		when(timeoffRepository.findOne(timeoffid)).thenReturn(timeoff);
		when(timeoffRepository.save(timeoff)).thenReturn(timeoff);
		timeoffServiceImpl.updateTimeoffStatus(timeoffDTOList, employeeProfileDTO);
		assertEquals(timeoffRepository.findOne(timeoffid), timeoff);
		assertEquals(timeoffRepository.save(timeoff), timeoff);
	}
	
	private static EntityAttributeDTO getEntityAttributeDTO(){
		EntityAttributeDTO entityAttributeDTO = new EntityAttributeDTO();
	    entityAttributeDTO.setAttributeId("124578963");
	    entityAttributeDTO.setAttributeName("check me");
	    entityAttributeDTO.setAttributeValue("never give up");
	    entityAttributeDTO.setSequenceNumber(1);
	    return entityAttributeDTO;
	}

	private static List<Timesheet> getAllTimeSheet(){
		Timesheet timesheet = new Timesheet();
		
		timesheet.setId(UUID.fromString(timesheetId));
		timesheet.setStartDate(new Date());
		timesheet.setEndDate(new Date());
		List<Timesheet> timesheetList = new ArrayList<>();
		timesheetList.add(timesheet);
		return timesheetList;
	}
	
	private static TimeoffDTO getTimeoffDTO(){
		TimeoffDTO timeoffDTO = new TimeoffDTO();
		timeoffDTO.setStartDate("05/12/2017");
		timeoffDTO.setEndDate("05/12/2017");
		timeoffDTO.setPtoRequestDetailDTO(validateTimeoffRequestDetailDTO());
		timeoffDTO.setPtoTypeName("welcome");
		return timeoffDTO;
	}
	
	private static List<TimeoffRequestDetailDTO> validateTimeoffRequestDetailDTO() {
		TimeoffRequestDetailDTO timeoffRequestDetailDTO = new TimeoffRequestDetailDTO();
		timeoffRequestDetailDTO.setRequestedDate("05/12/2017");
		timeoffRequestDetailDTO.setRequestedHours("8");
		timeoffRequestDetailDTO.setCreatedBy(101L);
		timeoffRequestDetailDTO.setCreatorName("Smiemployee");
		timeoffRequestDetailDTO.setUpdatedBy(101L);
		timeoffRequestDetailDTO.setUpdatorName("Smiemployee");
		timeoffRequestDetailDTO.setStatus(AWAITINGAPPROVAL);
		
		List<TimeoffRequestDetailDTO> timeoffRequestList = new ArrayList<>();
		timeoffRequestList.add(timeoffRequestDetailDTO);
		return timeoffRequestList;
	}
	
	private static List<TimeoffActivityLogDTO> getTimeoffActivityLogDTO(){
		TimeoffActivityLogDTO timeoffActivityLogDTO = new TimeoffActivityLogDTO();
		timeoffActivityLogDTO.setEmployeeName("All In All");
		timeoffActivityLogDTO.setId(UUID.fromString("000181d4-2b11-8702-0035-111f1f3ccec9"));
		timeoffActivityLogDTO.setTimeoffId(UUID.fromString("000181d4-2b11-8702-0035-111f1f15f771"));
		List<TimeoffActivityLogDTO> timeoffActivityLogDTOList = new ArrayList<>();
		timeoffActivityLogDTOList.add(timeoffActivityLogDTO);
		return timeoffActivityLogDTOList;
	}
	
	private static Timeoff prepareTimeoff() {
		Date startDate = new Date();
		Date endDate = new Date();
		Timeoff timeoff = new Timeoff();
		String timeoffId = "000181d4-2b11-8702-0035-111f1f15f771";
		timeoff.setId(UUID.fromString(timeoffId));
		
		
		timeoff.setStatus(AWAITINGAPPROVAL);
		timeoff.setStartDate(startDate);
		timeoff.setEndDate(endDate);
		timeoff.setStartDateStr("05/03/2017");
		timeoff.setEndDateStr("05/04/2017");
		timeoff.setLastUpdatedDateStr(DateUtil.parseDateWithTime(new Date()));
		timeoff.setCreated(prepareAuditFields(101L, "smiName", "smi@techmango.net"));
		timeoff.setUpdated(prepareAuditFields(101L, "smiName", "smi@techmango.net"));
		timeoff.setPtoRequestDetail(getTimeoffRequestDetail());
		
		timeoff.setPtoTypeId("e0f3db88-a00e-41fc-ba47-361f496e11ad");
		timeoff.setPtoTypeName("Sick");
		timeoff.setEngagementId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
		timeoff.setEmployeeId(1045396L);
		timeoff.setEmployeeName("All In All");
		
		timeoff.setTotalHours(12.0);
		return timeoff;
	}
	
	private static List<TimeoffRequestDetail> getTimeoffRequestDetail() {
		TimeoffRequestDetail timeoffRequestDetail = new TimeoffRequestDetail();
		timeoffRequestDetail.setRequestedDate(new Date());
		timeoffRequestDetail.setRequestedHours("8");
		timeoffRequestDetail.setCreatedBy("101");
		timeoffRequestDetail.setCreatorName("Smiemployee");
		timeoffRequestDetail.setUpdatedBy(101L);
		timeoffRequestDetail.setUpdatorName("Smiemployee");
		timeoffRequestDetail.setStatus(AWAITINGAPPROVAL);
		
		List<TimeoffRequestDetail> timeoffRequestList = new ArrayList<>();
		timeoffRequestList.add(timeoffRequestDetail);
		return timeoffRequestList;
	}

	private static AuditFields prepareAuditFields(Long employeeId, String employeeName, String email) {
		AuditFields auditFields = new AuditFields();
		auditFields.setBy(employeeId);
		auditFields.setEmail(email);
		auditFields.setName(employeeName);
		auditFields.setOn(new Date());
		return auditFields;
	}
	
	private static Page<Timeoff> listOfTimeoff(){
		Pageable pageable = null;
		Timeoff timeoff = prepareTimeoff();
		List<Timeoff> timeoffList = new ArrayList<>();
		timeoffList.add(timeoff);
		return new PageImpl<>(timeoffList, pageable, timeoffList.size());
	}
	
	@Test
	public void checkGetTimeoffDetails(){
		
		String timeoffId = "000181d4-2b11-8702-0035-111f1f15f771";
		Timeoff timeoff = prepareTimeoff();
		when(timeoffRepository.findOne(UUID.fromString(timeoffId))).thenReturn(timeoff);
		
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		request.setAttribute("token", requestedToken);
		String holidayUrl = "http://COMMONSERVICEMANAGEMENT/common/engagements/employeeengagementdetails?userId=1045396&engagementId=00000000-0000-0000-0000-000000000001";
		ParameterizedTypeReference<ContractorEmployeeEngagementView> responseType = new ParameterizedTypeReference<ContractorEmployeeEngagementView>() {
		};
		ContractorEmployeeEngagementView contractorEmployeeEngagementView = mock(ContractorEmployeeEngagementView.class);
		when(contractorEmployeeEngagementView.getStartDay()).thenReturn(ContractorEmployeeEngagementView.day.Mon);
		when(contractorEmployeeEngagementView.getEndDay()).thenReturn(ContractorEmployeeEngagementView.day.Sat);
		when(contractorEmployeeEngagementView.getEmplEffStartDate()).thenReturn(new Date("03/10/2017"));
		when(contractorEmployeeEngagementView.getEmplEffEndDate()).thenReturn(new Date("06/10/2017"));
        ResponseEntity<ContractorEmployeeEngagementView> resp = new ResponseEntity<>(contractorEmployeeEngagementView, HttpStatus.OK);
        HttpHeaders httpHeaders = new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
			}
		};
		when(restTemplate.exchange(holidayUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(resp);
		
		timeoffServiceImpl.getMyTimeoff(timeoffId, MY_TIMEOFF);
	}
	
	@Test
	public void testGetPtoTypes() {

		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		request.setAttribute("token", requestedToken);
		String holidayUrl = "http://COMMONSERVICEMANAGEMENT/common/entity-values/lookup?attribute=TIMEOFF_PTO_TYPE_ID&entity=TIME_OFF";
		ParameterizedTypeReference<List<EntityAttributeDTO>> responseType = new ParameterizedTypeReference<List<EntityAttributeDTO>>() {
		};
		EntityAttributeDTO entityAttributeDTO = mock(EntityAttributeDTO.class);
		List<EntityAttributeDTO> entityAttributeDTOs = Arrays.asList(entityAttributeDTO);
        ResponseEntity<List<EntityAttributeDTO>> resp = new ResponseEntity<>(entityAttributeDTOs, HttpStatus.OK);
        HttpHeaders httpHeaders = new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
			}
		};
		when(restTemplate.exchange(holidayUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(resp);
		AssertJUnit.assertNotNull(timeoffServiceImpl.getPtoTypes());
	}
	
	@Test
	public void testSaveTimehseetActivityLog() {
		timeoffServiceImpl.saveTimehseetActivityLog(getEmployeeProfileDTO(), UUID.fromString(timesheetId), "01/01/2017", "TestComment", "TestRefType");
	}
	
	@Test (dataProvider = "employeeRestTemplateDataProvider", expectedExceptions = {TimeoffBadRequestException.class})
	public void testGetMyTeamTimeoffHolidays(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		ReflectionTestUtils.setField(timeoffServiceImpl, "SUNDAY", "Sunday" );
		ReflectionTestUtils.setField(timeoffServiceImpl, "SATURDAY", "Saturday" );
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		String holidayUrl = "http://COMMONSERVICEMANAGEMENT/common/holidays/province/1000?startDate=01/01/2016&endDate=01/05/2016";
		ParameterizedTypeReference<List<HolidayDTO>> respType = new ParameterizedTypeReference<List<HolidayDTO>>() {
		};
		HolidayDTO holidayDTO = mock(HolidayDTO.class);        
		when(holidayDTO.getHolidayDate()).thenReturn(new Date("01/05/2017"));
        List<HolidayDTO> holidayDTOs = Arrays.asList(holidayDTO);
        ResponseEntity<List<HolidayDTO>> resp = new ResponseEntity<>(holidayDTOs, HttpStatus.OK);
		when(restTemplate.exchange(holidayUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), respType)).thenReturn(resp);
		
		List<Timeoff> timeOffs = new ArrayList<>();
		timeOffs.add(prepareTimeoffData(getTimeOff()));
		when(timeoffRepository.timeoffList(Mockito.anyLong(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyString())).thenReturn(timeOffs);
		
		try {
			timeoffServiceImpl.getMyTeamTimeoffHolidays("01/01/2016", "01/05/2016", null, "100");
		} catch(Exception e) {}
		
		String contractorHolidayCommandUrl = "http://ENGAGEMENTMANAGEMENT/engagements/000181d4-2b11-8702-0035-111f1f15f771/holidays?startDate=01/01/2016&endDate=01/05/2016";
		ParameterizedTypeReference<List<HolidayResource>> holidayResponseType = new ParameterizedTypeReference<List<HolidayResource>>() {
		};
		HolidayResource holidayResource = mock(HolidayResource.class);
		when(holidayResource.getHolidayDate()).thenReturn(new Date("01/03/2016"));
		List<HolidayResource> holidayResources = Arrays.asList(holidayResource);
        ResponseEntity<List<HolidayResource>> holidayResponse = new ResponseEntity<>(holidayResources, HttpStatus.OK);
		when(restTemplate.exchange(contractorHolidayCommandUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), holidayResponseType)).thenReturn(holidayResponse);
		
		String contractorUrl = "http://COMMONSERVICEMANAGEMENT/common/engagements/employeeengagementdetails?userId=100&engagementId=000181d4-2b11-8702-0035-111f1f15f771";
		ParameterizedTypeReference<ContractorEmployeeEngagementView> contractorRespType = new ParameterizedTypeReference<ContractorEmployeeEngagementView>() {
		};
		ContractorEmployeeEngagementView contractorEmployeeEngagementView = mock(ContractorEmployeeEngagementView.class);
		when(contractorEmployeeEngagementView.getStartDay()).thenReturn(ContractorEmployeeEngagementView.day.Mon);
		when(contractorEmployeeEngagementView.getEndDay()).thenReturn(ContractorEmployeeEngagementView.day.Sat);
		when(contractorEmployeeEngagementView.getEmplEffStartDate()).thenReturn(new Date("03/10/2017"));
		when(contractorEmployeeEngagementView.getEmplEffEndDate()).thenReturn(new Date("06/10/2017"));
        ResponseEntity<ContractorEmployeeEngagementView> contractorResp = new ResponseEntity<>(contractorEmployeeEngagementView, HttpStatus.OK);
		when(restTemplate.exchange(contractorUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), contractorRespType)).thenReturn(contractorResp);
		
		AssertJUnit.assertNotNull(timeoffServiceImpl.getMyTeamTimeoffHolidays("01/01/2016", "01/05/2016", "000181d4-2b11-8702-0035-111f1f15f771", "100"));
		
		timeoffServiceImpl.getMyTeamTimeoffHolidays("01/01/2016", "01/05/2016", "000181d4-2b11-8702-0035-111f1f15f771", null);
	}
	
	@Test
	public void testGetMyTimeoffHolidays() throws ParseException {

		ReflectionTestUtils.setField(timeoffServiceImpl, "SUNDAY", "Sunday" );
		ReflectionTestUtils.setField(timeoffServiceImpl, "SATURDAY", "Saturday" );
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
        HttpHeaders httpHeaders = new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
			}
		};
		
		String holidayUrl = "http://COMMONSERVICEMANAGEMENT/common/holidays/province/1000?startDate=01/01/2016&endDate=01/05/2016";
		ParameterizedTypeReference<List<HolidayDTO>> respType = new ParameterizedTypeReference<List<HolidayDTO>>() {
		};
		HolidayDTO holidayDTO = mock(HolidayDTO.class);        
		when(holidayDTO.getHolidayDate()).thenReturn(new Date("01/05/2017"));
        List<HolidayDTO> holidayDTOs = Arrays.asList(holidayDTO);
        ResponseEntity<List<HolidayDTO>> resp = new ResponseEntity<>(holidayDTOs, HttpStatus.OK);
		when(restTemplate.exchange(holidayUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), respType)).thenReturn(resp);
		
		List<Timeoff> timeOffs = new ArrayList<>();
		timeOffs.add(prepareTimeoffData(getTimeOff()));
		
		String contractorHolidayCommandUrl = "http://ENGAGEMENTMANAGEMENT/engagements/000181d4-2b11-8702-0035-111f1f15f771/holidays?startDate=01/01/2016&endDate=01/05/2016";
		ParameterizedTypeReference<List<HolidayResource>> holidayResponseType = new ParameterizedTypeReference<List<HolidayResource>>() {
		};
		HolidayResource holidayResource = mock(HolidayResource.class);
		when(holidayResource.getHolidayDate()).thenReturn(new Date("01/03/2016"));
		List<HolidayResource> holidayResources = Arrays.asList(holidayResource);
        ResponseEntity<List<HolidayResource>> holidayResponse = new ResponseEntity<>(holidayResources, HttpStatus.OK);
		when(restTemplate.exchange(contractorHolidayCommandUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), holidayResponseType)).thenReturn(holidayResponse);
		
		String contractorUrl = "http://COMMONSERVICEMANAGEMENT/common/engagements/employeeengagementdetails?userId=100&engagementId=000181d4-2b11-8702-0035-111f1f15f771";
		ParameterizedTypeReference<ContractorEmployeeEngagementView> contractorRespType = new ParameterizedTypeReference<ContractorEmployeeEngagementView>() {
		};
		ContractorEmployeeEngagementView contractorEmployeeEngagementView = mock(ContractorEmployeeEngagementView.class);
		when(contractorEmployeeEngagementView.getStartDay()).thenReturn(ContractorEmployeeEngagementView.day.Mon);
		when(contractorEmployeeEngagementView.getEndDay()).thenReturn(ContractorEmployeeEngagementView.day.Sat);
		when(contractorEmployeeEngagementView.getEmplEffStartDate()).thenReturn(new Date("03/10/2017"));
		when(contractorEmployeeEngagementView.getEmplEffEndDate()).thenReturn(new Date("06/10/2017"));
        ResponseEntity<ContractorEmployeeEngagementView> contractorResp = new ResponseEntity<>(contractorEmployeeEngagementView, HttpStatus.OK);
		when(restTemplate.exchange(contractorUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), contractorRespType)).thenReturn(contractorResp);
		
		when(timeoffRepository.timeoffList(Mockito.anyLong(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyString())).thenReturn(timeOffs);
		
		timeoffServiceImpl.getMyTimeoffHolidays("01/01/2016", "01/05/2016", 1000L, 100L, new Date("01/02/2017"), "R", "000181d4-2b11-8702-0035-111f1f15f771");
		
		timeoffServiceImpl.getMyTimeoffHolidays("01/01/2016", "01/05/2016", 1000L, 100L, new Date("01/02/2017"), "R", null);
	}
	
	@DataProvider(name = "employeeRestTemplateDataProvider")
    public static Iterator<Object[]> employeeRestTemplate() {
    	
        String url = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/100";
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
    
    private TimeoffDTO getTimeOff() {
    	
    	TimeoffDTO timeoff = new TimeoffDTO();
		timeoff.setEmployeeId("2");
		timeoff.setEmployeeName("Test employeename");
		timeoff.setReportingManagerId("1");
		timeoff.setReportingManagerName("Test reportmanager");
		timeoff.setPtoTypeId("c22e8658-e2f1-11e6-bf01-fe55135034f3");
		timeoff.setStartDate("02/10/2017");
		timeoff.setEndDate("02/13/2017");
		timeoff.setTotalHours("8");
		timeoff.setComments("commentsdata");
		timeoff.setTimeoffId(UUID.fromString(timeoffId));
		timeoff.setStatus("Active");
		timeoff.setPtoTypeName("PTO");
		timeoff.setTimesheetId("1");
		
		AuditFields createdAuditFields = new AuditFields();
		createdAuditFields.setBy(1l);
		createdAuditFields.setEmail("testmail@mail.com");
		createdAuditFields.setName("testname");
		//timeoff.setCreated(createdAuditFields);
		List<TimeoffRequestDetailDTO> timeoffRequestDetailDTOs = new ArrayList<TimeoffRequestDetailDTO>();
		TimeoffRequestDetailDTO timeoffRequestDetailDTOFirst = new TimeoffRequestDetailDTO();
		timeoffRequestDetailDTOFirst.setRequestedHours("8");
		timeoffRequestDetailDTOFirst.setRequestedDate("02/10/2017");
		timeoffRequestDetailDTOFirst.setWeekOffStatus(true);
		timeoffRequestDetailDTOFirst.setStatus("Active");
		TimeoffRequestDetailDTO timeoffRequestDetailDTOSecond = new TimeoffRequestDetailDTO();
		timeoffRequestDetailDTOSecond.setRequestedHours("8");
		timeoffRequestDetailDTOSecond.setRequestedDate("02/13/2017");
		timeoffRequestDetailDTOSecond.setWeekOffStatus(true);
		timeoffRequestDetailDTOSecond.setStatus("Active");
		timeoffRequestDetailDTOs.add(timeoffRequestDetailDTOFirst);
		timeoffRequestDetailDTOs.add(timeoffRequestDetailDTOSecond);
		timeoff.setPtoRequestDetailDTO(timeoffRequestDetailDTOs);
		
		return timeoff;
    }
}
