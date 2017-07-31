package com.tm.timesheet.configuration.web.rest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.RequiredAuthority;
import com.tm.commonapi.web.rest.util.HeaderUtil;
import com.tm.timesheet.configuration.exception.ConfigurationException;
import com.tm.timesheet.configuration.exception.ConfigurationGroupException;
import com.tm.timesheet.configuration.exception.ConfigurationNotFoundException;
import com.tm.timesheet.configuration.exception.HolidayConfigurationException;
import com.tm.timesheet.configuration.resource.assemeblers.ConfigurationGroupResourceAssembler;
import com.tm.timesheet.configuration.resource.assemeblers.ConfigurationGroupViewResourceAssembler;
import com.tm.timesheet.configuration.resource.assemeblers.HolidayConfigurationResourceAssembler;
import com.tm.timesheet.configuration.service.ConfigurationGroupService;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupDTO;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupResourceDTO;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupViewDTO;
import com.tm.timesheet.configuration.service.dto.HolidayConfigurationDTO;
import com.tm.timesheet.configuration.service.dto.NotificationAttributeDTO;
import com.tm.timesheet.configuration.service.dto.OfficeLocationDTO;
import com.tm.timesheet.configuration.service.dto.UserGroupDTO;
import com.tm.timesheet.constants.TimesheetConstants;

/**
 * REST controller for managing ConfigurationGroup.
 */
@RestController
@Api(value = "configurations", produces = APPLICATION_JSON_VALUE ,
        consumes = APPLICATION_JSON_VALUE)
public class ConfigurationGroupResource {

    private static final String FAILURE_ALERT =
            "A new configurationGroup doesn't have an ID";

    private static final String CONFIGURATION_GROUP_SETTINGS = "configurationGroupSetting";
    
    private static final String CONFIGURATION_GROUP = "configurationGroup";

    private ConfigurationGroupService configurationGroupService;

    private ConfigurationGroupResourceAssembler resourceAssembler;
    
    private ConfigurationGroupViewResourceAssembler resourceViewAssembler;

    private HolidayConfigurationResourceAssembler holidayResourceAssembler;

    @Inject
    public ConfigurationGroupResource(ConfigurationGroupService configurationGroupService,
            ConfigurationGroupResourceAssembler resourceAssembler,
            HolidayConfigurationResourceAssembler holidayResourceAssembler,ConfigurationGroupViewResourceAssembler resourceViewAssembler) {
        this.configurationGroupService = configurationGroupService;
        this.resourceAssembler = resourceAssembler;
        this.holidayResourceAssembler = holidayResourceAssembler;
        this.resourceViewAssembler = resourceViewAssembler;
    }

    @RequestMapping(value = "/configurations", method = RequestMethod.POST,
            produces = {APPLICATION_JSON_VALUE}, consumes = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Creating the configuration group",
            notes = "This REST service will create the configuration group")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<ConfigurationGroupResourceDTO> createConfigurationGroup(
            @Valid @RequestBody ConfigurationGroupDTO configurationGroupDTO)
            throws URISyntaxException, ConfigurationException {
        if (Objects.nonNull(configurationGroupDTO.getConfigurationGroupId())) {
            return ResponseEntity
                    .badRequest().headers(HeaderUtil
                            .createFailureAlert(CONFIGURATION_GROUP_SETTINGS, FAILURE_ALERT))
                    .body(null);
        }
        ConfigurationGroupDTO result =
                configurationGroupService.createConfigurationGroup(configurationGroupDTO);
        return ResponseEntity
                .created(new URI("/timetrack/configurations/" + result.getConfigurationGroupId()))
                .headers(HeaderUtil.createEntityCreationAlert(CONFIGURATION_GROUP,
                        result.getConfigurationGroupId().toString()))
                .body(resourceAssembler.toConfigResource(result));
    }

    @RequestMapping(value = "/configurations/{id}", method = RequestMethod.PUT,
            consumes = APPLICATION_JSON_VALUE, produces = {APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Updating Configuration Group",
            notes = "This REST service will update the configuration group")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<ConfigurationGroupResourceDTO> updateConfigurationGroup(
            @Valid @RequestBody ConfigurationGroupDTO configurationGroupDTO,
            @PathVariable UUID id)
            throws URISyntaxException, ConfigurationException, ConfigurationNotFoundException {
        if (null == configurationGroupDTO.getConfigurationGroupId()) {
            configurationGroupDTO.setConfigurationGroupId(id);
        }
        ConfigurationGroupDTO result =
                configurationGroupService.updateConfigurationGroup(configurationGroupDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(CONFIGURATION_GROUP,
                        result.getConfigurationGroupId().toString()))
                .body(resourceAssembler.toConfigResource(result));
    }

    @RequestMapping(value = "/configurations", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Getting Configuration Group",
            notes = "This REST service will get the configuration group")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ConfigurationGroupDTO")})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public PagedResources<ConfigurationGroupViewDTO> getAllConfigurationGroup(Pageable pageable,
            PagedResourcesAssembler<ConfigurationGroupViewDTO> pagedAssembler, String fields)
            throws ConfigurationException, IOException {
        Page<ConfigurationGroupViewDTO> result =
                configurationGroupService.getAllConfigurationGroupsView(pageable);
        return pagedAssembler.toResource(result, resourceViewAssembler);
    }

