package com.tm.timesheet.timesheetview.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.RequiredAuthority;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.service.dto.TimesheetMobileDTO;
import com.tm.timesheet.timesheetview.service.TimesheetViewService;

@RestController
@RequestMapping(value = "/my-timesheets")
@Api(value = "my-timesheets", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class SubmitterTimesheetDetailViewResource {

	private TimesheetViewService timesheetViewService;

	public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";

	@Inject
	public SubmitterTimesheetDetailViewResource(TimesheetViewService timesheetViewService) {
		this.timesheetViewService = timesheetViewService;
	}

	@RequestMapping(value = "/timesheetDetail/{timesheetId}", method = RequestMethod.GET)
	@ApiOperation(value = "Getting Particular Timesheets Details", notes = "This service will get the timesheet hour entry,units entry,timestamp entry,timer entry and timeoff entry details for particular employee.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimesheetDetailsDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER,
			TimesheetConstants.TIMESHEET_SUBMITTER, TimesheetConstants.TIMESHEET_APPROVER,
			TimesheetConstants.PROFILE_VIEW, TimesheetConstants.TIMESHEET_VERIFIER, TimesheetConstants.RECRUITER_AUTH,
			TimesheetConstants.TIMESHEET_PAYROLL_APPROVER, TimesheetConstants.FINANCE_MANAGER,
			TimesheetConstants.FINANCE_REPRESENTATIVE, TimesheetConstants.ACCOUNT_MANAGER })
	public TimesheetMobileDTO getTimesheetDetailsInMobile(@PathVariable UUID timesheetId, String timesheetDate) {
		return timesheetViewService.getTimesheetDetailsInMobile(timesheetId, timesheetDate, false);
	}

}
