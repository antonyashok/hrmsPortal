package com.tm.timesheet.service;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;
import com.tm.timesheet.configuration.domain.ConfigurationGroup;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.ActiveFlagEnum;
import com.tm.timesheet.configuration.domain.TimeruleConfiguration;
import com.tm.timesheet.configuration.domain.TimeruleConfiguration.ActiveFlag;
import com.tm.timesheet.configuration.repository.ConfigurationGroupRepository;
import com.tm.timesheet.configuration.repository.TimeruleConfigurationRepository;
import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.ActivityLog;
import com.tm.timesheet.domain.AuditFields;
import com.tm.timesheet.domain.Employee;
import com.tm.timesheet.domain.Engagement;
import com.tm.timesheet.domain.LookUpType;
import com.tm.timesheet.domain.SearchField;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.domain.TimesheetDetails;
import com.tm.timesheet.domain.UploadFilesDetails;
import com.tm.timesheet.domain.UploadLogs;
import com.tm.timesheet.repository.ActivityLogRepository;
import com.tm.timesheet.repository.TimesheetDetailsRepository;
import com.tm.timesheet.repository.TimesheetDetailsRepositoryCustom;
import com.tm.timesheet.repository.TimesheetRepository;
import com.tm.timesheet.repository.TimesheetRepositoryCustom;
import com.tm.timesheet.repository.TimesheetTemplateRepository;
import com.tm.timesheet.repository.UploadFilesDetailsRepository;
import com.tm.timesheet.repository.UploadLogsRepository;
import com.tm.timesheet.service.dto.CommonTimesheetDTO;
import com.tm.timesheet.service.dto.LookUpTypeDTO;
import com.tm.timesheet.service.dto.OverrideHourDTO;
import com.tm.timesheet.service.dto.TimeDetailDTO;
import com.tm.timesheet.service.dto.TimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetDetailsDTO;
import com.tm.timesheet.service.dto.TimesheetDetailsDTO.DayOfWeek;
import com.tm.timesheet.service.dto.TimesheetTaskDTO;
import com.tm.timesheet.service.impl.TimesheetServiceImpl;
import com.tm.timesheet.timeoff.domain.Timeoff;
import com.tm.timesheet.timeoff.domain.TimeoffRequestDetail;
import com.tm.timesheet.timeoff.service.TimeoffService;
import com.tm.timesheet.timeoff.service.dto.TimeoffDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffRequestDetailDTO;
import com.tm.timesheet.timeoff.web.rest.util.DateUtil;
import com.tm.timesheet.timesheetview.service.hystrix.commands.EmployeeRestTemplate;
import com.tm.timesheet.web.rest.util.MailManagerUtil;
import com.tm.timesheet.web.rest.util.TimesheetMailAsync;

public class TimesheetTest {

	@Mock
	private TimesheetDetailsRepository timesheetDetailsRepository;
	@Mock
	private TimesheetRepository timesheetRepository;
	@Mock
	private ConfigurationGroupRepository configurationGroupRepository;
	@Mock
	private TimeruleConfigurationRepository timeruleConfigurationRepository;
	@Mock
	private TimesheetRepositoryCustom timesheetRepositoryCustom;
	@Mock
	private TimesheetDetailsRepositoryCustom timesheetDetailsRepositoryCustom;
	@Mock
	private TimeoffService timeoffService;
	@Mock
	private RestTemplate restTemplate;
	@Mock
	private DiscoveryClient discoveryClient;
	@Mock
	private ActivityLogRepository activityLogRepository;
	@Mock
	private UploadLogsRepository uploadLogsRepository;
	@Mock
	private UploadFilesDetailsRepository uploadFilesDetailsRepository;
	@Mock
	private TimesheetTemplateRepository timesheetTemplateRepository;
	@Mock
	private MailManagerUtil mailManagerUtil;
	@Mock
	private EmployeeRestTemplate employeeRestTemplate;
	@Mock
	private TimesheetMailAsync timesheetMailAsync;

	private DiscoveryClientAndAccessTokenUtil discoveryClientAndAccessTokenUtil;

	private TimesheetServiceImpl timesheetServiceImpl;
	
	private static String timesheetId = "000181d4-2b11-8702-0035-111f1f15f771";
	private static final String APPROVED = "APPROVED";
	private static final String REJECTED = "REJECTED";
	private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIxZTgzYWNkMC00MDQ1LTQ0NTQtYmQwYi1lZmFmZWNjYTVlNzEiLCJleHAiOjE0OTQ4NTAxMTcsIm5iZiI6MCwiaWF0IjoxNDk0ODQ2NTE3LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjEyMjo4MDgwL2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6ImYzOGJkNzI3LWQyMmMtNDg4Zi04MGZkLTAyZWQ5YmMxMzBmNCIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6ImRhZGRiNmUwLWRjYTEtNDM3NC04NjJiLTM3ZTE3YjEyZWNiOCIsImFjciI6IjEiLCJjbGllbnRfc2Vzc2lvbiI6ImQ5YWRiNjk1LTQ1MTQtNDZhNC1hYmVmLWI4MWNmOTg5YWRlMSIsImFsbG93ZWQtb3JpZ2lucyI6W10sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7fSwibmFtZSI6IlZpbmF5YWdhbW9vcnRoaSBSYWplbmRyYW4iLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJydmluYXlhZ2Ftb29ydGhpQGdtYWlsLmNvbSIsImdpdmVuX25hbWUiOiJWaW5heWFnYW1vb3J0aGkiLCJmYW1pbHlfbmFtZSI6IlJhamVuZHJhbiIsImVtYWlsIjoicnZpbmF5YWdhbW9vcnRoaUBnbWFpbC5jb20ifQ.h6txZba0smrD41POBOzgWm9FOkwZi8wEWgtDhuFrKlnJpom2Lp-inG9_jcpdKQP6V-Up_h71cITB14MWj6n0RCPmzUPjnCl93YIVV178oSMlW0rM7kLCKPW1hNje6Pl4fievhXCItHQoHUaCRQnxSOV_Tcl-oa-gxuxw8DlQvRm4hVR7D3qBi8ZhOSm9tB1rFiTExte4td-YQm5S8jNdNiWCIhtX8P5IaSoXWmTtero6ukIuoeymKVKCkV11-vtKdw0G_gnsd7uXNLvs5kylW8mD3sG9DWsUJYuoVfChut3HFtciEgpk16IvEaDyyLFmfSpMmAeHLHZVN7CYAjtQiA";

