package com.tm.timesheet.web.rest;

import java.text.ParseException;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.RequiredAuthority;
import com.tm.commonapi.web.rest.util.HeaderUtil;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.service.TimesheetService;
import com.tm.timesheet.service.dto.CommonTimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/my_timesheets_verification")
@Api(value = "my_timesheets_verification", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class SubmitterTimesheetVerificationResource {

	private TimesheetService timesheetService;

	public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";
	public static final String TIMESHEET_TYPE = "Verification";

	@Inject
	public SubmitterTimesheetVerificationResource(TimesheetService timesheetService) {
		this.timesheetService = timesheetService;
	}

	@ApiOperation(value = "Submit My-Timesheet", notes = "This will submit timesheet by submitter")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Submit My-Timesheet"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequestMapping(value = "/{timesheetId}/submitter", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_VERIFIER })
	public ResponseEntity<TimesheetDTO> submitTimesheet(@PathVariable String timesheetId,
			@RequestBody CommonTimesheetDTO commonTimesheetDTO) throws ParseException {
		commonTimesheetDTO.setTimesheetType(TIMESHEET_TYPE);
		timesheetService.submitTimesheet(commonTimesheetDTO);
		TimesheetDTO timesheetDTO = new TimesheetDTO();
		timesheetDTO.setStatus(TimesheetConstants.OK);
		timesheetDTO.setPaidStatus(null);
		return new ResponseEntity<>(timesheetDTO, HttpStatus.OK);
	}

	@ApiOperation(value = "Timer My-Timesheet", notes = "This will start and stop timer by submitter")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Timer My-Timesheet"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequestMapping(value = "/{timesheetId}/timer", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_VERIFIER })
	public CommonTimesheetDTO updateTimer(@PathVariable String timesheetId,
			@RequestBody CommonTimesheetDTO commonTimesheetDTO) throws ParseException {
		commonTimesheetDTO.setTimesheetType(TIMESHEET_TYPE);
		return timesheetService.timerTimesheet(commonTimesheetDTO);
	}

	@ApiOperation(value = "Dispute Team-Timesheet", notes = "This will update particular timesheet status based on current one.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Update Verification-Timesheet"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequestMapping(value = "/{timesheetId}/dispute", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_VERIFIER })
	public ResponseEntity<String> reopenTimesheet(@PathVariable UUID timesheetId) throws ParseException {
		timesheetService.disputeTimesheet(timesheetId);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("timesheetId", timesheetId.toString()))
				.body("OK");
	}

}
