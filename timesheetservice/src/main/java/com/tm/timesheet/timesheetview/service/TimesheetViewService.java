package com.tm.timesheet.timesheetview.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.tm.commonapi.exception.FileUploadException;
import com.tm.timesheet.configuration.service.dto.OfficeLocationDTO;
import com.tm.timesheet.service.dto.ActivityLogDTO;
import com.tm.timesheet.service.dto.CommentsDTO;
import com.tm.timesheet.service.dto.TimesheetAttachmentsDTO;
import com.tm.timesheet.service.dto.TimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetMobileDTO;
import com.tm.timesheet.service.dto.TimesheetStatusCountSummary;
import com.tm.timesheet.timesheetview.exception.InvalidDateRangeException;

public interface TimesheetViewService {

	TimesheetStatusCountSummary getStatusCount(String actorType, String startDate, String endDate, String employeeType,
			String searchParam,String timesheetType) throws ParseException, InvalidDateRangeException;
	
	TimesheetStatusCountSummary getRecruiterStatusCount(String actorType, String startDate,
            String endDate, String employeeType, String searchParam,String timesheetType)
            throws ParseException, InvalidDateRangeException;

    public Page<TimesheetDTO> getAllTimesheets(Pageable pageable, String status, String startDate,
            String endDate, String searchParam, String actorType,
            String employeeType ,String office,String timesheetType) throws ParseException;
    
    Page<TimesheetDTO> getAllRecruiterTimesheets(Pageable pageable, String status, String startDate,
            String endDate, String searchParam,  String actorType,
            String employeeType, String office,String timesheetType) throws ParseException;

    public TimesheetDTO getTimesheetDetails(UUID id, Boolean isApprover) throws ParseException;
    
    public TimesheetMobileDTO getTimesheetDetailsInMobile(UUID id,String timesheetDate, Boolean isApprover);

    public List<TimesheetDTO> getTimesheetsDetail(String startDate,
            String endDate);

    public List<ActivityLogDTO> getActivityLog(UUID id);

    public CommentsDTO getComments(UUID id) throws ParseException;

    public List<TimesheetAttachmentsDTO> uploadMultipleTimesheetFiles(MultipartFile[] files,UUID timesheetId)
            throws ParseException,FileUploadException, IOException;

    public List<TimesheetAttachmentsDTO> getTimesheetFileDetails(UUID timesheetId);

    public TimesheetAttachmentsDTO getTimesheetFile(String timesheetAttachmentId)
            throws IOException;

    public String deleteTimesheetFile(String timesheetAttachmentId)
            throws FileUploadException, ParseException;
    
    public List<OfficeLocationDTO> getOfficeLocations(String actorType);

	TimesheetStatusCountSummary getPayrollStatusCount(String actorType, String startDate, String endDate,
			String employeeType, String searchParam, String timesheetType, String office)
			throws ParseException, InvalidDateRangeException;


}