	@BeforeMethod
	public void timesheetTestConfig() {
		timesheetDetailsRepository = Mockito
				.mock(TimesheetDetailsRepository.class);
		timesheetRepository = Mockito.mock(TimesheetRepository.class);
		configurationGroupRepository = Mockito
				.mock(ConfigurationGroupRepository.class);
		timeruleConfigurationRepository = Mockito
				.mock(TimeruleConfigurationRepository.class);
		timesheetRepositoryCustom = Mockito
				.mock(TimesheetRepositoryCustom.class);
		timesheetDetailsRepositoryCustom = Mockito
				.mock(TimesheetDetailsRepositoryCustom.class);
		timeoffService = Mockito.mock(TimeoffService.class);
		restTemplate = Mockito.mock(RestTemplate.class);
		discoveryClient = Mockito.mock(DiscoveryClient.class);
		activityLogRepository = Mockito.mock(ActivityLogRepository.class);
		uploadLogsRepository = Mockito.mock(UploadLogsRepository.class);
		uploadFilesDetailsRepository = Mockito
				.mock(UploadFilesDetailsRepository.class);
		mailManagerUtil = Mockito.mock(MailManagerUtil.class);
		discoveryClientAndAccessTokenUtil = Mockito
				.mock(DiscoveryClientAndAccessTokenUtil.class);
		employeeRestTemplate = Mockito.mock(EmployeeRestTemplate.class);
		timesheetTemplateRepository = Mockito.mock(TimesheetTemplateRepository.class);
		timesheetServiceImpl = new TimesheetServiceImpl(
				timesheetDetailsRepository, timesheetRepository,
				configurationGroupRepository, timeruleConfigurationRepository,
				timesheetRepositoryCustom, timesheetDetailsRepositoryCustom,
				timeoffService, restTemplate, discoveryClient,
				activityLogRepository, uploadLogsRepository,
				uploadFilesDetailsRepository, timesheetTemplateRepository, mailManagerUtil,timesheetMailAsync);
	}

	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testUpdateTimesheet(ParameterizedTypeReference<EmployeeProfileDTO> responseType, 
			ResponseEntity<EmployeeProfileDTO> responseEntity,
			HttpHeaders httpHeaders, String url) throws ParseException {
		Long units = 100L;
		String ptoTypeName = "Innopeople";
		List<TimesheetDetails> timesheetDetailsList = getTimesheetDetailsList();
		Timeoff timeoff = getTimeoff();
		List<Timeoff> timeoffList = new ArrayList<>();
		timeoffList.add(timeoff);
		TimeoffDTO timeofDTO = getTimeoffDTO();
		List<TimeoffDTO> timeoffDTOList = new ArrayList<>();
		timeoffDTOList.add(timeofDTO);
		EmployeeProfileDTO employee = getEmployeeProfileDTO();

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(timesheetDetailsRepository.deleteTimesheetDetailsByTimesheetId(UUID.fromString(timesheetId))).thenReturn(units);
		when(timesheetDetailsRepository.save(timesheetDetailsList)).thenReturn(timesheetDetailsList);
		when(timesheetRepository.findOne(UUID.fromString(timesheetId))).thenReturn(getTimesheet());
		Mockito.doNothing().when(timeoffService).removeAndUpdateTimesheetAppliedTimeoff(null,UUID.fromString(timesheetId));
		Mockito.doNothing().when(timeoffService).createTimesheetTimeoff(timeoffDTOList, employee);
		when(activityLogRepository.save(getActivityLogList())).thenReturn(getActivityLogList());
		when(employeeRestTemplate.getEmployeeProfileDTO()).thenReturn(getEmployeeProfileDTO());

		Timesheet timesheet = Mockito.mock(Timesheet.class);
		when(timesheetRepository.findOne((UUID)Mockito.anyObject())).thenReturn(timesheet);
		LookUpType lookUpType = Mockito.mock(LookUpType.class);
		when(timesheet.getLookupType()).thenReturn(lookUpType);
		SearchField searchField = Mockito.mock(SearchField.class);
		when(timesheet.getSearchField()).thenReturn(searchField);
		AuditFields fields = Mockito.mock(AuditFields.class);
		when(timesheet.getUpdated()).thenReturn(fields);
		when(fields.getOn()).thenReturn(new Date());
		when(lookUpType.getValue()).thenReturn("Units");
		CommonTimesheetDTO commonTimesheetDTO = getCommonTimesheetDTO();
		timesheetServiceImpl.updateTimesheet(commonTimesheetDTO);
		
		LookUpTypeDTO lookupType = new LookUpTypeDTO();
		lookupType.setValue("Hours");
		commonTimesheetDTO.setLookupType(lookupType);
		timesheetServiceImpl.updateTimesheet(commonTimesheetDTO);
		
		lookupType.setValue("Timestamp");
		commonTimesheetDTO.setLookupType(lookupType);
		timesheetServiceImpl.updateTimesheet(commonTimesheetDTO);
		
		lookupType.setValue("Timer");
		commonTimesheetDTO.setLookupType(lookupType);
		timesheetServiceImpl.updateTimesheet(commonTimesheetDTO);
		
		commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).getOverrideHour().setEndTime("");
		timesheetServiceImpl.updateTimesheet(commonTimesheetDTO);
		
