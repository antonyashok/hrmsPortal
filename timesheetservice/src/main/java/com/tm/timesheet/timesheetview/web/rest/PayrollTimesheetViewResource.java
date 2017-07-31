package com.tm.timesheet.timesheetview.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
import org.springframework.http.HttpHeaders;
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
import com.tm.timesheet.configuration.service.dto.OfficeLocationDTO;
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

@RestController
@RequestMapping(value = "/team-timesheets")
@Api(value = "team-timesheets", produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class PayrollTimesheetViewResource {

	private TimesheetViewService timesheetViewService;

	private TimesheetViewResourceAssembler timesheetViewResourceAssembler;

	private TimesheetStatusViewResourceAssembler timesheetStatusViewResourceAssembler;

	public static final String TIMESHEET_TYPE = "Payroll";

	@Inject
	public PayrollTimesheetViewResource(TimesheetViewService timesheetViewService,
			TimesheetViewResourceAssembler timesheetViewResourceAssembler,
			TimesheetStatusViewResourceAssembler timesheetStatusViewResourceAssembler) {
		this.timesheetViewService = timesheetViewService;
		this.timesheetViewResourceAssembler = timesheetViewResourceAssembler;
		this.timesheetStatusViewResourceAssembler = timesheetStatusViewResourceAssembler;
	}

    @ApiOperation(value = "Getting the status count for Payroll", notes = "This will get timesheets status count of all the E/C/R under this approver")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "StatusCountDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/status/payroll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ TimesheetConstants.TIMESHEET_PAYROLL_APPROVER,TimesheetConstants.PROFILE_VIEW})
	public TimesheetStatusCountSummary getStatusCount(@RequestParam String startDate, @RequestParam String endDate,
			@RequestParam String searchParam,@RequestParam String office)
			throws ParseException {
		TimesheetStatusCountSummary result = timesheetViewService.getPayrollStatusCount(TimesheetViewConstants.APPROVER,
				startDate, endDate, TimesheetViewConstants.TYPE_EMPLOYEE, searchParam, TimesheetConstants.TIMESHEET_PAYROLL_TYPE,office);
		return timesheetStatusViewResourceAssembler.toPayrollStatusCountResource(result, TIMESHEET_TYPE, searchParam);
	}
    
    
    //@RequiresAuthority({VIEW_SUBORDINATES_TIMESHEETS})
    @RequestMapping(value = "/payroll",method = RequestMethod.GET)
    @ApiOperation(value = "Getting All payroll Timesheets",
            notes = "This will get all all approved Payroll timesheets of all the employees under this PayrollManager")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "TimesheetDTO"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequiredAuthority({ TimesheetConstants.TIMESHEET_PAYROLL_APPROVER,TimesheetConstants.PROFILE_VIEW})
    public PagedResources<TimesheetDTO> getAllPayrollTimesheets(Pageable pageable,
            PagedResourcesAssembler<TimesheetDTO> pagedAssembler, String startDate, String endDate,
            String searchParam, String office,@RequestParam(required=true) String  status) throws ParseException {
		Page<TimesheetDTO> result = timesheetViewService.getAllTimesheets(pageable, status,
				startDate, endDate, searchParam, TimesheetViewConstants.APPROVER,
				TimesheetViewConstants.PAYROLL_MANAGER, office,TIMESHEET_TYPE);
        return pagedAssembler.toResource(result, timesheetViewResourceAssembler);
    }
    
   
    //@RequiresAuthority({VIEW_SUBORDINATES_TIMESHEETS})
    @RequestMapping(value = "/{id}/activities/payroll", method = RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Getting the activity logs",
            notes = "This will get all the activity logs for particular employee.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List<ActivityLogDTO>"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequiredAuthority({ TimesheetConstants.TIMESHEET_PAYROLL_APPROVER})
    public ResponseEntity<List<ActivityLogDTO>> getActivityLog(@PathVariable UUID id) {
        List<ActivityLogDTO> activityLogDTOs = timesheetViewService.getActivityLog(id);
        return Optional.ofNullable(activityLogDTOs)
                .map(result -> new ResponseEntity<>(activityLogDTOs, HttpStatus.OK))
                .orElseThrow(() -> new ActivityLogNotFoundException());

    }
    
    //@RequiresAuthority({VIEW_SUBORDINATES_TIMESHEETS})
    @RequestMapping(value = "/{id}/comments/payroll", method = RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Getting the timesheet comments",
            notes = "This will get all the timesheet comments for particular employee.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "CommentsDTO"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequiredAuthority({ TimesheetConstants.TIMESHEET_PAYROLL_APPROVER})
    public ResponseEntity<CommentsDTO> getComments(@PathVariable UUID id) throws ParseException {      
        CommentsDTO commentsDTO = timesheetViewService.getComments(id);
        return Optional.ofNullable(commentsDTO)
                .map(result -> new ResponseEntity<>(commentsDTO, HttpStatus.OK))
                .orElseThrow(() -> new CommentsNotFoundException());       
    }
    
    //@RequiresAuthority({VIEW_SUBORDINATES_TIMESHEETS})
    @RequestMapping(value = "/{timesheetId}/payroll", method = RequestMethod.GET)
    @ApiOperation(value = "Getting Particular Timesheets Details",
            notes = "This service will get the timesheet hour entry,units entry,timestamp entry,timer entry and timeoff entry details for particular employee")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "TimesheetResourceDTO"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequiredAuthority({ TimesheetConstants.TIMESHEET_PAYROLL_APPROVER})
    public TimesheetResourceDTO getPayrollTimesheetDetails(@PathVariable UUID timesheetId) throws ParseException {
        TimesheetDTO result =
                timesheetViewService.getTimesheetDetails(timesheetId, true);
        return timesheetViewResourceAssembler.toTimesheetDetailsResource(result);        
    }
   
    // @RequiresAuthority({EDIT_MY_TIMESHEET})
    @RequestMapping(value = "/{timesheetId}/files/payroll", method = RequestMethod.POST)
    @ApiOperation(value = "upload Timesheet's attachments",
            notes = "This will upload payroll timesheet's attachments of a particular Employee.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok")})
    @RequiredAuthority({ TimesheetConstants.TIMESHEET_PAYROLL_APPROVER})
    public @ResponseBody Status uploadMultipleTimesheetFiles(
            @RequestPart("files") MultipartFile[] files,
            @PathVariable("timesheetId") UUID timesheetId)
            throws Exception {
        timesheetViewService.uploadMultipleTimesheetFiles(files, timesheetId);
        return new Status("OK");
    }
    
 // @RequiresAuthority({EDIT_MY_TIMESHEET})
    @RequestMapping(value = "/{timesheetId}/fileDetails/payroll", method = RequestMethod.GET)
    @ApiOperation(value = "get Timesheet's attachment details",
            notes = "This will get timesheet's attachments of a particular Employee.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok")})
    @RequiredAuthority({ TimesheetConstants.TIMESHEET_PAYROLL_APPROVER})
    public List<TimesheetAttachmentsDTO> getTimesheetFileDetails(
    		
            @PathVariable("timesheetId") String timesheetId) throws FileUploadException {
    	UUID timesheetUUID = UUID.fromString(timesheetId);
        return timesheetViewService.getTimesheetFileDetails(timesheetUUID);
    }

    // @RequiresAuthority({EDIT_MY_TIMESHEET})
    @RequestMapping(value = "/file/{timesheetAttachmentId}/payroll", method = RequestMethod.GET)
    @ApiOperation(value = "view Timesheet's attachment",
            notes = "This will show the selected timesheet's attachments of a particular Employee timesheet.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok")})
    @RequiredAuthority({ TimesheetConstants.TIMESHEET_PAYROLL_APPROVER})
    public TimesheetAttachmentsDTO getTimesheetFile(
            @PathVariable("timesheetAttachmentId") String timesheetAttachmentId)
            throws FileUploadException, IOException {
        return timesheetViewService.getTimesheetFile(timesheetAttachmentId);
    }
    
    // @RequiresAuthority({EDIT_MY_TIMESHEET})
    @RequestMapping(value = "/file/{timesheetAttachmentId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "delete Timesheet's attachments",
            notes = "This will delete timesheet's attachment of a particular Employee timesheet.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ok")})
    @RequiredAuthority({ TimesheetConstants.TIMESHEET_PAYROLL_APPROVER})
    public @ResponseBody Status deleteTimesheetFile(
            @PathVariable("timesheetAttachmentId") String timesheetAttachmentId)
            throws Exception {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new Status(timesheetViewService.deleteTimesheetFile(timesheetAttachmentId));
    }
    
    @ApiOperation(value = "Getting the Office locations list",
            notes = "This REST service will get all office locations list for a timesheet")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequestMapping(value = "/locations/payroll", method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)
    @RequiredAuthority({ TimesheetConstants.TIMESHEET_PAYROLL_APPROVER})
    public ResponseEntity<List<OfficeLocationDTO>> getAllOfficeLocations(){
    	String actorType = TimesheetViewConstants.APPROVER;
        List<OfficeLocationDTO> result =
        		timesheetViewService.getOfficeLocations(actorType);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }  
    
}