    @RequestMapping(value = "/configurations/{id}", method = RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Getting the particular Configuration Group",
            notes = "This REST service will get the particular configuration group")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "ConfigurationGroupDTO"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<ConfigurationGroupResourceDTO> getConfigurationGroup(
            @PathVariable UUID id) throws ConfigurationException, ConfigurationNotFoundException {
        ConfigurationGroupDTO configurationGroupDTO =
                configurationGroupService.getConfigurationGroup(id);
        return Optional.ofNullable(configurationGroupDTO)
                .map(result -> new ResponseEntity<>(resourceAssembler.toConfigResource(result),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @SuppressWarnings("rawtypes")
    @ApiOperation(value = "Deleting the particular Configuration Group",
            notes = "This REST service will delete the particular configuration group")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequestMapping(value = "/configurations/{id}", method = RequestMethod.DELETE,
            produces = {APPLICATION_JSON_VALUE})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<Resource> deleteConfigurationGroup(@PathVariable UUID id)
            throws ConfigurationException {
        configurationGroupService.deleteConfigurationGroup(id);
        Link getAllConfigLink = linkTo(ConfigurationGroupResource.class).slash("configurations")
                .withRel("getAllConfigurationGroup");
        Link createConfigLink = linkTo(ConfigurationGroupResource.class).slash("configurations")
                .withRel("createConfigurationGroup");

        Resource<Class<ConfigurationGroupResource>> resource =
                new Resource<>(ConfigurationGroupResource.class);
        resource.add(getAllConfigLink);
        resource.add(createConfigLink);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert(CONFIGURATION_GROUP, id.toString()))
                .body(resource);
    }

    @RequestMapping(value = "/configurations/{configGroupId}/holidays", method = RequestMethod.POST,
            consumes = APPLICATION_JSON_VALUE, produces = {APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Creating the Holiday configuration group",
            notes = "This REST service will create Holiday for a configuration group")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<List<HolidayConfigurationDTO>> createHolidayConfigurations(
            @Valid @RequestBody List<HolidayConfigurationDTO> holidayConfigurationDTOs,
            @PathVariable UUID configGroupId) throws URISyntaxException, ConfigurationException {
        List<HolidayConfigurationDTO> result = configurationGroupService
                .createHolidayConfiguration(configGroupId, holidayConfigurationDTOs);
        return ResponseEntity
                .created(new URI("/configurations/settings/" + configGroupId + "/holiday/"))
                .headers(HeaderUtil.createEntityCreationAlert(CONFIGURATION_GROUP, configGroupId.toString()))
                .body(holidayResourceAssembler.toResources(result));
    }

    @RequestMapping(value = "/configurations/{configGroupId}/holiday/{holidayId}",
            method = RequestMethod.GET, produces = {APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Get the Holiday configuration group",
            notes = "This REST service get create Holiday for a configuration group")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<HolidayConfigurationDTO> getHolidayConfiguration(
            @PathVariable UUID configGroupId, @PathVariable UUID holidayId)
            throws ConfigurationException {
        HolidayConfigurationDTO result =
                configurationGroupService.getHolidayConfiguration(holidayId);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityCreationAlert(CONFIGURATION_GROUP, configGroupId.toString()))
                .body(holidayResourceAssembler.toResource(result));
    }

    @RequestMapping(value = "/configurations/{configGroupId}/holidays", method = RequestMethod.PUT,
            consumes = APPLICATION_JSON_VALUE, produces = {APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Updating Holiday for Configuration Group",
            notes = "This REST service will update the holiday for a configuration group")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<List<HolidayConfigurationDTO>> updateHolidayConfigurations(
            @Valid @RequestBody List<HolidayConfigurationDTO> holidayConfigurationDTOs,
            @PathVariable UUID configGroupId)
            throws URISyntaxException, ConfigurationException, ConfigurationNotFoundException {
        List<HolidayConfigurationDTO> result = configurationGroupService
                .updateHolidayConfiguration(configGroupId, holidayConfigurationDTOs);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityCreationAlert(CONFIGURATION_GROUP, configGroupId.toString()))
                .body(holidayResourceAssembler.toResources(result));
    }

    @ApiOperation(value = "Deleting particular holiday",
            notes = "This REST service will delete the particular holiday")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequestMapping(value = "/configurations/{configGroupId}/holidays/{id}",
            method = RequestMethod.DELETE)
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<String> deleteHolidayConfiguration(@PathVariable(
            name = "configGroupId") UUID configGroupId, @PathVariable(name = "id") UUID id)
            throws ConfigurationException {
        configurationGroupService.deleteHolidayConfiguration(id);
      
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert(CONFIGURATION_GROUP, id.toString()))
                .body("OK");
    }

    @ApiOperation(value = "Deleting the particular Notification Configuration",
            notes = "This REST service will delete the particular notification configuration")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequestMapping(value = "/configurations/notification/{id}", method = RequestMethod.DELETE)
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<String> deleteNotificationConfiguration(@PathVariable UUID id)
            throws ConfigurationException {
        configurationGroupService.deleteNotificationConfiguration(id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert(CONFIGURATION_GROUP, id.toString())).body(null);
    }

    @RequestMapping(value = "/configurations/{id}/holiday", method = RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Getting the particular ConfigurationGorup holidays",
            notes = "This REST service will get the particular holiday list for a configuration group")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<List<HolidayConfigurationDTO>> getHolidayConfigurations(
            @PathVariable UUID id) throws ConfigurationException, ConfigurationNotFoundException {
        List<HolidayConfigurationDTO> holidayConfigurationDTOs =
                configurationGroupService.getConfiguredHolidayConfigurations(id);
        return new ResponseEntity<>(
                holidayResourceAssembler.toResources(holidayConfigurationDTOs), HttpStatus.OK);
    }

    @RequestMapping(value = "/configurations/{id}/notification", method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Getting the particular ConfigurationGroup NotificationConfigurations",
            notes = "This REST service will get the particular notification config group list for a configuration group")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<List<NotificationAttributeDTO>> getNotificationConfiguration(
            @PathVariable UUID id) throws ConfigurationException, ConfigurationNotFoundException {
        List<NotificationAttributeDTO> notificationAttributeDTOs =
                configurationGroupService.getConfiguredNotificationConfigurations(id);
        return new ResponseEntity<>(notificationAttributeDTOs,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/configurations/{id}/notifications", method = RequestMethod.POST,
            consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Creating the notification configuration group for a configuration group",
            notes = "This REST service will create the notification config group for a configuration group")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "CREATED"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<List<NotificationAttributeDTO>> createNotification(
            @PathVariable UUID id,
            @Valid @RequestBody List<NotificationAttributeDTO> notificationAttribute)
            throws URISyntaxException, ConfigurationException {
        List<NotificationAttributeDTO> notificatisonAttributeDTOs =
                configurationGroupService.createNotification(id, notificationAttribute);
        return new ResponseEntity<>(notificatisonAttributeDTOs,
                HttpStatus.OK);
    }

    @RequestMapping(value = "/configurations/{id}/notifications", method = RequestMethod.PUT,
            consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Updating Notification for Configuration Group",
            notes = "This REST service will update the notification config group for configuration group")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<List<NotificationAttributeDTO>> updateNotificationConfigurations(
            @Valid @RequestBody List<NotificationAttributeDTO> notificationAttribute,
            @PathVariable UUID id)
            throws URISyntaxException, ConfigurationException, ConfigurationNotFoundException {
        List<NotificationAttributeDTO> notificationAttributeDTOs =
                configurationGroupService.updateNotification(id, notificationAttribute);
        return new ResponseEntity<>(notificationAttributeDTOs,
                HttpStatus.OK);
    }
    
    @ApiOperation(value = "Getting the User group list",
            notes = "This REST service will get all user group list")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequestMapping(value = "/configurations/usergroups", method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<List<UserGroupDTO>> getAllUserGroupWithConfiguredUserGroup(
            @RequestParam("configId") UUID configId) throws Exception {
        List<UserGroupDTO> result =
                configurationGroupService.getUserGroupByConfigurationGroup(configId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Getting the Office locations list",
            notes = "This REST service will get all office locations list for a configuration group")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found")})
    @RequestMapping(value = "/configurations/officelocations", method = RequestMethod.GET,
            produces = APPLICATION_JSON_VALUE)
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<List<OfficeLocationDTO>> getAllOfficeLocationWithConfiguratedLocations(
            @RequestParam("configId") UUID configId) throws ConfigurationException {
        List<OfficeLocationDTO> result =
                configurationGroupService.getOfficeLocationsByConfigurationGroup(configId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }  

    /**
     * Configuration Group ST, OT and DT validation service.
     */
  
    @RequestMapping(value = "/configurations/timeValidation", method = RequestMethod.POST,
        produces = {APPLICATION_JSON_VALUE}, consumes = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Validating the configuration group",
        notes = "This REST service will validate the timesheet configuration")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 422, message = "Unprocessable Entity")})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<String> validateConfiguration(
        @Valid @RequestBody ConfigurationGroupDTO groupDTO)
        throws URISyntaxException, ConfigurationException, ConfigurationGroupException {
      if (configurationGroupService.configurationGroupValidation(groupDTO)) {
        return new ResponseEntity<>("OK", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Unprocessable Entity", HttpStatus.UNPROCESSABLE_ENTITY);
      }
    }
    
    @RequestMapping(value = "/configurations/holidayValidation", method = RequestMethod.POST,
            consumes = APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Validating the holiday configurations",
            notes = "This REST service will validate the holiday configurations")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),@ApiResponse(code =422, message = "Unprocessable Entity")})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<String> createHolidayConfigurationValidation(
            @Valid @RequestBody List<HolidayConfigurationDTO> holidayConfigurationDTOs)
            throws URISyntaxException, HolidayConfigurationException {
        configurationGroupService.createHolidayConfigurationValidation(holidayConfigurationDTOs);
        return new ResponseEntity<>("OK", HttpStatus.OK);

    }
    
}