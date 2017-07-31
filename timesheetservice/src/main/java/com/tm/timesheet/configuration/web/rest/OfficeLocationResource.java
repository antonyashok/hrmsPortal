/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.web.rest.OfficeLocationResource.java
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

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.RequiredAuthority;
import com.tm.timesheet.configuration.exception.ConfigurationException;
import com.tm.timesheet.configuration.service.ConfigurationGroupService;
import com.tm.timesheet.configuration.service.dto.OfficeLocationDTO;
import com.tm.timesheet.constants.TimesheetConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * REST controller for managing OfficeLocation.
 */
@RestController
@Api(value = "locations", produces =  APPLICATION_JSON_VALUE,
        consumes = APPLICATION_JSON_VALUE)
public class OfficeLocationResource {

    private ConfigurationGroupService configurationGroupService;

	@Inject
	public OfficeLocationResource(ConfigurationGroupService configurationGroupService) {
		this.configurationGroupService = configurationGroupService;
	}

	
    @ApiOperation(value = "Getting the configured Office locations list",
            notes = "This REST service will get all the configured office locations list")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "List<OfficeLocationDTO>"),
            @ApiResponse(code = 404, message = "Not Found") })
    @RequestMapping(value = "/locations", method = RequestMethod.GET,
            produces =  {APPLICATION_JSON_VALUE})
    @RequiredAuthority({ TimesheetConstants.SUPER_ADMIN })
    public ResponseEntity<List<OfficeLocationDTO>> getConfiguredOfficeLocationsByUserGroup(
            @RequestParam("configId") UUID configurationGroupId,
           @RequestParam String userGroupIds) throws ConfigurationException {
        List<OfficeLocationDTO> result = configurationGroupService.getConfiguredOfficeIdsByConfigIdOrUserGroupIds(configurationGroupId, userGroupIds);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }	
}