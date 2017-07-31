/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.service.TimesheetService.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.domain.TimesheetTemplate;
import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.domain.UploadLogs;
import com.tm.timesheet.service.dto.CommonTimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetDetailsDTO;
import com.tm.timesheet.service.dto.UploadFilesDetailsDTO;

public interface TimesheetService {

	CommonTimesheetDTO updateTimesheet(CommonTimesheetDTO commonTimesheetDTO)
			throws ParseException;

	void submitTimesheet(CommonTimesheetDTO commonTimesheetDTO)
			throws ParseException;

	TimesheetDTO bulkSubmitTimesheet(CommonTimesheetDTO commonTimesheetDTO)
			throws ParseException;

	CommonTimesheetDTO timerTimesheet(CommonTimesheetDTO commonTimesheetDTO)
			throws ParseException;

	TimesheetDTO approveTimesheet(String timeSheetId,
			CommonTimesheetDTO commonTimesheetDTO) throws ParseException;

	TimesheetDTO bulkApproveTimesheet(CommonTimesheetDTO commonTimesheetDTO)
			throws ParseException;

	void reopenTimesheet(String timesheetId) throws ParseException;

	TimesheetDetailsDTO updateTimesheet(
			TimesheetDetailsDTO timesheetDetailsDTO, String lookUpType)
			throws ParseException;
	
	void disputeTimesheet(UUID timesheetId) throws ParseException;
	Map<String, Object> readTimesheetExcel(InputStream inputStream, String fileName) throws IOException;
	
	Page<UploadFilesDetailsDTO> getAllUploadFilesDetails(Pageable pageable);
	List<UploadLogs> getAllUploadLogs(String fileName);

	TimesheetDetailsDTO timerTimesheet(TimesheetDetailsDTO timesheetDetailsDTO) throws ParseException;
	
	Timesheet getTimesheet(UUID timeSheetId);

	EmployeeProfileDTO getLoggedInUser();

	CommonTimesheetDTO updatePayrollTimesheet(CommonTimesheetDTO commonTimesheetDTO) throws ParseException;

	Long getCountByUserIdAndStatus(Long reportingManagerId, String status, boolean isApproval) throws ParseException;
	
	String getTimesheetEndDateByStatusForEmployee(String status, Long employeeId, String date) throws ParseException;

	TimesheetTemplate getTimesheetTemplate(Long timesheetTemplateId);
}
