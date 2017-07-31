package com.tm.timesheet.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.testng.annotations.DataProvider;

import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.domain.UploadFilesDetails;
import com.tm.timesheet.domain.UploadLogs;
import com.tm.timesheet.timeoff.web.rest.util.DateUtil;

public class TimesheetTestDataProvider {
	private static String timesheetId = "000181d4-2b11-8702-0035-111f1f15f771";
	private static final String APPROVED = "APPROVED";
	
	@DataProvider(name = "testDisputTimesheet")
	public static Iterator<Object[]> testDisputTimesheet(){
		Timesheet timesheet = getTimesheet();
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { timesheet, timesheet.getId()});
		return testData.iterator();
	}
	
	@DataProvider(name = "getUploadFilesDetails")
	public static  Iterator<Object[]> uploadFilesDetails() {
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		UploadFilesDetails uploadFilesDetails = getuploadFilesDetails();
		testData.add(new Object[] {uploadFilesDetails});
		return testData.iterator();
	}
	
	@DataProvider(name = "getAllUploadFilesDetails")
	public static  Iterator<Object[]> getAllUploadFilesDetails() {
		Pageable pageable = null;
		UploadFilesDetails uploadFilesDetails = getuploadFilesDetails();
		List<UploadFilesDetails> uploadFilesDetailsList = new ArrayList<>();
		uploadFilesDetailsList.add(uploadFilesDetails);
		Page<UploadFilesDetails> pageUploadFilesDetails = new PageImpl<>(uploadFilesDetailsList, pageable, uploadFilesDetailsList.size()+1);
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { pageUploadFilesDetails, pageable});
		return testData.iterator();
	}
	
	@DataProvider(name = "getAllUploadLogs")
	public static Iterator<Object[]> getAllUploadLogs(){
		UploadLogs uploadLogs = getUploadLogs();
		List<UploadLogs> uploadLogsList = new ArrayList<>();
		uploadLogsList.add(uploadLogs);
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { uploadLogsList});
		return testData.iterator();
	}
	
	private static UploadLogs getUploadLogs(){
		UploadLogs uploadlogs = new UploadLogs();
		uploadlogs.setId(UUID.randomUUID());
		uploadlogs.setEmployeeName("smi_123");
		uploadlogs.setEngagementName("innopeople");
		uploadlogs.setTotalHours(1000.00);
		return uploadlogs;
	}
	
	private static UploadFilesDetails getuploadFilesDetails(){
		UploadFilesDetails uploadFilesDetails = new UploadFilesDetails();
		uploadFilesDetails.setOriginalUploadedFileName("customerName");
		uploadFilesDetails.setUploadedFileName("customerNames");
		uploadFilesDetails.setFailedRecords(1);
		uploadFilesDetails.setPassedRecords(2);
		uploadFilesDetails.setProceededRecords(4);
		uploadFilesDetails.setPassedTimesheetRecords(10);
		uploadFilesDetails.setUploaddate(new Date());
		uploadFilesDetails.setId(UUID.randomUUID());
		return uploadFilesDetails;
	}
	
	private static Timesheet getTimesheet(){
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
	
}
