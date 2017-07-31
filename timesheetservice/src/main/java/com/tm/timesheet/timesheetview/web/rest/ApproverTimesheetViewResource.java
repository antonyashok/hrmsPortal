package com.tm.timesheet.timesheetview.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tm.commonapi.exception.FileUploadException;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.Status;
import com.tm.timesheet.service.dto.ActivityLogDTO;
import com.tm.timesheet.service.dto.CommentsDTO;
import com.tm.timesheet.service.dto.TimesheetAttachmentsDTO;
import com.tm.timesheet.service.dto.TimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetResourceDTO;
import com.tm.timesheet.service.dto.TimesheetStatusCountSummary;
import com.tm.timesheet.timesheetview.constants.TimesheetViewConstants;
import com.tm.timesheet.timesheetview.exception.ActivityLogNotFoundException;
import com.tm.timesheet.timesheetview.exception.CommentsNotFoundException;
import com.tm.timesheet.timesheetview.resource.assembler.TimesheetStatusViewResourceAssembler;
import com.tm.timesheet.timesheetview.resource.assembler.TimesheetViewResourceAssembler;
import com.tm.timesheet.timesheetview.service.TimesheetViewService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/team-timesheets")
@Api(value = "team-timesheets", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class ApproverTimesheetViewResource {

	private TimesheetViewService timesheetViewService;

	private TimesheetStatusViewResourceAssembler timesheetStatusViewResourceAssembler;

	private TimesheetViewResourceAssembler timesheetViewResourceAssembler;

	public static final String TIMESHEET_TYPE = "Approver";

	@Inject
	public ApproverTimesheetViewResource(TimesheetViewService timesheetViewService,
			TimesheetStatusViewResourceAssembler timesheetStatusViewResourceAssembler,
			TimesheetViewResourceAssembler timesheetViewResourceAssembler) {
		this.timesheetViewService = timesheetViewService;
		this.timesheetStatusViewResourceAssembler = timesheetStatusViewResourceAssembler;
		this.timesheetViewResourceAssembler = timesheetViewResourceAssembler;
	}

	@ApiOperation(value = "Getting the status count", notes = "This will get timesheets status count of all the employees under this approver")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "StatusCountDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER,
			 TimesheetConstants.ACCOUNT_MANAGER })
	public TimesheetStatusCountSummary getStatusCount(@RequestParam String startDate, @RequestParam String endDate,
			@RequestParam(required = true) String employeeType, @RequestParam String searchParam)
			throws ParseException {
		TimesheetStatusCountSummary result = timesheetViewService.getStatusCount(TimesheetViewConstants.APPROVER,
				startDate, endDate, employeeType, searchParam, TIMESHEET_TYPE);
		return timesheetStatusViewResourceAssembler.toManagerStatusCountResource(result, employeeType, searchParam);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value = "Getting All Timesheets", notes = "This will get all timesheets of all the employees under this approver")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimesheetDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER,
		 TimesheetConstants.ACCOUNT_MANAGER })
	public PagedResources<TimesheetDTO> getAllTeamTimesheets(Pageable pageable,
			PagedResourcesAssembler<TimesheetDTO> pagedAssembler, String startDate, String endDate, String searchParam,
			String status, @RequestParam(required = true) String employeeType) throws ParseException {
		Page<TimesheetDTO> result = timesheetViewService.getAllTimesheets(pageable, status, startDate, endDate,
				searchParam, TimesheetViewConstants.APPROVER, employeeType, null, TIMESHEET_TYPE);
		return pagedAssembler.toResource(result, timesheetViewResourceAssembler);
	}

	@RequestMapping(value = "/my-timesheets", method = RequestMethod.GET)
	@ApiOperation(value = "Getting All Timesheets Details", notes = "This REST service will get the timesheet of the logged in user for the given status")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimesheetDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER,
		 TimesheetConstants.ACCOUNT_MANAGER })
	public ResponseEntity<List<TimesheetDTO>> getTimesheetsDetail(String startDate, String endDate) {
		List<TimesheetDTO> timeoffStatus = timesheetViewService.getTimesheetsDetail(startDate, endDate);
		return new ResponseEntity<>(timeoffStatus, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/activities", method = RequestMethod.GET, produces = { APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Getting the activity logs", notes = "This will get all the activity logs for particular employee.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "List<ActivityLogDTO>"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER,
		 TimesheetConstants.ACCOUNT_MANAGER,TimesheetConstants.TIMESHEET_PAYROLL_APPROVER })
	public ResponseEntity<List<ActivityLogDTO>> getActivityLog(@PathVariable UUID id) {
		List<ActivityLogDTO> activityLogDTOs = timesheetViewService.getActivityLog(id);
		return Optional.ofNullable(activityLogDTOs).map(result -> new ResponseEntity<>(activityLogDTOs, HttpStatus.OK))
				.orElseThrow(() -> new ActivityLogNotFoundException());

	}

	@RequestMapping(value = "/{id}/comments", method = RequestMethod.GET, produces = { APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Getting the timesheet comments", notes = "This will get all the timesheet comments for particular employee.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "CommentsDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER,
		 TimesheetConstants.ACCOUNT_MANAGER,TimesheetConstants.TIMESHEET_PAYROLL_APPROVER })
	public ResponseEntity<CommentsDTO> getComments(@PathVariable UUID id) throws ParseException {
		CommentsDTO commentsDTO = timesheetViewService.getComments(id);
		return Optional.ofNullable(commentsDTO).map(result -> new ResponseEntity<>(commentsDTO, HttpStatus.OK))
				.orElseThrow(() -> new CommentsNotFoundException());
	}

	@RequestMapping(value = "/{timesheetId}", method = RequestMethod.GET)
	@ApiOperation(value = "Getting Particular Timesheets Details", notes = "This service will get the timesheet hour entry,units entry,timestamp entry,timer entry and timeoff entry details for particular employee")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimesheetResourceDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_APPROVER,
		 TimesheetConstants.ACCOUNT_MANAGER,TimesheetConstants.TIMESHEET_PAYROLL_APPROVER })
	public TimesheetResourceDTO getTimesheetDetails(@PathVariable UUID timesheetId) throws ParseException {
		TimesheetDTO result = timesheetViewService.getTimesheetDetails(timesheetId, true);
		return timesheetViewResourceAssembler.toTimesheetDetailsResource(result);
	}

	@RequestMapping(value = "/{timesheetId}/fileDetails", method = RequestMethod.GET)
	@ApiOperation(value = "get Timesheet's attachment details", notes = "This will get timesheet's attachments of a particular Employee.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "ok") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_APPROVER, TimesheetConstants.TIMESHEET_APPROVER,
			 TimesheetConstants.ACCOUNT_MANAGER,TimesheetConstants.TIMESHEET_PAYROLL_APPROVER })
	public List<TimesheetAttachmentsDTO> getTimesheetFileDetails(@PathVariable("timesheetId") String timesheetId)
			throws FileUploadException {
		UUID timesheetUUID = UUID.fromString(timesheetId);
		return timesheetViewService.getTimesheetFileDetails(timesheetUUID);
	}

	@RequestMapping(value = "/file/{timesheetAttachmentId}", method = RequestMethod.GET)
	@ApiOperation(value = "view Timesheet's attachment", notes = "This will show the selected timesheet's attachments of a particular Employee timesheet.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "ok") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_APPROVER, TimesheetConstants.TIMESHEET_APPROVER,
			 TimesheetConstants.ACCOUNT_MANAGER,TimesheetConstants.TIMESHEET_PAYROLL_APPROVER })
	public TimesheetAttachmentsDTO getTimesheetFile(@PathVariable("timesheetAttachmentId") String timesheetAttachmentId)
			throws FileUploadException, IOException {
		return timesheetViewService.getTimesheetFile(timesheetAttachmentId);
	}

	@RequestMapping(value = "/{timesheetId}/files", method = RequestMethod.POST)
	@ApiOperation(value = "upload Team Timesheet's attachments", notes = "This will upload timesheet's attachments of a particular Employee.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "ok") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_APPROVER, TimesheetConstants.TIMESHEET_APPROVER,
			 TimesheetConstants.ACCOUNT_MANAGER ,TimesheetConstants.TIMESHEET_PAYROLL_APPROVER })
	public @ResponseBody Status uploadMultipleTimesheetFiles(@RequestPart("files") MultipartFile[] files,
			@PathVariable("timesheetId") UUID timesheetId) throws FileUploadException, ParseException, IOException {
		timesheetViewService.uploadMultipleTimesheetFiles(files, timesheetId);
		return new Status("OK");
	}

	@RequestMapping(value = "/rctr-ts-status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_APPROVER, TimesheetConstants.TIMESHEET_APPROVER,
			 TimesheetConstants.ACCOUNT_MANAGER,TimesheetConstants.RECRUITER })
	public TimesheetStatusCountSummary getTimesheetStatusCountForRecruiter(@RequestParam String startDate,
			@RequestParam String endDate, @RequestParam(required = true) String employeeType,
			@RequestParam String searchParam) throws ParseException {
		TimesheetStatusCountSummary result = timesheetViewService.getRecruiterStatusCount(
				TimesheetViewConstants.APPROVER, startDate, endDate, employeeType, searchParam, TIMESHEET_TYPE);
		return timesheetStatusViewResourceAssembler.toManagerStatusCountResource(result, employeeType, searchParam);
	}

	@RequestMapping(value = "/rctr-ts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_APPROVER, TimesheetConstants.TIMESHEET_APPROVER,
			 TimesheetConstants.ACCOUNT_MANAGER,TimesheetConstants.RECRUITER})
	public PagedResources<TimesheetDTO> getAllTeamTimesheetsForRecruiter(Pageable pageable,
			PagedResourcesAssembler<TimesheetDTO> pagedAssembler, String startDate, String endDate, String searchParam,
			String status, @RequestParam(required = true) String employeeType) throws ParseException {
		Page<TimesheetDTO> result = timesheetViewService.getAllRecruiterTimesheets(pageable, status, startDate, endDate,
				searchParam, TimesheetViewConstants.APPROVER, employeeType, null, TIMESHEET_TYPE);
		return pagedAssembler.toResource(result, timesheetViewResourceAssembler);
	}
}