		lookupType.setValue("Timestamp");
		commonTimesheetDTO.setLookupType(lookupType);
		TimeDetailDTO timeDetailDTO = commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).getTimeDetail().get(0);
		timeDetailDTO.setStartTime("");
		try {
			timesheetServiceImpl.updateTimesheet(commonTimesheetDTO);
		} catch (Exception e) {}
		
		commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).setTaskName("");
		try {
			timesheetServiceImpl.updateTimesheet(commonTimesheetDTO);
		} catch (Exception e) {}
		
		timeDetailDTO.setStartTime("9:30 AM");
		timeDetailDTO.setEndTime("");
		try {
			timesheetServiceImpl.updateTimesheet(commonTimesheetDTO);
		} catch (Exception e) {}
		
		commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).setTaskName("TestTaskName");
		try {
			timesheetServiceImpl.updateTimesheet(commonTimesheetDTO);
		} catch (Exception e) {}
	}

	@Test(dataProviderClass = TimesheetTestDataProvider.class, dataProvider = "testDisputTimesheet", description = "")
	public void testDisputTimesheet(Timesheet timesheet, UUID timesheetId)
			throws ParseException {
		when(timesheetRepository.findOne(timesheetId)).thenReturn(timesheet);
		when(timesheetRepository.save(timesheet)).thenReturn(timesheet);
		timesheetServiceImpl.disputeTimesheet(timesheetId);
	}

	@Test(dataProviderClass = TimesheetTestDataProvider.class, dataProvider = "getAllUploadFilesDetails")
	public void getAllUploadFilesDetails(
			Page<UploadFilesDetails> uploadFilesDetails, Pageable pageable) {
		when(uploadFilesDetailsRepository.findAll(pageable)).thenReturn(
				uploadFilesDetails);
		timesheetServiceImpl.getAllUploadFilesDetails(pageable);
	}

	@Test(dataProviderClass = TimesheetTestDataProvider.class, dataProvider = "getAllUploadLogs")
	public void getAllUploadLogs(List<UploadLogs> uploadLogsList) {
		String fileName = "engagementfile.xls";
		when(uploadLogsRepository.findByOriginalFileName(fileName)).thenReturn(
				uploadLogsList);
		timesheetServiceImpl.getAllUploadLogs(fileName);
	}

	/*	@Test(dataProviderClass = TimesheetTestDataProvider.class, dataProvider = "getUploadFilesDetails")
	public void readTimesheetExcel(UploadFilesDetails uploadFilesDetails) throws IOException{
		when(uploadFilesDetailsRepository.save(uploadFilesDetails)).thenReturn(uploadFilesDetails);
		InputStream inputstream = new FileInputStream("D:/Back up/Book1.xls");
		timesheetServiceImpl.readTimesheetExcel(inputstream, "customeNames.xls");
	}*/
	
	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testupdateTimesheet(ParameterizedTypeReference<EmployeeProfileDTO> responseType, 
			ResponseEntity<EmployeeProfileDTO> responseEntity,
			HttpHeaders httpHeaders, String url) throws ParseException {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		TimesheetDetailsDTO timesheetDetailsDTO = new TimesheetDetailsDTO();
		timesheetDetailsDTO.setComments("TestComments");
		timesheetDetailsDTO.setActiveTaskFlag(true);
		timesheetDetailsDTO.setTimesheetId(UUID.fromString(timesheetId));
		timesheetDetailsDTO.setTimesheetDetailsId(UUID.fromString("000181d4-2b11-8702-0035-111f1f15f711"));
		timesheetDetailsDTO.setTimesheetDate("01/05/2017");
		timesheetDetailsDTO.setHours("10");
		timesheetDetailsDTO.setUnits(2L);
		OverrideHourDTO overrideHourDTO = new OverrideHourDTO();
		overrideHourDTO.setStartTime("9:30 PM");
		overrideHourDTO.setEndTime("11:30 PM");
		overrideHourDTO.setHours("2");
		overrideHourDTO.setReason("TestReason");
		overrideHourDTO.setBreakHours("1");
		timesheetDetailsDTO.setOverrideHour(overrideHourDTO);
		TimeDetailDTO timeDetailDTO = new TimeDetailDTO();
		timeDetailDTO.setStartTime("9:30 AM");
		timeDetailDTO.setEndTime("8:30 PM");
		timeDetailDTO.setBreakHours(1);
		List<TimeDetailDTO> timeDetailDTOs = new ArrayList<TimeDetailDTO>();
		timeDetailDTOs.add(timeDetailDTO);
		timesheetDetailsDTO.setTimeDetail(timeDetailDTOs);
		Timesheet timesheet = Mockito.mock(Timesheet.class);
		when(timesheet.getId()).thenReturn(UUID.fromString("000181d4-2b11-8702-0035-111f1f15f722"));
		when(timesheet.getPtoHours()).thenReturn(20D);
		when(timesheet.getLeaveHours()).thenReturn(16D);
		LookUpType lookUpType = Mockito.mock(LookUpType.class);
		when(lookUpType.getValue()).thenReturn(TimesheetConstants.UNITS);
		when(timesheet.getLookupType()).thenReturn(lookUpType);
		when(timesheetRepository.findOne(timesheetDetailsDTO.getTimesheetId())).thenReturn(timesheet);
		TimesheetDetails timesheetDetails = Mockito.mock(TimesheetDetails.class);
		when(timesheetDetails.getHours()).thenReturn(10D);
		when(timesheetDetails.getUnits()).thenReturn(2L);
		List<TimesheetDetails> finaltimeSheetDetailList = new ArrayList<>();
		finaltimeSheetDetailList.add(timesheetDetails);
		when(timesheetDetailsRepository.findByTimesheetId(UUID.fromString("000181d4-2b11-8702-0035-111f1f15f722"))).thenReturn(finaltimeSheetDetailList);
		
		AssertJUnit.assertNotNull(timesheetServiceImpl.updateTimesheet(timesheetDetailsDTO, "Hours"));
		
		AssertJUnit.assertNotNull(timesheetServiceImpl.updateTimesheet(timesheetDetailsDTO, "Units"));
		
		AssertJUnit.assertNotNull(timesheetServiceImpl.updateTimesheet(timesheetDetailsDTO, "Timestamp"));
		
		timesheetDetailsDTO.setOverrideFlag(true);
		AssertJUnit.assertNotNull(timesheetServiceImpl.updateTimesheet(timesheetDetailsDTO, "Timer"));
		
		timesheetDetailsDTO.setOverrideFlag(true);
		timesheetDetailsDTO.getOverrideHour().setEndTime("");
		AssertJUnit.assertNotNull(timesheetServiceImpl.updateTimesheet(timesheetDetailsDTO, "Timer"));
	}

	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testSubmitTimesheet(ParameterizedTypeReference<EmployeeProfileDTO> responseType, 
			ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		String urlNew = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/100";
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(restTemplate.exchange(urlNew, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		Timesheet timesheet = Mockito.mock(Timesheet.class);
		Employee employee = Mockito.mock(Employee.class);
		SearchField searchField = Mockito.mock(SearchField.class);
		AuditFields auditFields = Mockito.mock(AuditFields.class); 
		Engagement engagement = Mockito.mock(Engagement.class);
		when(engagement.getId()).thenReturn("000181d4-2b11-8702-0035-111f1f15f733");
		when(employee.getId()).thenReturn(100L);
		when(employee.getReportingManagerId()).thenReturn(100L);
		when(employee.getType()).thenReturn("R");
		when(timesheet.getId()).thenReturn(UUID.fromString("000181d4-2b11-8702-0035-111f1f15f722"));
		when(timesheet.getPtoHours()).thenReturn(20D);
		when(timesheet.getLeaveHours()).thenReturn(16D);
		when(timesheet.getEmployee()).thenReturn(employee);
		when(timesheet.getSearchField()).thenReturn(searchField);
		when(timesheet.getUpdated()).thenReturn(auditFields);
		when(timesheet.getSubmitted()).thenReturn(auditFields);
		when(timesheet.getApproved()).thenReturn(auditFields);
		when(timesheet.getStatus()).thenReturn("Not Submitted");
		when(timesheet.getEngagement()).thenReturn(engagement);
		when(auditFields.getOn()).thenReturn(new Date());
		LookUpType lookUpType = Mockito.mock(LookUpType.class);
		when(lookUpType.getValue()).thenReturn(TimesheetConstants.UNITS);
		when(timesheet.getLookupType()).thenReturn(lookUpType);
		when(timesheet.getTimeRuleId()).thenReturn(UUID.fromString(timesheetId));
		TimeruleConfiguration timeRule = Mockito.mock(TimeruleConfiguration.class);
		when(timeRule.getActiveIndFlag()).thenReturn(ActiveFlag.Y);
		when(timeRule.getOtForWeekHrsExceeds()).thenReturn(2D);
		when(timeRule.getOtForDayHrsExceeds()).thenReturn(1D);
		when(timeRule.getRateForHolidays()).thenReturn("10");
		when(timeRule.getRateForSundays()).thenReturn("15");
		when(timeruleConfigurationRepository.findOne(timesheet.getTimeRuleId())).thenReturn(timeRule);
		
		when(timesheetRepository.findOne((UUID)Mockito.anyObject())).thenReturn(timesheet);
		CommonTimesheetDTO commonTimesheetDTO = getCommonTimesheetDTO();
		timesheetServiceImpl.submitTimesheet(commonTimesheetDTO);
		
		commonTimesheetDTO.setTimesheetType(TimesheetConstants.TIMESHEET_PROXY_TYPE);
		timesheetServiceImpl.submitTimesheet(commonTimesheetDTO);
		
		commonTimesheetDTO.setTimesheetType(TimesheetConstants.TIMESHEET_VERIFICATION_TYPE);
		when(engagement.getId()).thenReturn(null);
		timesheetServiceImpl.submitTimesheet(commonTimesheetDTO);
		
		when(timesheet.getConfigGroupId()).thenReturn(UUID.fromString("000181d4-2b11-8702-0035-111f1f15f733"));
		ConfigurationGroup configGroup = Mockito.mock(ConfigurationGroup.class);
		when(configurationGroupRepository.findOne(timesheet.getConfigGroupId())).thenReturn(configGroup);
		when(configGroup.getActiveFlag()).thenReturn(ActiveFlagEnum.N);
		timesheetServiceImpl.submitTimesheet(commonTimesheetDTO);
	}

	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testReopenTimesheet(ParameterizedTypeReference<EmployeeProfileDTO> responseType, 
			ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		String urlNew = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/100";
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(restTemplate.exchange(urlNew, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		Timesheet timesheet = Mockito.mock(Timesheet.class);
		Employee employee = Mockito.mock(Employee.class);
		SearchField searchField = Mockito.mock(SearchField.class);
		AuditFields auditFields = Mockito.mock(AuditFields.class); 
		Engagement engagement = Mockito.mock(Engagement.class);
		when(engagement.getId()).thenReturn("000181d4-2b11-8702-0035-111f1f15f733");
		when(employee.getId()).thenReturn(100L);
		when(employee.getReportingManagerId()).thenReturn(100L);
		when(employee.getType()).thenReturn("R");
		when(timesheet.getId()).thenReturn(UUID.fromString("000181d4-2b11-8702-0035-111f1f15f722"));
		when(timesheet.getPtoHours()).thenReturn(20D);
		when(timesheet.getLeaveHours()).thenReturn(16D);
		when(timesheet.getEmployee()).thenReturn(employee);
		when(timesheet.getSearchField()).thenReturn(searchField);
		when(timesheet.getUpdated()).thenReturn(auditFields);
		when(timesheet.getSubmitted()).thenReturn(auditFields);
		when(timesheet.getApproved()).thenReturn(auditFields);
		when(timesheet.getStatus()).thenReturn("Not Submitted");
		when(timesheet.getEngagement()).thenReturn(engagement);
		when(timesheet.getStartDate()).thenReturn(new Date("01/05/2017"));
		when(auditFields.getOn()).thenReturn(new Date());
		LookUpType lookUpType = Mockito.mock(LookUpType.class);
		when(lookUpType.getValue()).thenReturn(TimesheetConstants.UNITS);
		when(timesheet.getLookupType()).thenReturn(lookUpType);
		when(timesheet.getTimeRuleId()).thenReturn(UUID.fromString(timesheetId));
		TimeruleConfiguration timeRule = Mockito.mock(TimeruleConfiguration.class);
		when(timeRule.getActiveIndFlag()).thenReturn(ActiveFlag.Y);
		when(timeRule.getOtForWeekHrsExceeds()).thenReturn(2D);
		when(timeRule.getOtForDayHrsExceeds()).thenReturn(1D);
		when(timeRule.getRateForHolidays()).thenReturn("10");
		when(timeRule.getRateForSundays()).thenReturn("15");
		when(timeruleConfigurationRepository.findOne(timesheet.getTimeRuleId())).thenReturn(timeRule);
		
		when(timesheetRepository.findOne((UUID)Mockito.anyObject())).thenReturn(timesheet);
		
		timesheetServiceImpl.reopenTimesheet(timesheetId);
	}
	
	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testBulkSubmitTimesheet(ParameterizedTypeReference<EmployeeProfileDTO> responseType, 
			ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		String urlNew = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/100";
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(restTemplate.exchange(urlNew, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		Timesheet timesheet = Mockito.mock(Timesheet.class);
		Employee employee = Mockito.mock(Employee.class);
		SearchField searchField = Mockito.mock(SearchField.class);
		AuditFields auditFields = Mockito.mock(AuditFields.class); 
		Engagement engagement = Mockito.mock(Engagement.class);
		when(employee.getId()).thenReturn(100L);
		when(employee.getReportingManagerId()).thenReturn(100L);
		when(employee.getType()).thenReturn("R");
		when(timesheet.getId()).thenReturn(UUID.fromString("000181d4-2b11-8702-0035-111f1f15f722"));
		when(timesheet.getPtoHours()).thenReturn(20D);
		when(timesheet.getLeaveHours()).thenReturn(16D);
		when(timesheet.getEmployee()).thenReturn(employee);
		when(timesheet.getSearchField()).thenReturn(searchField);
		when(timesheet.getUpdated()).thenReturn(auditFields);
		when(timesheet.getSubmitted()).thenReturn(auditFields);
		when(timesheet.getApproved()).thenReturn(auditFields);
		when(timesheet.getStatus()).thenReturn("Not Submitted");
		when(timesheet.getEngagement()).thenReturn(engagement);
		when(auditFields.getOn()).thenReturn(new Date());
		LookUpType lookUpType = Mockito.mock(LookUpType.class);
		when(lookUpType.getValue()).thenReturn(TimesheetConstants.UNITS);
		when(timesheet.getLookupType()).thenReturn(lookUpType);
		when(timesheet.getTimeRuleId()).thenReturn(UUID.fromString(timesheetId));
		TimeruleConfiguration timeRule = Mockito.mock(TimeruleConfiguration.class);
		when(timeRule.getActiveIndFlag()).thenReturn(ActiveFlag.Y);
		when(timeRule.getOtForWeekHrsExceeds()).thenReturn(2D);
		when(timeRule.getOtForDayHrsExceeds()).thenReturn(1D);
		when(timeRule.getRateForHolidays()).thenReturn("10");
		when(timeRule.getRateForSundays()).thenReturn("15");
		when(timeruleConfigurationRepository.findOne(timesheet.getTimeRuleId())).thenReturn(timeRule);
		
		when(timesheetRepository.findOne((UUID)Mockito.anyObject())).thenReturn(timesheet);
		CommonTimesheetDTO commonTimesheetDTO = getCommonTimesheetDTO();
		List<TimesheetDTO> timesheetDTOs = new ArrayList<>();
		TimesheetDTO timesheetDTO = new TimesheetDTO();
		timesheetDTO.setTimesheetId(UUID.fromString(timesheetId));
		timesheetDTO.setStatus("proxySubmit");
		timesheetDTOs.add(timesheetDTO);
		commonTimesheetDTO.setTimesheetList(timesheetDTOs);
		
		List<Timesheet> timesheetList = new ArrayList<Timesheet>();
		timesheetList.add(timesheet);
		when(timesheetRepositoryCustom.getAllTimesheetbyIds(Mockito.anyListOf(UUID.class))).thenReturn(timesheetList);
		List<TimesheetDetails> timesheetDetailList = new ArrayList<>();
		TimesheetDetails timesheetDetails = Mockito.mock(TimesheetDetails.class);
		when(timesheetDetails.getTaskName()).thenReturn("TestTaskName");
		timesheetDetailList.add(timesheetDetails);
		when(timesheetDetailsRepositoryCustom.getAllTimesheetDetailsByTimesheetIds(Mockito.anyListOf(UUID.class))).thenReturn(timesheetDetailList);
		timesheetServiceImpl.bulkSubmitTimesheet(commonTimesheetDTO);
		 
		when(timesheet.getStatus()).thenReturn("Not Verified");
		timesheetServiceImpl.bulkSubmitTimesheet(commonTimesheetDTO);
		
		timesheetDTO.setStatus("Submit");
		when(timesheet.getStatus()).thenReturn("Not Submitted");
		when(engagement.getId()).thenReturn("000181d4-2b11-8702-0035-111f1f15f733");
		timesheetServiceImpl.bulkSubmitTimesheet(commonTimesheetDTO);
	}
	
	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testTimerTimesheet(ParameterizedTypeReference<EmployeeProfileDTO> responseType, 
			ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		CommonTimesheetDTO commonTimesheetDTO = getCommonTimesheetDTO();
		
		timesheetServiceImpl.timerTimesheet(commonTimesheetDTO);
		
		List<TimeDetailDTO> timeDetailDTOs = commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).getTimeDetail();
		commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).setTimeDetail(new ArrayList<>());
		timesheetServiceImpl.timerTimesheet(commonTimesheetDTO);
		
		commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).setTimeDetail(timeDetailDTOs);
		Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = dateFormat.format(date);
		commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).setTimesheetDate(strDate);
		timesheetServiceImpl.timerTimesheet(commonTimesheetDTO);
		
		timeDetailDTOs.get(0).setEndTime("");
		commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).setTimeDetail(timeDetailDTOs);
		commonTimesheetDTO.getTaskDetails().get(0).setStartFlag(false);
		timesheetServiceImpl.timerTimesheet(commonTimesheetDTO);
		
		commonTimesheetDTO.getTaskDetails().get(0).setActiveTaskFlag(false);
		timesheetServiceImpl.timerTimesheet(commonTimesheetDTO);
	}

	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testApproveTimesheet(ParameterizedTypeReference<EmployeeProfileDTO> responseType, 
			ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		String urlNew = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/100";
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(restTemplate.exchange(urlNew, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		Timesheet timesheet = Mockito.mock(Timesheet.class);
		Employee employee = Mockito.mock(Employee.class);
		SearchField searchField = Mockito.mock(SearchField.class);
		AuditFields auditFields = Mockito.mock(AuditFields.class); 
		Engagement engagement = Mockito.mock(Engagement.class);
		when(employee.getId()).thenReturn(100L);
		when(employee.getReportingManagerId()).thenReturn(100L);
		when(employee.getType()).thenReturn("R");
		when(timesheet.getId()).thenReturn(UUID.fromString("000181d4-2b11-8702-0035-111f1f15f722"));
		when(timesheet.getPtoHours()).thenReturn(20D);
		when(timesheet.getLeaveHours()).thenReturn(16D);
		when(timesheet.getEmployee()).thenReturn(employee);
		when(timesheet.getSearchField()).thenReturn(searchField);
		when(timesheet.getUpdated()).thenReturn(auditFields);
		when(timesheet.getSubmitted()).thenReturn(auditFields);
		when(timesheet.getApproved()).thenReturn(auditFields);
		when(timesheet.getStatus()).thenReturn("Awaiting Approval");
		when(timesheet.getEngagement()).thenReturn(engagement);
		when(auditFields.getOn()).thenReturn(new Date());
		LookUpType lookUpType = Mockito.mock(LookUpType.class);
		when(lookUpType.getValue()).thenReturn(TimesheetConstants.UNITS);
		when(timesheet.getLookupType()).thenReturn(lookUpType);
		when(timesheet.getTimeRuleId()).thenReturn(UUID.fromString(timesheetId));
		TimeruleConfiguration timeRule = Mockito.mock(TimeruleConfiguration.class);
		when(timeRule.getActiveIndFlag()).thenReturn(ActiveFlag.Y);
		when(timeRule.getOtForWeekHrsExceeds()).thenReturn(2D);
		when(timeRule.getOtForDayHrsExceeds()).thenReturn(1D);
		when(timeRule.getRateForHolidays()).thenReturn("10");
		when(timeRule.getRateForSundays()).thenReturn("15");
		when(timeruleConfigurationRepository.findOne(timesheet.getTimeRuleId())).thenReturn(timeRule);
		
		when(timesheetRepository.findOne((UUID)Mockito.anyObject())).thenReturn(timesheet);
		
		CommonTimesheetDTO commonTimesheetDTO = getCommonTimesheetDTO();
		List<TimesheetDTO> timesheetDTOs = new ArrayList<>();
		TimesheetDTO timesheetDTO = new TimesheetDTO();
		timesheetDTO.setTimesheetId(UUID.fromString(timesheetId));
		timesheetDTO.setStatus("Approved");
		timesheetDTOs.add(timesheetDTO);
		commonTimesheetDTO.setTimesheetList(timesheetDTOs);

		timesheetServiceImpl.approveTimesheet(timesheetId, commonTimesheetDTO);
		
		timesheetDTO.setStatus("Rejected");
		timesheetServiceImpl.approveTimesheet(timesheetId, commonTimesheetDTO);
	}
	
	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testBulkApproveTimesheet(ParameterizedTypeReference<EmployeeProfileDTO> responseType, 
			ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		String urlNew = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/100";
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(restTemplate.exchange(urlNew, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		CommonTimesheetDTO commonTimesheetDTO = getCommonTimesheetDTO();
		List<TimesheetDTO> timesheetDTOs = new ArrayList<>();
		TimesheetDTO timesheetDTO = new TimesheetDTO();
		timesheetDTO.setTimesheetId(UUID.fromString(timesheetId));
		timesheetDTO.setStatus("Approved");
		timesheetDTOs.add(timesheetDTO);
		commonTimesheetDTO.setTimesheetList(timesheetDTOs);

		Timesheet timesheet = Mockito.mock(Timesheet.class);
		Employee employee = Mockito.mock(Employee.class);
		SearchField searchField = Mockito.mock(SearchField.class);
		AuditFields auditFields = Mockito.mock(AuditFields.class); 
		Engagement engagement = Mockito.mock(Engagement.class);
		when(employee.getId()).thenReturn(100L);
		when(employee.getReportingManagerId()).thenReturn(100L);
		when(employee.getType()).thenReturn("R");
		when(timesheet.getId()).thenReturn(UUID.fromString("000181d4-2b11-8702-0035-111f1f15f722"));
		when(timesheet.getPtoHours()).thenReturn(20D);
		when(timesheet.getLeaveHours()).thenReturn(16D);
		when(timesheet.getEmployee()).thenReturn(employee);
		when(timesheet.getSearchField()).thenReturn(searchField);
		when(timesheet.getUpdated()).thenReturn(auditFields);
		when(timesheet.getSubmitted()).thenReturn(auditFields);
		when(timesheet.getApproved()).thenReturn(auditFields);
		when(timesheet.getStatus()).thenReturn("Awaiting Approval");
		when(timesheet.getEngagement()).thenReturn(engagement);
		when(auditFields.getOn()).thenReturn(new Date());
		LookUpType lookUpType = Mockito.mock(LookUpType.class);
		when(lookUpType.getValue()).thenReturn(TimesheetConstants.UNITS);
		when(timesheet.getLookupType()).thenReturn(lookUpType);
		when(timesheet.getTimeRuleId()).thenReturn(UUID.fromString(timesheetId));
		TimeruleConfiguration timeRule = Mockito.mock(TimeruleConfiguration.class);
		when(timeRule.getActiveIndFlag()).thenReturn(ActiveFlag.Y);
		when(timeRule.getOtForWeekHrsExceeds()).thenReturn(2D);
		when(timeRule.getOtForDayHrsExceeds()).thenReturn(1D);
		when(timeRule.getRateForHolidays()).thenReturn("10");
		when(timeRule.getRateForSundays()).thenReturn("15");
		when(timeruleConfigurationRepository.findOne(timesheet.getTimeRuleId())).thenReturn(timeRule);
		when(timesheetRepository.findOne((UUID)Mockito.anyObject())).thenReturn(timesheet);
		
		List<Timesheet> timesheetList = new ArrayList<Timesheet>();
		timesheetList.add(timesheet);
		when(timesheetRepositoryCustom.getAllTimesheetbyIds(Mockito.anyListOf(UUID.class))).thenReturn(timesheetList);
		
		timesheetServiceImpl.bulkApproveTimesheet(commonTimesheetDTO);
		
		timesheetDTO.setStatus("Rejected");
		timesheetServiceImpl.bulkApproveTimesheet(commonTimesheetDTO);
	}
	
	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testtimerTimesheet(ParameterizedTypeReference<EmployeeProfileDTO> responseType, 
			ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		CommonTimesheetDTO commonTimesheetDTO = getCommonTimesheetDTO();
		TimesheetDetailsDTO timesheetDetailsDTO = commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0);
		timesheetServiceImpl.timerTimesheet(timesheetDetailsDTO);
		
		List<TimeDetailDTO> timeDetailDTOs = commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).getTimeDetail();
		commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).setTimeDetail(new ArrayList<>());
		timesheetServiceImpl.timerTimesheet(timesheetDetailsDTO);
		
		commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).setTimeDetail(timeDetailDTOs);
		Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = dateFormat.format(date);
        
		commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).setTimesheetDate(strDate);
		timesheetServiceImpl.timerTimesheet(timesheetDetailsDTO);
		
		timeDetailDTOs.get(0).setEndTime("");
		commonTimesheetDTO.getTaskDetails().get(0).getTimesheetDetailList().get(0).setTimeDetail(timeDetailDTOs);
		timesheetDetailsDTO.setStartFlag(false);
		timesheetServiceImpl.timerTimesheet(timesheetDetailsDTO);
		
		timesheetDetailsDTO.setStartFlag(true);
		timesheetServiceImpl.timerTimesheet(timesheetDetailsDTO);
	}
	
	@Test
	public void testReadTimesheetExcel() throws IOException {

		String fileName = "templates/timesheet.xlsx";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		InputStream inputStream = new FileInputStream(file);
		timesheetServiceImpl.readTimesheetExcel(inputStream, fileName);
	}

	private static CommonTimesheetDTO getCommonTimesheetDTO() {
		CommonTimesheetDTO commonTimesheetDTO = new CommonTimesheetDTO();
		commonTimesheetDTO.setConfigGroupId("124578");
		commonTimesheetDTO.setStartDate("05/05/2017");
		commonTimesheetDTO.setEndDate("06/05/2017");
		commonTimesheetDTO.setLeaveHours(15.00);
		commonTimesheetDTO.setWorkHours(25.00);
		commonTimesheetDTO.setTotalHours(40.00);
		commonTimesheetDTO.setTimesheetId(UUID.fromString(timesheetId));
		LookUpTypeDTO lookupType = new LookUpTypeDTO();
		lookupType.setValue("Units");
		commonTimesheetDTO.setLookupType(lookupType);
		List<TimesheetTaskDTO> taskDTOs = new ArrayList<>();
		List<TimesheetDetailsDTO> timesheetDetailsDTOs = new ArrayList<>();
		TimesheetDetailsDTO timesheetDetailsDTO = new TimesheetDetailsDTO();
		timesheetDetailsDTO.setOverrideFlag(true);
		OverrideHourDTO overrideHourDTO = new OverrideHourDTO();
		overrideHourDTO.setStartTime("9:30 PM");
		overrideHourDTO.setEndTime("11:30 PM");
		overrideHourDTO.setHours("2");
		overrideHourDTO.setReason("TestReason");
		overrideHourDTO.setBreakHours("1");
		timesheetDetailsDTO.setOverrideHour(overrideHourDTO);
		List<TimeDetailDTO> timeDetailDTOs = new ArrayList<>();
		TimeDetailDTO timeDetailDTO = new TimeDetailDTO();
		timeDetailDTO.setStartTime("9:30 AM");
		timeDetailDTO.setEndTime("8:30 PM");
		timeDetailDTO.setBreakHours(1);
		timeDetailDTO.setHours("11");
		timeDetailDTOs.add(timeDetailDTO);
		timesheetDetailsDTO.setTimeDetail(timeDetailDTOs);
		timesheetDetailsDTO.setHours("10");
		timesheetDetailsDTO.setTimesheetDate("01/05/2017");
		timesheetDetailsDTO.setComments("TestComments");
		timesheetDetailsDTO.setTaskName("TestTaskName");
		timesheetDetailsDTO.setUnits(1L);
		timesheetDetailsDTO.setDayOfWeek(DayOfWeek.Sun.toString());
		timesheetDetailsDTOs.add(timesheetDetailsDTO);
		TimesheetTaskDTO taskDTO = new TimesheetTaskDTO();
		taskDTO.setActiveTaskFlag(true);
		taskDTO.setStartFlag(true);
		taskDTO.setTimesheetDetailList(timesheetDetailsDTOs);
		taskDTOs.add(taskDTO);
		commonTimesheetDTO.setTaskDetails(taskDTOs);
		commonTimesheetDTO.setTimeoffDTOList(Arrays.asList(getTimeoffDTO()));
		
		return commonTimesheetDTO;
	}

	private static List<ActivityLog> getActivityLogList() {
		List<ActivityLog> activityLogList = new ArrayList<>();
		ActivityLog activityLog = new ActivityLog();
		activityLog.setEmployeeId(101L);
		activityLog.setEmployeeName("sam raj");
		activityLog.setEmployeeRoleName("manager");
		activityLog.setSourceReferenceId(UUID.fromString(timesheetId));
		activityLog.setSourceReferenceType("reference type");
		activityLog.setComment("unit test");
		activityLogList.add(activityLog);
		return activityLogList;
	}

	private static EmployeeProfileDTO getEmployeeProfileDTO() {
		EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();
		employeeProfileDTO.setEmployeeId(101L);
		employeeProfileDTO.setFirstName("Rajesh");
		employeeProfileDTO.setLastName("Sekar");
		employeeProfileDTO.setReportingManagerId(102L);
		employeeProfileDTO.setReportingManagerName("Joburds");
		return employeeProfileDTO;
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
		timeoffRequestDetailDTO.setStatus("Awaiting Approva");
		
		List<TimeoffRequestDetailDTO> timeoffRequestList = new ArrayList<>();
		timeoffRequestList.add(timeoffRequestDetailDTO);
		return timeoffRequestList;
	}

	private static List<TimesheetDetails> getTimesheetDetailsList() {
		String timesheetDate = "05/06/2017";
		TimesheetDetails timesheetDetails = new TimesheetDetails();
		timesheetDetails.setId(UUID.fromString(timesheetId));
		timesheetDetails.setTaskName("testing");
		timesheetDetails.setTimesheetDate(DateUtil
				.convertStringToDate(timesheetDate));
		timesheetDetails
				.setEmployeeEngagementTaskMapId("000181d4-2b11-8702-0035-111f1f15f770");
		List<TimesheetDetails> timesheetDetailsList = new ArrayList<>();
		timesheetDetailsList.add(timesheetDetails);
		return timesheetDetailsList;
	}

	private static Timesheet getTimesheet() {
		Timesheet timesheet = new Timesheet();
		timesheet.setId(UUID.fromString(timesheetId));
		String startDate = "05/05/2017";
		String endDate = "06/05/2017";
		timesheet.setStartDate(DateUtil.convertStringToDate(startDate));
		timesheet.setEndDate(DateUtil.convertStringToDate(endDate));
		timesheet.setStatus(APPROVED);
		timesheet.setWorkHours(9.00);
		timesheet.setTotalHours(300.00);
		timesheet.setLeaveHours(15.00);
		return timesheet;
	}

	private static Timeoff getTimeoff() {
		Date startDate = new Date();
		Date endDate = new Date();
		Timeoff timeoff = new Timeoff();
		String timeoffId = "000181d4-2b11-8702-0035-111f1f15f771";
		timeoff.setId(UUID.fromString(timeoffId));

		timeoff.setStatus(APPROVED);
		timeoff.setStartDate(startDate);
		timeoff.setEndDate(endDate);
		timeoff.setStartDateStr("05/03/2017");
		timeoff.setEndDateStr("05/04/2017");
		timeoff.setLastUpdatedDateStr(DateUtil.parseDateWithTime(new Date()));
		timeoff.setPtoRequestDetail(getTimeoffRequestDetail());

		timeoff.setPtoTypeId("e0f3db88-a00e-41fc-ba47-361f496e11ad");
		timeoff.setPtoTypeName("Sick");
		timeoff.setEngagementId(UUID
				.fromString("00000000-0000-0000-0000-000000000001"));
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
		timeoffRequestDetail.setStatus(APPROVED);

		List<TimeoffRequestDetail> timeoffRequestList = new ArrayList<>();
		timeoffRequestList.add(timeoffRequestDetail);
		return timeoffRequestList;
	}

	private static AuditFields prepareAuditFields(Long employeeId,
			String employeeName, String email) {
		AuditFields auditFields = new AuditFields();
		auditFields.setBy(employeeId);
		auditFields.setEmail(email);
		auditFields.setName(employeeName);
		auditFields.setOn(new Date());
		return auditFields;
	}

	@DataProvider(name = "employeeRestTemplateDataProvider")
	public static Iterator<Object[]> employeeRestTemplate() {

		String url = "http://COMMONSERVICEMANAGEMENT/common/employee-profile?emailId=allinall@techmango.net";
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();
		employeeProfileDTO.setEmployeeId(100L);
		employeeProfileDTO.setReportingManagerId(100L);
		employeeProfileDTO.setProvinceId(1000L);
		employeeProfileDTO.setJoiningDate(new Date("01/04/2017"));
		employeeProfileDTO.setEmployeeType("C");
		ParameterizedTypeReference<EmployeeProfileDTO> responseType = new ParameterizedTypeReference<EmployeeProfileDTO>() {
		};
		ResponseEntity<EmployeeProfileDTO> responseEntity = new ResponseEntity<>(
				employeeProfileDTO, HttpStatus.OK);
		HttpHeaders httpHeaders = new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION, new StringBuilder("Bearer")
						.append(" ").append(requestedToken).toString());
			}
		};
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { responseType, responseEntity, httpHeaders,
				url });
		return testData.iterator();
	}
}
