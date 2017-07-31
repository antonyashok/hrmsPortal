package com.tm.timesheet.web.rest;

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
import com.tm.timesheet.service.dto.CommonTimesheetDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/team_timesheets")
@Api(value = "team_timesheets", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class PayrollTimesheetResource {

	private TimesheetService timesheetService;

	public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";

	@Inject
	public PayrollTimesheetResource(TimesheetService timesheetService) {
		this.timesheetService = timesheetService;
	}

	@ApiOperation(value = "Update Payroll-Timesheet", notes = "This will update timesheet details by payroll user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Upeate Payroll-Timesheet"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequestMapping(value = "/{timesheetId}/payroll", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({TimesheetConstants.TIMESHEET_PAYROLL_APPROVER,
			TimesheetConstants.ACCOUNT_MANAGER })
	public CommonTimesheetDTO updateTimesheet(@PathVariable String timesheetId,
			@RequestBody CommonTimesheetDTO commonTimesheetDTO) throws ParseException {
		return timesheetService.updatePayrollTimesheet(commonTimesheetDTO);
	}

}
