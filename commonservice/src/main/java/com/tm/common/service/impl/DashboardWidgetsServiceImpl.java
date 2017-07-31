package com.tm.common.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import javax.inject.Inject;


import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.tm.common.configuration.service.hystrix.commands.DashboardWidgetsRestTemplate;
import com.tm.common.employee.domain.CommonInfo.contactTypeEnum;
import com.tm.common.employee.repository.AccoladesRepository;
import com.tm.common.employee.repository.AnnouncementRepository;
import com.tm.common.employee.repository.EmployeeProfileRepository;
import com.tm.common.employee.repository.ExpenseReportViewRepository;
import com.tm.common.employee.repository.NewsLetterRepository;
import com.tm.common.engagement.domain.EmployeeProfile;
import com.tm.common.service.DashboardWidgetsService;
import com.tm.common.service.dto.MyActiveTaskDTO;
import com.tm.common.util.EmployeeConstants;
import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;

@Service
public class DashboardWidgetsServiceImpl implements DashboardWidgetsService {

	private EmployeeProfileRepository employeeProfileRepository;
	
	private ExpenseReportViewRepository expenseReportViewRepository;
	
	private NewsLetterRepository newsLetterRepository;
	
	private AccoladesRepository accoladesRepository;
	
	private AnnouncementRepository announcementRepository;
	
	private RestTemplate restTemplate;

	private DiscoveryClient discoveryClient;

	private static final String TIMESHEET_APPROVAL = "Timesheet Approval";
	
	private static final String EXPENSE_APPROVAL = "Expense Approval";
	
	private static final String INVOICE_APPROVAL = "Invoice Approval";
	
	private static final String TIMEOFF_APPROVAL = "Timeoff Approval";

	private static final String TIMESHEET_SUBMISSION = "Timesheet Submission";

	private static final String AWAITING_APPROVAL = "Awaiting Approval";
	
	private static final String OVERDUE_COUNT = "Overdue Count";
	
	private static final String OVERDUE = "Overdue";
	
	private static final String SUBMITTED = "SUBMITTED";
	
	private static final String NOT_SUBMITTED = "Not Submitted";

	private static final int BIRTHDAY_ANNIVERSARY_DATE_RANGE = 7;
	
	private static String TODAY_BIRTHDAY_STRING = "%s is celebrating %s birthday today (Emp Id - %s) ";
	
	private static String RECENT_BIRTHDAY_STRING = "%s celebrated %s birthday on %s (Emp Id - %s)";
	
	private static String UPCOMING_BIRTHDAY_STRING = "%s will be celebrating %s birthday on %s (Emp Id - %s)";
	
	private static String TODAY_ANNIVERSARY_STRING = "%s is completing %s %s year(s) with us today (Emp Id - %s) ";
	
	private static String RECENT_ANNIVERSARY_STRING = "%s completed %s %s year(s) with us on %s (Emp Id - %s)";
	
	private static String UPCOMING_ANNIVERSARY_STRING = "%s will be completing %s %s year(s) with us on %s (Emp Id - %s)";
	
	private static final String TIMEOFF_COUNT_URI = "/timetrack/team-timeoff/count/";
	
	private static final String TIMESHEET_COUNT_URI = "/timetrack/team_timesheets/count/";
	
	private static final String INVOICE_COUNT_URI = "/invoice/getinvoiceapprovalcount";
	
	private static final String TIMESHEET_SUBMISSION_URI = "/timetrack/team_timesheets/gettimesheetenddatebystatusforemployee/";
	
	private static final String INVOICE_GROUP_KEY = "INVOICE";
	
	private static final String TIMESHEET_GROUP_KEY = "TIMESHEETMANAGEMENT";
	
	private static final String GENDER_MALE = "Male";
	
	@Inject
	public DashboardWidgetsServiceImpl(EmployeeProfileRepository employeeProfileRepository,
			ExpenseReportViewRepository expenseReportViewRepository,
			@LoadBalanced final RestTemplate restTemplate,
			@Qualifier("discoveryClient") final DiscoveryClient discoveryClient,
			NewsLetterRepository newsLetterRepository,
			AccoladesRepository accoladesRepository,
			AnnouncementRepository announcementRepository) {
		this.employeeProfileRepository = employeeProfileRepository;
		this.expenseReportViewRepository = expenseReportViewRepository;
		this.restTemplate = restTemplate;
		this.discoveryClient = discoveryClient;
		this.newsLetterRepository = newsLetterRepository;
		this.accoladesRepository = accoladesRepository;
		this.announcementRepository = announcementRepository;
	}
	
