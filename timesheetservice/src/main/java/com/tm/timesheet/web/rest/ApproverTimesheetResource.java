package com.tm.timesheet.web.rest;

import java.text.ParseException;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.service.TimesheetService;
import com.tm.timesheet.service.dto.CommonTimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/team_timesheets")
@Api(value = "team_timesheets", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class ApproverTimesheetResource {

	private TimesheetService timesheetService;

	public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";

	@Inject
	public ApproverTimesheetResource(TimesheetService timesheetService) {
		this.timesheetService = timesheetService;
	}

	@ApiOperation(value = "Submit Team-Timesheet", notes = "This will submit team-timesheet by approver")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Submit Team-Timesheet"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequestMapping(value = "/{timesheetId}/approver", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ 
			TimesheetConstants.TIMESHEET_APPROVER})
	public ResponseEntity<TimesheetDTO> approveTimesheet(@PathVariable String timesheetId,
			@RequestBody CommonTimesheetDTO commonTimesheetDTO) throws ParseException {
		TimesheetDTO timesheetDTO = timesheetService.approveTimesheet(timesheetId, commonTimesheetDTO);
		return new ResponseEntity<>(timesheetDTO, HttpStatus.OK);
	}

	@ApiOperation(value = "Submit All Team-Timesheet", notes = "This will submit one or more team-timesheet by approver")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Submit All Team-Timesheet"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequestMapping(value = "/approver", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ 
		TimesheetConstants.TIMESHEET_APPROVER})
	public ResponseEntity<TimesheetDTO> bulkApproveTimesheet(@RequestBody CommonTimesheetDTO commonTimesheetDTO)
			throws ParseException {
		TimesheetDTO timesheetDTO = timesheetService.bulkApproveTimesheet(commonTimesheetDTO);
		return new ResponseEntity<>(timesheetDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/count/{status}/{isapproval}", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.ALL })
	public ResponseEntity<Long> getCountByUserIdAndStatus(@PathVariable("status") String status, @PathVariable("isapproval") String isApproval) throws ParseException {
		return new ResponseEntity<>(timesheetService.getCountByUserIdAndStatus(
				timesheetService.getLoggedInUser().getEmployeeId(), status, Boolean.valueOf(isApproval)), HttpStatus.OK);
	}

	@RequestMapping(value = "/gettimesheetenddatebystatusforemployee/{status}/{date}", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.ALL })
	public ResponseEntity<String> getTimesheetEndDateByStatusForEmployee(@PathVariable("status") String status, 
			@PathVariable("date") String date) throws ParseException {
		return new ResponseEntity<>(timesheetService.getTimesheetEndDateByStatusForEmployee(status,
				timesheetService.getLoggedInUser().getEmployeeId(), date), HttpStatus.OK);
	}
}
