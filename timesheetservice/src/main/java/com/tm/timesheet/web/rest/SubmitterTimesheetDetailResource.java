package com.tm.timesheet.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.text.ParseException;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.RequiredAuthority;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.service.TimesheetService;
import com.tm.timesheet.service.dto.TimesheetDetailsDTO;

@RestController
@RequestMapping(value = "/my_timesheets")
@Api(value = "my_timesheets", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class SubmitterTimesheetDetailResource {

	private TimesheetService timesheetService;

	public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";

	@Inject
	public SubmitterTimesheetDetailResource(TimesheetService timesheetService) {
		this.timesheetService = timesheetService;
	}

	@ApiOperation(value = "Update My-Timesheet Detail", notes = "This will update timesheet details by submitter")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Upeate My-Timesheet Detail"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequestMapping(value = "/{timesheetDetailId}/timesheetDetail", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({
			TimesheetConstants.TIMESHEET_SUBMITTER, TimesheetConstants.TIMESHEET_APPROVER,
			TimesheetConstants.PROFILE_VIEW, TimesheetConstants.TIMESHEET_VERIFIER, TimesheetConstants.RECRUITER_AUTH,
			TimesheetConstants.TIMESHEET_PAYROLL_APPROVER, TimesheetConstants.ACCOUNT_MANAGER })
	public TimesheetDetailsDTO updateTimesheetDetail(@PathVariable String timesheetDetailId,
			@RequestBody TimesheetDetailsDTO timesheetDetailsDTO, String lookUpType) throws ParseException {
		return timesheetService.updateTimesheet(timesheetDetailsDTO, lookUpType);
	}

	@ApiOperation(value = "Timer My-Timesheet", notes = "This will start and stop timer by submitter")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Timer My-Timesheet"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequestMapping(value = "/{timesheetId}/timesheetDetail/timer", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({
		TimesheetConstants.TIMESHEET_SUBMITTER, TimesheetConstants.TIMESHEET_APPROVER,
		TimesheetConstants.PROFILE_VIEW, TimesheetConstants.TIMESHEET_VERIFIER, TimesheetConstants.RECRUITER_AUTH,
		TimesheetConstants.TIMESHEET_PAYROLL_APPROVER, TimesheetConstants.ACCOUNT_MANAGER })
	public TimesheetDetailsDTO updateTimer(@PathVariable String timesheetId,
			@RequestBody TimesheetDetailsDTO timesheetDetailsDTO) throws ParseException {
		return timesheetService.timerTimesheet(timesheetDetailsDTO);
	}
}
