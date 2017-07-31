package com.tm.timesheet.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.commonapi.web.rest.util.HeaderUtil;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.TimesheetTemplate;
import com.tm.timesheet.service.TimesheetService;
import com.tm.timesheet.service.dto.CommonTimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetDTO;
import com.tm.timesheet.service.dto.UploadFilesDetailsDTO;

@RestController
@RequestMapping(value = "/my_timesheets_proxy")
@Api(value = "my_timesheets_proxy", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class SubmitterTimesheetProxyResource {

	private TimesheetService timesheetService;

	public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";
	public static final String TIMESHEET_TYPE = "Proxy";
	private static final String FILE_NAME = "fileName";
	public static final String ATTACHMENT = "attachment";
	public static final String CACHE_CONTROL = "must-revalidate, post-check=0, pre-check=0";
	public static final String APPLICATION_EXCEL = "application/vnd.ms-excel";

	@Inject
	public SubmitterTimesheetProxyResource(TimesheetService timesheetService) {
		this.timesheetService = timesheetService;
	}

	@ApiOperation(value = "Update My-Timesheet", notes = "This will update timesheet details by submitter")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Upeate My-Timesheet"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequestMapping(value = "/{timesheetId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ TimesheetConstants.ACCOUNT_MANAGER})
	public CommonTimesheetDTO updateTimesheet(@PathVariable String timesheetId,
			@RequestBody CommonTimesheetDTO commonTimesheetDTO) throws ParseException {
		commonTimesheetDTO.setTimesheetType(TIMESHEET_TYPE);
		return timesheetService.updateTimesheet(commonTimesheetDTO);
	}

	@ApiOperation(value = "Submit My-Timesheet", notes = "This will submit timesheet by submitter")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Submit My-Timesheet"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequestMapping(value = "/{timesheetId}/submitter", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ TimesheetConstants.ACCOUNT_MANAGER })
	public ResponseEntity<TimesheetDTO> submitTimesheet(@PathVariable String timesheetId,
			@RequestBody CommonTimesheetDTO commonTimesheetDTO) throws ParseException {
		commonTimesheetDTO.setTimesheetType(TIMESHEET_TYPE);
		timesheetService.submitTimesheet(commonTimesheetDTO);
		TimesheetDTO timesheetDTO = new TimesheetDTO();
		timesheetDTO.setStatus(TimesheetConstants.OK);
		timesheetDTO.setPaidStatus(null);
		return new ResponseEntity<>(timesheetDTO, HttpStatus.OK);
	}

	@ApiOperation(value = "Re-Open My-Timesheet", notes = "This will update particular timesheet status based on current one.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Update My-Timesheet"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequestMapping(value = "/{timesheetId}/reopen", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ TimesheetConstants.ACCOUNT_MANAGER ,TimesheetConstants.ACCOUNT_MANAGER})
	public ResponseEntity<String> reopenTimesheet(@PathVariable String timesheetId) throws ParseException {
		timesheetService.reopenTimesheet(timesheetId);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("timesheetId", timesheetId)).body("OK");
	}

	@ApiOperation(value = "Submit All Proxy-Timesheet", notes = "This will submit one or more proxy timesheet by submitter")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Submit All proxy-Timesheet"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequestMapping(value = "/submitter", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ TimesheetConstants.ACCOUNT_MANAGER })
	public ResponseEntity<TimesheetDTO> bulkSubmitTimesheet(@RequestBody CommonTimesheetDTO commonTimesheetDTO)
			throws ParseException {
		timesheetService.bulkSubmitTimesheet(commonTimesheetDTO);
		TimesheetDTO timesheetDTO = new TimesheetDTO();
		timesheetDTO.setStatus(TimesheetConstants.OK);
		timesheetDTO.setPaidStatus(null);
		return new ResponseEntity<>(timesheetDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	@RequiredAuthority({ TimesheetConstants.ACCOUNT_MANAGER })
	public ResponseEntity<Map<String, Object>> fileUpload(@RequestParam("file") MultipartFile multipartFile)
			throws IOException {
		return new ResponseEntity<>(timesheetService.readTimesheetExcel(multipartFile.getInputStream(),
				multipartFile.getOriginalFilename()), HttpStatus.OK);
	}

	@RequestMapping(value = "/fileUploadDetails", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ TimesheetConstants.ACCOUNT_MANAGER })
	public ResponseEntity<Page<UploadFilesDetailsDTO>> getAllUploaddetails(Pageable pageable) {
		return new ResponseEntity<>(timesheetService.getAllUploadFilesDetails(pageable), HttpStatus.OK);
	}

	@RequestMapping(value = "/fileUploadLogs", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.ACCOUNT_MANAGER })
	public ResponseEntity<Map<String, Object>> getAllUploadLogs(@RequestParam String fileName) {
		Map<String, Object> map = new HashMap<>();
		map.put("uploadLogs", timesheetService.getAllUploadLogs(fileName));
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

    @RequestMapping(value = "/template/download/{timesheetTemplateId}", method = RequestMethod.GET)
    @RequiredAuthority({AuthoritiesConstants.ALL})
    public ResponseEntity<byte[]> downloadTemplate(@PathVariable("timesheetTemplateId") Long timesheetTemplateId, 
    		HttpServletResponse response) throws IOException {
    	
		TimesheetTemplate timesheetTemplate = timesheetService.getTimesheetTemplate(timesheetTemplateId);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(APPLICATION_EXCEL));
		String fileName = timesheetTemplate.getTimesheetTemplateName() + ".xls";
		headers.set(FILE_NAME, fileName);
		headers.setContentDispositionFormData(ATTACHMENT, fileName);
		headers.setCacheControl(CACHE_CONTROL);

		return new ResponseEntity<>(timesheetTemplate.getTemplate(), headers, HttpStatus.OK);
    }
}