	@Override
	public Map<String, Object> getBirthdayDetails() throws ParseException {

		Map<String, Object> response = new HashMap<>();
		LocalDate today = LocalDate.now();
		LocalDate upcoming = LocalDate.now().plusDays(BIRTHDAY_ANNIVERSARY_DATE_RANGE);
		LocalDate recent = LocalDate.now().minusDays(BIRTHDAY_ANNIVERSARY_DATE_RANGE);
		List<String> todayBirthdayAnniversaryTexts = new ArrayList<>();
		List<String> upcomingBirthdayAnniversaryTexts = new ArrayList<>();
		List<String> recentBirthdayAnniversaryTexts = new ArrayList<>();

		Long employeeId = getEmployeeId();
		// TODO : Need to retrieve the employees based on the address or city or country
		
		List<EmployeeProfile> employeeProfiles = employeeProfileRepository.findAll();
		if (CollectionUtils.isNotEmpty(employeeProfiles)) {
			employeeProfiles.stream().forEach(employeeProfile -> { 
					getDobTexts(employeeProfile, today, upcoming, recent, todayBirthdayAnniversaryTexts, 
							upcomingBirthdayAnniversaryTexts, recentBirthdayAnniversaryTexts);
					getDojTexts(employeeProfile, today, upcoming, recent, todayBirthdayAnniversaryTexts, 
							upcomingBirthdayAnniversaryTexts, recentBirthdayAnniversaryTexts);
				}
			);
		}

		response.put("Upcoming", upcomingBirthdayAnniversaryTexts);
		response.put("Recent", recentBirthdayAnniversaryTexts);
		response.put("Today", todayBirthdayAnniversaryTexts);
		
		return response;
	}
	
	@Override
	public Map<String, Object> getMyActiveTasks() throws ParseException {

		Long employeeId = getEmployeeId();
		Map<String, Object> myActiveTaskMap = new HashMap<>();
		List<MyActiveTaskDTO> myActiveTaskDTOs = new ArrayList<>();
		List<Long> ids = employeeProfileRepository.getAssociatedEmployeeIdsByReportingManagerId(employeeId);
		if (!ids.isEmpty()) {

			Integer timesheetCount = getTimesheetCountByStatusAndId(AWAITING_APPROVAL, "true");
			MyActiveTaskDTO myActiveTimesheetTask = new MyActiveTaskDTO();
			myActiveTimesheetTask.setTaskName(TIMESHEET_APPROVAL);
			myActiveTimesheetTask.setApprovalCount(timesheetCount == null ? 0 : timesheetCount);
			myActiveTimesheetTask.setEscalateCount(0);
			myActiveTaskDTOs.add(myActiveTimesheetTask);
			
			Long expenseCount = expenseReportViewRepository.getCountByUserIdAndStatus(ids, SUBMITTED); 
			MyActiveTaskDTO myActiveExpenseTask = new MyActiveTaskDTO();
			myActiveExpenseTask.setTaskName(EXPENSE_APPROVAL);
			myActiveExpenseTask.setApprovalCount(expenseCount == null ? 0 : Integer.parseInt(String.valueOf(expenseCount)));
			myActiveExpenseTask.setEscalateCount(0);
			myActiveTaskDTOs.add(myActiveExpenseTask);
	
			Integer invoiceCount = getInvoiceCountByStatusAndId();
			MyActiveTaskDTO myActiveInvoiceTask = new MyActiveTaskDTO();
			myActiveInvoiceTask.setTaskName(INVOICE_APPROVAL);
			myActiveInvoiceTask.setApprovalCount(invoiceCount == null ? 0 : invoiceCount);
			myActiveInvoiceTask.setEscalateCount(0);
			myActiveTaskDTOs.add(myActiveInvoiceTask);
			
			Integer timeoffCount = getTimeoffCountByStatusAndId(AWAITING_APPROVAL);
			MyActiveTaskDTO myActiveTimeoffTask = new MyActiveTaskDTO();
			myActiveTimeoffTask.setTaskName(TIMEOFF_APPROVAL);
			myActiveTimeoffTask.setApprovalCount(timeoffCount == null ? 0 : timeoffCount);
			myActiveTimeoffTask.setEscalateCount(0);
			myActiveTaskDTOs.add(myActiveTimeoffTask);
		}
		
		Integer overdueCount = getTimesheetCountByStatusAndId(OVERDUE, "false");
		MyActiveTaskDTO myActiveTimesheetOverdueTask = new MyActiveTaskDTO();
		myActiveTimesheetOverdueTask.setTaskName(OVERDUE_COUNT);
		myActiveTimesheetOverdueTask.setApprovalCount(overdueCount == null ? 0 : overdueCount);
		myActiveTimesheetOverdueTask.setEscalateCount(0);
		myActiveTaskDTOs.add(myActiveTimesheetOverdueTask);
		
		String timesheetSubmissionDate = getTimesheetSubmissionDateById(NOT_SUBMITTED);
		if (timesheetSubmissionDate != null) {
			MyActiveTaskDTO timesheetSubmissionTask = new MyActiveTaskDTO();
			timesheetSubmissionTask.setTaskName(TIMESHEET_SUBMISSION);
			timesheetSubmissionTask.setTimesheetSubmissionDate(timesheetSubmissionDate);
			myActiveTaskDTOs.add(timesheetSubmissionTask);
		}
		
		if (!myActiveTaskDTOs.isEmpty()) {
			myActiveTaskMap.put("myTasks", myActiveTaskDTOs);
		}

		return myActiveTaskMap;
	}
	
