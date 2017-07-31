package com.tm.timesheet.timeoff.web.rest;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewModule;
import com.monitorjbl.json.Match;
import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.commonapi.web.rest.util.HeaderUtil;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.service.dto.EngagementDTO;
import com.tm.timesheet.timeoff.exception.TimeoffException;
import com.tm.timesheet.timeoff.resource.assemeblers.PtoAvailableAssembler;
import com.tm.timesheet.timeoff.resource.assemeblers.TimeoffAssembler;
import com.tm.timesheet.timeoff.service.TimeoffService;
import com.tm.timesheet.timeoff.service.dto.PtoAvailableDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffRequestDetailDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "timeoff", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class TimeoffResource {

	private TimeoffService timeoffService;

	private TimeoffAssembler timeoffAssembler;

	private PtoAvailableAssembler ptoAvailableAssembler;

	private static final String STATUS_REQUIRED = "Status is Required";
	private static final String MY_TIMEOFF = "my-timeoff";
	private static final String MY_TEAM_TIMEOFF = "my-team-timeoff";
	private ObjectMapper mapper = new ObjectMapper().registerModule(new JsonViewModule());

	@Inject
	public TimeoffResource(TimeoffService timeoffService, TimeoffAssembler timeoffAssembler,
			PtoAvailableAssembler ptoAvailableAssembler) {
		this.timeoffService = timeoffService;
		this.timeoffAssembler = timeoffAssembler;
		this.ptoAvailableAssembler = ptoAvailableAssembler;
	}

	@ApiOperation(value = "Getting the timeoff for logged in user", notes = "This REST service will get the timeoff requests of the logged in user for the given status")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimeoffDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/my-timeoff", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER })
	public PagedResources<TimeoffDTO> getMyTimeoffList(Pageable pageable,
			PagedResourcesAssembler<TimeoffDTO> pagedAssembler, String startDate, String endDate, String status,String searchParam) {
		validateMyTimeoffQueryParams(status);
		Page<TimeoffDTO> result = timeoffService.getMyTimeoffList(pageable, startDate, endDate, status,
				timeoffService.getLoggedInUser().getEmployeeId(),searchParam);
		return pagedAssembler.toResource(configurationGroupProjection(pageable, "", result), timeoffAssembler);
	}

	private void validateMyTimeoffQueryParams(String status) {
		if (Objects.isNull(status)) {
			throw new TimeoffException(STATUS_REQUIRED);
		}
	}

	private Page<TimeoffDTO> configurationGroupProjection(Pageable pageable, String fields, Page<TimeoffDTO> result) {
		if (StringUtils.isNotBlank(fields)) {
			try {
				String json = mapper.writeValueAsString(JsonView.with(result.getContent()).onClass(TimeoffDTO.class,
						Match.match().exclude("*").include(fields.split(","))));
				TimeoffDTO[] sortings = mapper.readValue(json,
						TypeFactory.defaultInstance().constructArrayType(TimeoffDTO.class));
				new PageImpl<>(Arrays.asList(sortings), pageable, result.getTotalElements());
			} catch (IOException e) {
				throw new TimeoffException("", e);
			}
		}
		return result;
	}

	@ApiOperation(value = "Getting the timeoff count for all status", notes = "This REST service will get the timeoff count of the logged in user for the given status")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimeoffStatus"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/my-timeoff/status", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER })
	public ResponseEntity<TimeoffStatus> getMyTimeoffStatusCount(String startDate, String endDate,String searchParam) {
		TimeoffStatus timeoffStatus = timeoffService.getMyTimeoffStatusCount(timeoffService.getLoggedInUser().getEmployeeId(), startDate, endDate, searchParam);
		return new ResponseEntity<>(timeoffStatus, HttpStatus.OK);
	}

	@RequestMapping(value = "/my-timeoff", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Creating new timeoff request", notes = "This REST service will create the timeoff of the logged in user")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 422, message = "Unprocessable Entity") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER })
	public ResponseEntity<TimeoffDTO> createTimeoff(@Valid @RequestBody TimeoffDTO timeoffDTO) throws ParseException {
		TimeoffDTO timeoffDTOResult = timeoffService.createTimeoff(timeoffDTO, timeoffService.getLoggedInUser());
		return new ResponseEntity<>(timeoffDTOResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/my-timeoff/timeoffdays", method = RequestMethod.GET)
	@ApiOperation(value = "List timeoff days", notes = "This REST service will get timeoff days with holidays,weekoff,applied leaves")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "GETTING"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 422, message = "Unprocessable Entity") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER })
	public ResponseEntity<List<TimeoffRequestDetailDTO>> listTimeOffDays(String startDate, String endDate,String engagementId) {
		List<TimeoffRequestDetailDTO> timeoffRequestDetailDTO = timeoffService.getTimeoffDates(timeoffService.getLoggedInUser(),
				startDate, endDate,engagementId);
		return new ResponseEntity<>(timeoffRequestDetailDTO, HttpStatus.OK);
	}

	/*@ApiOperation(value = "Getting the My PTO Timeoff Details", notes = "This REST service will get the My PTO Timeoff Details of the logged in user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "PtoAvaliableDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/my-timeoff/balance", method = RequestMethod.GET)
	
	public ResponseEntity<PtoAvailableDTO> getMyPtoTimeoffDetails() {
		PtoAvailableDTO timeoffDTO = timeoffService.getMyPtoTimeoffDetails(timeoffService.getLoggedInUser());
		return new ResponseEntity<>(timeoffDTO, HttpStatus.OK);
	}*/
	
	@ApiOperation(value = "Getting the My PTO Timeoff Details", notes = "This REST service will get the My PTO Timeoff Details of the logged in user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "PtoAvaliableDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/my-timeoff/balance", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER })
	public ResponseEntity<PtoAvailableDTO> getMyPtoTimeoffDetails(String engagementId) {
		PtoAvailableDTO timeoffDTO = timeoffService.getMyPtoTimeoffDetails(timeoffService.getLoggedInUser(),engagementId);
		return new ResponseEntity<>(timeoffDTO, HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Getting the My Team PTO Timeoff Details", notes = "This REST service will get the My Team PTO Timeoff Details of the logged in user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "PtoAvaliableDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/team-timeoff/ptobalance", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER, TimesheetConstants.TIMESHEET_APPROVER, TimesheetConstants.ACCOUNT_MANAGER, })
	public ResponseEntity<PtoAvailableDTO> getMyTeamPtoTimeoffDetails(String employeeId,String timeoffId,String engagementId) {
		PtoAvailableDTO timeoffDTO = timeoffService.getMyTeamPtoTimeoffDetails(employeeId,timeoffId,engagementId);
		return new ResponseEntity<>(timeoffDTO, HttpStatus.OK);
	}

	/*@ApiOperation(value = "Getting the Holiday", notes = "This REST service will get the Holidays for date range param")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimeoffDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/my-timeoff/holidays", method = RequestMethod.GET)
	
	public ResponseEntity<Map<String,Map<String,List<TimeoffDTO>>>> getMyTimeoffHolidays(String startDate, String endDate,String engagementId) {
		List<TimeoffDTO> detailDTOs = timeoffService.getMyTimeoffHolidays(startDate, endDate,
				timeoffService.getLoggedInUser().getProvinceId(), timeoffService.getLoggedInUser().getEmployeeId(),
				timeoffService.getLoggedInUser().getJoiningDate(),timeoffService.getLoggedInUser().getEmployeeType(),engagementId);
		
		Map<String,List<TimeoffDTO>> detailDTOMap = new HashMap<>();
		 
		Map<String,Map<String,List<TimeoffDTO>>> detailDTOEmbeddedMap = new HashMap<>();
		
		detailDTOMap.put("timeoffDTOList", detailDTOs);
		
		detailDTOEmbeddedMap.put("_embedded", detailDTOMap);
		return new ResponseEntity<>(detailDTOEmbeddedMap, HttpStatus.OK);
	}*/
	
	
	@ApiOperation(value = "Getting the Holiday", notes = "This REST service will get the Holidays for date range param")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimeoffDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/my-timeoff/holidays", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER,TimesheetConstants.PROFILE_VIEW})
	public Resources<TimeoffDTO> getMyTimeoffHolidays(String startDate, String endDate,String engagementId) {
		List<TimeoffDTO> detailDTOs = timeoffService.getMyTimeoffHolidays(startDate, endDate,
				timeoffService.getLoggedInUser().getProvinceId(), timeoffService.getLoggedInUser().getEmployeeId(),
				timeoffService.getLoggedInUser().getJoiningDate(),timeoffService.getLoggedInUser().getEmployeeType(),engagementId);
		return timeoffAssembler.toHolidayResource(startDate, endDate,engagementId, detailDTOs);
	}
	
	@ApiOperation(value = "Getting the team member timeoff for particular manager", notes = "This REST service will get the team timeoff for particular manager")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimeoffDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/team-timeoff", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_APPROVER, TimesheetConstants.TIMESHEET_APPROVER, TimesheetConstants.ACCOUNT_MANAGER, TimesheetConstants.PROFILE_VIEW })
	public PagedResources<TimeoffDTO> getMyTeamTimeoff(Pageable pageable,
			PagedResourcesAssembler<TimeoffDTO> pagedAssembler, String startDate, String endDate, String status,String searchParam) {

		Page<TimeoffDTO> result = timeoffService.getMyTeamTimeoff(pageable, startDate, endDate, status,
				timeoffService.getLoggedInUser().getEmployeeId(),searchParam);

		return pagedAssembler.toResource(configurationGroupProjection(pageable, "", result), timeoffAssembler);
	}

	@ApiOperation(value = "Getting the myteam timeoff count for all status", notes = "This REST service will get the my timeoff count for all status")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimeoffStatus"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/team-timeoff/status", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER, TimesheetConstants.TIMESHEET_APPROVER, TimesheetConstants.ACCOUNT_MANAGER, })
	public ResponseEntity<TimeoffStatus> getMyTeamTimeoffStatusCount(String startDate, String endDate,String searchParam) {

		TimeoffStatus timeoffStatus = timeoffService.getMyTeamTimeoffStatusCount(timeoffService.getLoggedInUser().getEmployeeId(), startDate, endDate,searchParam);

		return new ResponseEntity<>(timeoffStatus, HttpStatus.OK);
	}

	@ApiOperation(value = "Getting the Team avaliable PTO", notes = "This REST service will get the PTO Avaliable for particular Manager Team Member")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "PtoAvaliableDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/team-timeoff/balance", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER, TimesheetConstants.TIMESHEET_APPROVER, TimesheetConstants.ACCOUNT_MANAGER, })
	public PagedResources<PtoAvailableDTO> getMyTeamTimeoffAvaliableList(Pageable pageable,
			PagedResourcesAssembler<PtoAvailableDTO> ptoPagedAssembler,String searchParam) {

		Page<PtoAvailableDTO> ptoAvaliableDTO = timeoffService.getMyTeamTimeoffAvaliableList(timeoffService.getLoggedInUser().getEmployeeId(),
				pageable,searchParam);

		return ptoPagedAssembler.toResource(ptoAvaliableProjection(pageable, "", ptoAvaliableDTO),
				ptoAvailableAssembler);
	}

	private Page<PtoAvailableDTO> ptoAvaliableProjection(Pageable pageable, String fields,
			Page<PtoAvailableDTO> result) {
		if (StringUtils.isNotBlank(fields)) {
			try {
				String json = mapper.writeValueAsString(JsonView.with(result.getContent())
						.onClass(PtoAvailableDTO.class, Match.match().exclude("*").include(fields.split(","))));
				PtoAvailableDTO[] sortings = mapper.readValue(json,
						TypeFactory.defaultInstance().constructArrayType(PtoAvailableDTO.class));
				new PageImpl<>(Arrays.asList(sortings), pageable, result.getTotalElements());
			} catch (IOException e) {
				throw new TimeoffException("", e);
			}
		}
		return result;
	}

	@RequestMapping(value = "/my-timeoff/{timeoffid}", method = RequestMethod.GET)
	@ApiOperation(value = "Get timeoff details", notes = "This REST service will get timeoff detials with holidays,weekoff,applied leaves")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "GETTING"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 422, message = "Unprocessable Entity") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER })
	public ResponseEntity<TimeoffDTO> getTimeoffDetails(@PathVariable("timeoffid") String timeoffId) {
		TimeoffDTO timeoffDTO = timeoffAssembler.toResource(timeoffService.getMyTimeoff(timeoffId, MY_TIMEOFF));
		return new ResponseEntity<>(timeoffDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/team-timeoff/{timeoffid}", method = RequestMethod.GET)
	@ApiOperation(value = "Get timeoff details", notes = "This REST service will get timeoff detials with holidays,weekoff,applied leaves")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "GETTING"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 422, message = "Unprocessable Entity") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_APPROVER, TimesheetConstants.TIMESHEET_APPROVER, TimesheetConstants.ACCOUNT_MANAGER,TimesheetConstants.PROFILE_VIEW })
	public ResponseEntity<TimeoffDTO> getTeamTimeoffDetails(@PathVariable("timeoffid") String timeoffId) {
		TimeoffDTO timeoffDTO = timeoffAssembler.toResource(timeoffService.getMyTimeoff(timeoffId, MY_TEAM_TIMEOFF));
		return new ResponseEntity<>(timeoffDTO, HttpStatus.OK);
	}

	@ApiOperation(value = "Deleting the particular time off", notes = "This REST service will delete the particular time off.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/my-timeoff/{timeoffid}", method = RequestMethod.DELETE, produces = { HAL_JSON_VALUE,
			APPLICATION_JSON_VALUE })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER })
	public ResponseEntity<Resource<Object>> deleteTimeoff(@PathVariable("timeoffid") String timeoffId) {
		timeoffService.deleteMyTimeoff(timeoffId);
		Resource<Object> resource = new Resource<>(TimeoffResource.class);
		resource.add(
				linkTo(methodOn(TimeoffResource.class).getMyTimeoffList(null, null, null, null, "all",null)).withRel("All")
		);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("timeoff", timeoffId)).body(resource);
	}
	
	
	@RequestMapping(value = "/team-timeoff", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update timeoff status", notes = "This REST service will update the timeoff status of the selected user")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request")
			})
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER, TimesheetConstants.TIMESHEET_APPROVER, TimesheetConstants.ACCOUNT_MANAGER,})
	public ResponseEntity<TimeoffDTO> updateTimeoffStatus(@RequestBody List<TimeoffDTO> timeoffDTO) {
		TimeoffDTO timeoffDTOObj = timeoffService.updateTimeoffStatus(timeoffDTO, timeoffService.getLoggedInUser());
		return new ResponseEntity<>(timeoffDTOObj, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/timesheet-timeoff", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Creating new timeoff request", notes = "This REST service will create the timeoff of the logged in user")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 422, message = "Unprocessable Entity") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER,TimesheetConstants.TIMESHEET_APPROVER, TimesheetConstants.ACCOUNT_MANAGER, })
	public ResponseEntity<String> createTimesheetTimeoff(@Valid @RequestBody List<TimeoffDTO> timeoffDTO) {
		timeoffService.createTimesheetTimeoff(timeoffDTO, timeoffService.getLoggedInUser());
		return new ResponseEntity<>(StringUtils.EMPTY, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/timesheet-teamtimeoff", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update timeoff status", notes = "This REST service will update the timeoff status of the selected user")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request")
			})
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER, TimesheetConstants.TIMESHEET_APPROVER, TimesheetConstants.ACCOUNT_MANAGER,})
	public ResponseEntity<TimeoffDTO> updateTimesheetTimeoffStatus(@RequestBody List<TimeoffDTO> timeoffDTO) {
		TimeoffDTO timeoffDTOObj = timeoffService.updateTimesheetTimeoffStatus(timeoffDTO, timeoffService.getLoggedInUser());
		return new ResponseEntity<>(timeoffDTOObj, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Getting the Holiday", notes = "This REST service will get the Holidays for date range param")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimeoffDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/team-timeoff/holidays", method = RequestMethod.GET)
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER, TimesheetConstants.TIMESHEET_APPROVER, TimesheetConstants.ACCOUNT_MANAGER,TimesheetConstants.TIMESHEET_PAYROLL_APPROVER})
	public Resources<TimeoffDTO> getMyTeamTimeoffHolidays(String startDate, String endDate,String engagementId,String employeeId) {
		List<TimeoffDTO> detailDTOs = timeoffService.getMyTeamTimeoffHolidays(startDate, endDate ,engagementId,employeeId);
		return timeoffAssembler.toHolidayResource(startDate, endDate,null, detailDTOs);
	}
	
	@RequestMapping(value = "/getengagements", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "getengagements", notes = "This REST service will getengagements")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request")
			})
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER })
	public ResponseEntity<List<EngagementDTO>> getEngagements() {
		List<EngagementDTO> engagementDTO = timeoffService.getEngagements(timeoffService.getLoggedInUser());
		return new ResponseEntity<>(engagementDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/ptoAvailable", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "PTO Available creation", notes = "This REST service will update the timeoff status of the selected user")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER,
			TimesheetConstants.FINANCE_MANAGER, TimesheetConstants.FINANCE_REPRESENTATIVE,
			TimesheetConstants.PROFILE_VIEW, TimesheetConstants.SUPER_ADMIN })
	public void createPtoAvailable(@RequestBody PtoAvailableDTO ptoAvailableDTO) {
		timeoffService.createPTOAvailable(ptoAvailableDTO);
	}
	
	@RequestMapping(value = "/ptoAvailable/PTOAccural", method = RequestMethod.GET)
	@ApiOperation(value = "Get timeoff details", notes = "This REST service will get timeoff detials with holidays,weekoff,applied leaves")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "GETTING"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 422, message = "Unprocessable Entity") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER,TimesheetConstants.PROFILE_VIEW,TimesheetConstants.FINANCE_MANAGER,TimesheetConstants.FINANCE_REPRESENTATIVE })
	public ResponseEntity<PtoAvailableDTO> getPTOAccural(String startDate,Long employeeId) {
		PtoAvailableDTO ptoAvailableDTO  = timeoffService.getPTOAccural(startDate, employeeId);
		return new ResponseEntity<>(ptoAvailableDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/createMobileTimeoff", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Creating new timeoff request for mobile", notes = "This REST service will create the timeoff of the logged in user")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 422, message = "Unprocessable Entity") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER })
	public ResponseEntity<TimeoffDTO> createMobileTimeoff(@Valid @RequestBody TimeoffDTO timeoffDTO) throws ParseException {
		List<TimeoffDTO> timeoffDTOs=new ArrayList<>();
		timeoffDTOs.add(timeoffDTO);
		TimeoffDTO timeoffDTOResult = timeoffService.createMobileTimeoff(timeoffDTOs, timeoffService.getLoggedInUser());
		return new ResponseEntity<>(timeoffDTOResult, HttpStatus.OK);
	}
	
	/*@RequestMapping(value = "/mobile-timesheettimeoff/{timesheetid}", method = RequestMethod.GET)
	@ApiOperation(value = "Get timeoff details", notes = "This REST service will get ")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "GETTING"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 422, message = "Unprocessable Entity") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER })
	public ResponseEntity<Timesheet> getTimesheetTimeoff(@PathVariable("timesheetid") String timesheetid) {
		Timesheet timesheet = timeoffService.getTimesheetTimeoff(timesheetid);
		return new ResponseEntity<>(timesheet, HttpStatus.OK);
	}*/
	
	
	/*@RequestMapping(value = "/mobile-timesheettimeoffview", method = RequestMethod.POST)
	@ApiOperation(value = "Get timeoff details", notes = "This REST service will get ")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "GETTING"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 422, message = "Unprocessable Entity") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER })
	public ResponseEntity<TimeoffDTO> getTimesheetTimeoffView(@Valid @RequestBody TimeoffDTO timeoffDTO) {
		TimeoffDTO timeoffDTOs = timeoffService.getTimesheetTimeoffView(timeoffDTO,timeoffService.getLoggedInUser());
		return new ResponseEntity<>(timeoffDTOs, HttpStatus.OK);
	}*/
	
	@RequestMapping(value = "/mobile-timesheettimeoffview", method = RequestMethod.GET)
	@ApiOperation(value = "Get timeoff details", notes = "This REST service will get ")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "GETTING"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 422, message = "Unprocessable Entity") })
	@RequiredAuthority({ TimesheetConstants.TIMEOFF_SUBMITTER, TimesheetConstants.TIMEOFF_APPROVER })
	public ResponseEntity<TimeoffDTO> getTimesheetTimeoffView(String startDate, String ptoType,String engagementId) {
		TimeoffDTO timeoffDTOs = timeoffService.getTimesheetTimeoffView(startDate, ptoType,engagementId,timeoffService.getLoggedInUser());
		return new ResponseEntity<>(timeoffDTOs, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/team-timeoff/count/{status}", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.ALL })
	public ResponseEntity<Long> getCountByUserIdAndStatus(@PathVariable("status") String status) throws ParseException {
		return new ResponseEntity<>(timeoffService.getCountByUserIdAndStatus(
				timeoffService.getLoggedInUser().getEmployeeId(), status), HttpStatus.OK);
	}

}