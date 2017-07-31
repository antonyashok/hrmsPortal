/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.web.rest.TimeruleConfigurationResource.java
 * Author        : Hemanth Kumar
 * Date Created  : Feb 28, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
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
import com.tm.commonapi.web.rest.util.HeaderUtil;
import com.tm.timesheet.configuration.domain.TimeruleConfiguration;
import com.tm.timesheet.configuration.domain.TimeruleConfiguration.ActiveFlag;
import com.tm.timesheet.configuration.exception.TimeRuleConfigurationNotFoundException;
import com.tm.timesheet.configuration.exception.TimeruleConfigurationException;
import com.tm.timesheet.configuration.repository.TimeruleConfigurationRepository;
import com.tm.timesheet.configuration.resource.assemeblers.TimeruleResourceAssembler;
import com.tm.timesheet.configuration.service.resources.TimeruleResource;
import com.tm.timesheet.constants.TimesheetConstants;

@RestController
@Api(value = "timeruleConfigurations", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class TimeruleConfigurationResource {

	private static final String TIMERULE_CONFIG_SETTINGS = "Timerule Configuration Settings";
	private static final String INVALID_DT = "timerule.dt.less.than.ot";
	private static final String EMPTY_DATA = "timerule.data.empty";
	private static final String MAX_HOURS_ACHIEVED = "timerule.ot.max.hours.achieved";
	private static final String TIMERULE_INITIAL_ID_NOT_EXISTS = "timerule.initial.id.cannot.exists";
	private static final String INVALID_SELECTION = "timerule.invalid.selection";
	private static final String TIMERULE_CANNOT_BE_EMPTY = "timerule.type.not.empty";
	private static final String TIMERULE_NAME_ALREADY_EXISTS = "timerule.name.already.exists";
	private static final String TIME_RULE_URI = "/timetrack/timerules/";
	private static final Double MAX_HOURS_DT = Double.valueOf(12);
	private static final Double MAX_HOURS_OT = Double.valueOf(8);
	
	private TimeruleResourceAssembler timeruleResourceAssembler;
	private TimeruleConfigurationRepository timeruleConfigurationRepository;

	@Inject
	protected TimeruleConfigurationResource(TimeruleResourceAssembler timeruleResourceAssembler,
			TimeruleConfigurationRepository timeruleConfigurationRepository) {
		this.timeruleResourceAssembler = timeruleResourceAssembler;
		this.timeruleConfigurationRepository = timeruleConfigurationRepository;
	}

	@RequestMapping(value = "/timerules", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Creating the timerule configuration", notes = "This REST service will create the timerule configuration")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "CREATED"),
			@ApiResponse(code = 400, message = "BAD REQUEST") })
	@RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
	public ResponseEntity<TimeruleConfiguration> createTimerule(
			@Valid @RequestBody TimeruleConfiguration timeruleConfigSetting)
			throws URISyntaxException, TimeruleConfigurationException {
		if (null != timeruleConfigSetting.getTimeRuleId()) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert(TIMERULE_CONFIG_SETTINGS, TIMERULE_INITIAL_ID_NOT_EXISTS))
					.body(null);
		}
		prepareTimeruleData(timeruleConfigSetting);
		validateTimeruleConfiguration(timeruleConfigSetting);
		timeruleConfigurationRepository.save(timeruleConfigSetting);
		return ResponseEntity
				.created(new URI(TIME_RULE_URI + timeruleConfigSetting.getTimeRuleId())).headers(HeaderUtil
						.createEntityCreationAlert("timeRuleSetting", timeruleConfigSetting.getTimeRuleId().toString()))
				.body(timeruleConfigSetting);
	}

	@RequestMapping(value = "/timerules/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Getting the particular Timerule Configuration", notes = "This REST service will get the particular timerule configuration")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimeruleResource"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
	public ResponseEntity<TimeruleResource> getTimeruleConfiguration(@PathVariable UUID id)
			throws TimeruleConfigurationException, TimeRuleConfigurationNotFoundException {
		TimeruleConfiguration timeruleSetting;
		timeruleSetting = timeruleConfigurationRepository.findOne(id);
		if (Objects.isNull(timeruleSetting)) {
			throw new TimeRuleConfigurationNotFoundException(id + " Not Found");
		}
		return Optional.ofNullable(timeruleSetting)
				.map(result -> new ResponseEntity<>(timeruleResourceAssembler.toResources(result), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@RequestMapping(value = "/timerules", method = RequestMethod.GET)
	@ApiOperation(value = "Getting Timerule  Configuration", notes = "This REST service will get All Time Rules")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimeruleResource"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
	public PagedResources<TimeruleResource> getAllTimerules(Pageable pageable,
			PagedResourcesAssembler<TimeruleConfiguration> pagedAssembler) throws TimeruleConfigurationException {
		Page<TimeruleConfiguration> result;
		result = timeruleConfigurationRepository.findAllTimeruleConfigurations(pageable);
		return pagedAssembler.toResource(result, timeruleResourceAssembler);
	}

	@RequestMapping(value = "/timerules/{id}", method = RequestMethod.PUT)
	@ApiOperation(value = "Updating Timerule Configuration", notes = "This REST service will update the Time Rule")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request") })
	@RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
	public TimeruleConfiguration updateTimeruleConfiguration(@PathVariable UUID id,
			@Valid @RequestBody TimeruleConfiguration timeruleConfiguration)
			throws TimeruleConfigurationException, TimeRuleConfigurationNotFoundException {
		validateNullException(timeruleConfiguration);
		UUID timeruleId = timeruleConfiguration.getTimeRuleId();
		TimeruleConfiguration configuration = timeruleConfigurationRepository.findOne(timeruleId);
		if (Objects.isNull(configuration)) {
			throw new TimeRuleConfigurationNotFoundException(timeruleId + "Not found");
		}
		prepareTimeruleUpdateData(timeruleConfiguration);
		validateTimeruleUpdateConfiguration(timeruleConfiguration);
		return timeruleConfigurationRepository.saveAndFlush(timeruleConfiguration);
	}
	
	@RequestMapping(value = "/timerules/list", method = RequestMethod.GET)
	@ApiOperation(value = "Getting Timerule  Configuration", notes = "This REST service will get All Time Rules")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimeruleResource"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequiredAuthority({ TimesheetConstants.SUPER_ADMIN ,AuthoritiesConstants.FINANCE_REPRESENTATIVE})
	public List<TimeruleConfiguration> getAllTimerules() throws TimeruleConfigurationException {
		List<TimeruleConfiguration> result;
		result = timeruleConfigurationRepository.findByActiveIndFlag(ActiveFlag.Y);
		return result;
	}
	
	private void prepareTimeruleData(TimeruleConfiguration timeruleConfiguration) {
		timeruleConfiguration.setTimeRuleName(timeruleConfiguration.getTimeRuleName().trim());
		timeruleConfiguration.setCreatedBy(1L);
		timeruleConfiguration.setLastModifiedBy(1L);
	}

	private void prepareTimeruleUpdateData(TimeruleConfiguration timeruleConfiguration) {
		if (StringUtils.isNotBlank(timeruleConfiguration.getTimeRuleName())) {
			timeruleConfiguration.setTimeRuleName(timeruleConfiguration.getTimeRuleName().trim());
		} else {
			throw new TimeruleConfigurationException(TIMERULE_CANNOT_BE_EMPTY);
		}

		timeruleConfiguration.setLastModifiedBy(1L);
		timeruleConfiguration.setLastModifiedDate(ZonedDateTime.now());
	}

	private void validateTimeruleConfiguration(TimeruleConfiguration timeruleConfiguration) {
		TimeruleConfiguration configuration = timeruleConfigurationRepository
				.findByTimeRuleName(timeruleConfiguration.getTimeRuleName());
		if (!Objects.isNull(configuration)) {
			throw new TimeruleConfigurationException(TIMERULE_NAME_ALREADY_EXISTS);
		}
		validateConfiguration(timeruleConfiguration);
	}

	private void validateTimeruleUpdateConfiguration(TimeruleConfiguration timeruleConfiguration) {
		TimeruleConfiguration configuration = timeruleConfigurationRepository
				.findByTimeRuleName(timeruleConfiguration.getTimeRuleName());
		if (!Objects.isNull(configuration)
				&& (!configuration.getTimeRuleId().equals(timeruleConfiguration.getTimeRuleId()))) {
			throw new TimeruleConfigurationException(TIMERULE_NAME_ALREADY_EXISTS);
		}
		validateConfiguration(timeruleConfiguration);
	}

	private void validateConfiguration(TimeruleConfiguration timeruleConfiguration) {
		if (timeruleConfiguration.getDtForDayHrsExceeds() != null
				&& timeruleConfiguration.getOtForDayHrsExceeds() != null) {
			validateDTHours(timeruleConfiguration);
		}
		validateRate(timeruleConfiguration);
	}

	private void validateDTHours(TimeruleConfiguration timeruleConfiguration) {
		if (timeruleConfiguration.getDtForDayHrsExceeds().equals(MAX_HOURS_OT)
				&& null != timeruleConfiguration.getOtForDayHrsExceeds()) {
			throw new TimeruleConfigurationException(MAX_HOURS_ACHIEVED);
		}

		if (timeruleConfiguration.getOtForDayHrsExceeds().equals(MAX_HOURS_DT)
				&& null != timeruleConfiguration.getDtForDayHrsExceeds()) {
			throw new TimeruleConfigurationException(MAX_HOURS_ACHIEVED);
		}
		if (timeruleConfiguration.getDtForDayHrsExceeds()
				.compareTo(timeruleConfiguration.getOtForDayHrsExceeds()) <= 0) {
			throw new TimeruleConfigurationException(INVALID_DT);
		}
	}

	private void validateRate(TimeruleConfiguration timeruleConfiguration) {
		if ((StringUtils.isNotEmpty(timeruleConfiguration.getRateFor7thDayAllHrs())
				&& StringUtils.isNotEmpty(timeruleConfiguration.getRateFor7thDayFirst8hrs()))
				|| (StringUtils.isNotEmpty(timeruleConfiguration.getRateFor7thDayAllHrs())
						&& StringUtils.isNotEmpty(timeruleConfiguration.getRateFor7thDayAfter8hrs()))) {
			throw new TimeruleConfigurationException(INVALID_SELECTION);
		}
	}

	private void validateNullException(TimeruleConfiguration timeruleConfiguration) {
		if (Objects.isNull(timeruleConfiguration)) {
			throw new TimeruleConfigurationException(EMPTY_DATA);
		}
	}

}