	private Integer getTimesheetCountByStatusAndId(String status, String isApproval) {

		String url = getDiscoveryInstanceUrl(TIMESHEET_GROUP_KEY);
		url = url.startsWith(EmployeeConstants.HTTP) ? url : new StringBuilder(EmployeeConstants.HTTP).append(url).toString();
		url = new StringBuilder(url).append(TIMESHEET_COUNT_URI).append(status).append("/").append(isApproval).toString();
		
		DashboardWidgetsRestTemplate<Integer> dashboardRestTemplate = new DashboardWidgetsRestTemplate<>(restTemplate, 
				url, DiscoveryClientAndAccessTokenUtil.getAccessToken(), HttpMethod.GET);
		
		return dashboardRestTemplate.getResponse();	
	}
	
	private Integer getTimeoffCountByStatusAndId(String status) {

		String url = getDiscoveryInstanceUrl(TIMESHEET_GROUP_KEY);
		url = url.startsWith(EmployeeConstants.HTTP) ? url : new StringBuilder(EmployeeConstants.HTTP).append(url).toString();
		url = new StringBuilder(url).append(TIMEOFF_COUNT_URI).append(status).toString();
		
		DashboardWidgetsRestTemplate<Integer> dashboardRestTemplate = new DashboardWidgetsRestTemplate<>(restTemplate, 
				url, DiscoveryClientAndAccessTokenUtil.getAccessToken(), HttpMethod.GET);
		
		return dashboardRestTemplate.getResponse();
	}
	
	private Integer getInvoiceCountByStatusAndId() {

		String url = getDiscoveryInstanceUrl(INVOICE_GROUP_KEY);
		url = url.startsWith(EmployeeConstants.HTTP) ? url : new StringBuilder(EmployeeConstants.HTTP).append(url).toString();
		url = new StringBuilder(url).append(INVOICE_COUNT_URI).toString();
		
		DashboardWidgetsRestTemplate<Integer> dashboardRestTemplate = new DashboardWidgetsRestTemplate<>(restTemplate, 
				url, DiscoveryClientAndAccessTokenUtil.getAccessToken(), HttpMethod.GET);
		
		return dashboardRestTemplate.getResponse();
	}
	
	private String getTimesheetSubmissionDateById(String status) {
		
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
		String currentDate = localDate.format(formatter);
		
		String url = getDiscoveryInstanceUrl(TIMESHEET_GROUP_KEY);
		url = url.startsWith(EmployeeConstants.HTTP) ? url : new StringBuilder(EmployeeConstants.HTTP).append(url).toString();
		url = new StringBuilder(url).append(TIMESHEET_SUBMISSION_URI).append(status).append("/").append(currentDate).toString();
		
		DashboardWidgetsRestTemplate<String> dashboardRestTemplate = new DashboardWidgetsRestTemplate<>(restTemplate, 
				url, DiscoveryClientAndAccessTokenUtil.getAccessToken(), HttpMethod.GET);
		
		return dashboardRestTemplate.getResponse();
	}
	
