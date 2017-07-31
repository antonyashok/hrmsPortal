/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.web.rest.NotificationAttributeResource.java
 * Author        : Annamalai L
 * Date Created  : Jan 21, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.RequiredAuthority;
import com.tm.timesheet.configuration.exception.ConfigurationException;
import com.tm.timesheet.configuration.exception.ConfigurationNotFoundException;
import com.tm.timesheet.configuration.service.ConfigurationGroupService;
import com.tm.timesheet.configuration.service.dto.NotificationAttributeDTO;
import com.tm.timesheet.constants.TimesheetConstants;

/**
 * REST controller for managing NotificationAttribute.
 */
@RestController
@Api(value = "notification-attributes", produces = APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE)
public class NotificationAttributeResource {
	
	private ConfigurationGroupService configurationGroupService;
	
	@Inject
    public NotificationAttributeResource(ConfigurationGroupService configurationGroupService) {
        this.configurationGroupService = configurationGroupService;
    }
	
    @RequestMapping(value = "/notification-attributes", method = RequestMethod.GET,
            produces =  { APPLICATION_JSON_VALUE } )
    @ApiOperation(
            value = "Getting the particular ConfigurationGroup NotificationConfigurations",
            notes = "This REST service will get the particular configurationGroup notificationConfiguration list")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "NotificationAttributeDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
	public ResponseEntity<List<NotificationAttributeDTO>> getNotificationAttributes(
			@RequestParam("userGroupCategory") String userGroupCategory)
            throws ConfigurationException, ConfigurationNotFoundException {
        List<NotificationAttributeDTO> notificationAttributeDTOs = configurationGroupService
                .getNotificationAttributes(userGroupCategory);
        return Optional.ofNullable(notificationAttributeDTOs)
                .map(result -> new ResponseEntity<>(notificationAttributeDTOs,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}	
}