	private void getDobTexts(EmployeeProfile employeeProfile, LocalDate today, LocalDate upcoming, LocalDate recent,
			List<String> todayBirthdayAnniversaryTexts, List<String> upcomingBirthdayAnniversaryTexts,
			List<String> recentBirthdayAnniversaryTexts) {
		
		String employeeName = employeeProfile.getFirstName() + " " + employeeProfile.getLastName();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
		String gender = null;
		if (employeeProfile.getGender() != null && employeeProfile.getGender().equalsIgnoreCase(GENDER_MALE)) {
			gender = "his";
		} else {
			gender = "her";
		}
		Date dob = employeeProfile.getDob();
		if (Objects.nonNull(dob)) {
			
			Calendar dobCal = Calendar.getInstance();
			dobCal.setTime(dob);
			int dobDay = dobCal.get(Calendar.DATE);
			int dobMonth = dobCal.get(Calendar.MONTH) + 1;
			LocalDate thisYearDob = LocalDate.of(today.getYear(), dobMonth, dobDay);
			Date thisYearDobDate = Date.from(thisYearDob.atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			if (thisYearDob.isEqual(today)) {
				String content = TODAY_BIRTHDAY_STRING;
				todayBirthdayAnniversaryTexts.add(String.format(content, employeeName, gender, employeeProfile.getEmployeeId()));
			}
			else if (thisYearDob.isAfter(today) && thisYearDob.isBefore(upcoming)) {
				String content = UPCOMING_BIRTHDAY_STRING;
				upcomingBirthdayAnniversaryTexts.add(String.format(content, employeeName, gender, simpleDateFormat.format(thisYearDobDate), employeeProfile.getEmployeeId()));
			}
			else if (thisYearDob.isBefore(today) && thisYearDob.isAfter(recent)) {
				String content = RECENT_BIRTHDAY_STRING;
				recentBirthdayAnniversaryTexts.add(String.format(content, employeeName, gender, simpleDateFormat.format(thisYearDobDate), employeeProfile.getEmployeeId()));
			}
		}
	}
	
	private void getDojTexts(EmployeeProfile employeeProfile, LocalDate today, LocalDate upcoming, LocalDate recent,
			List<String> todayBirthdayAnniversaryTexts, List<String> upcomingBirthdayAnniversaryTexts,
			List<String> recentBirthdayAnniversaryTexts) {
		
		String employeeName = employeeProfile.getFirstName() + " " + employeeProfile.getLastName();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy");
		String gender = null;
		if (employeeProfile.getGender() != null && employeeProfile.getGender().equalsIgnoreCase(GENDER_MALE)) {
			gender = "his";
		} else {
			gender = "her";
		}
		Date doj =  employeeProfile.getDateJoin();
		if (Objects.nonNull(doj)) {
			
			Calendar dojCal = Calendar.getInstance();
			dojCal.setTime(doj);
			int dojDay = dojCal.get(Calendar.DATE);
			int dojMonth = dojCal.get(Calendar.MONTH) + 1;
			int completedYear = today.getYear() - dojCal.get(Calendar.YEAR);
			LocalDate thisYearDoj = LocalDate.of(today.getYear(), dojMonth, dojDay);
			Date thisYearDojDate = Date.from(thisYearDoj.atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			if (completedYear != 0) {
				if (thisYearDoj.isEqual(today)) {
					String content = TODAY_ANNIVERSARY_STRING;
					todayBirthdayAnniversaryTexts.add(String.format(content, employeeName, gender, completedYear, employeeProfile.getEmployeeId()));
				}
				else if (thisYearDoj.isAfter(today) && thisYearDoj.isBefore(upcoming)) {
					String content = UPCOMING_ANNIVERSARY_STRING;
					upcomingBirthdayAnniversaryTexts.add(String.format(content, employeeName, gender, completedYear, simpleDateFormat.format(thisYearDojDate), employeeProfile.getEmployeeId()));
				}
				else if (thisYearDoj.isBefore(today) && thisYearDoj.isAfter(recent)) {
					String content = RECENT_ANNIVERSARY_STRING;
					recentBirthdayAnniversaryTexts.add(String.format(content, employeeName, gender, completedYear, simpleDateFormat.format(thisYearDojDate), employeeProfile.getEmployeeId()));
				}
			}
		}
	}

	private String getDiscoveryInstanceUrl(String groupKey) {
		return DiscoveryClientAndAccessTokenUtil.discoveryClient(groupKey, discoveryClient);
	}
	
	private Long getEmployeeId() throws ParseException {

		String token = DiscoveryClientAndAccessTokenUtil.getAccessToken();
		JWTClaimsSet claimsSet = JWTParser.parse(token).getJWTClaimsSet();
		String email = (String) claimsSet.getClaim("email");
		Map<String, Object> employeeDetailsMap = employeeProfileRepository.getEmployeeByMail(email, contactTypeEnum.email);
		if (!employeeDetailsMap.isEmpty() && employeeDetailsMap.get("employeeId") != null) {
			return Long.valueOf(String.valueOf(employeeDetailsMap.get("employeeId")));
		}
		
		return null;
	}
	
	
	@Override
	public Map<String, Object> getNewsLetter() throws ParseException {
		
		Map<String,Object> map=new HashMap<>();
		map.put("newsLetterList", newsLetterRepository.findAll());
		return map;
	}
	
	@Override
	public Map<String, Object> getAccolades() throws ParseException {
		Map<String,Object> map=new HashMap<>();
		map.put("accoladesList", accoladesRepository.findAll());
		return map;
	}
	
	@Override
	public Map<String, Object> getAnnouncement() throws ParseException {
		Map<String,Object> map=new HashMap<>();
		map.put("announcementList", announcementRepository.findAll());
		return map;
	}
}
