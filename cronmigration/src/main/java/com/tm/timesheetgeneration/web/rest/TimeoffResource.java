package com.tm.timesheetgeneration.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "sample", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class TimeoffResource {

	private static final Logger log = LoggerFactory.getLogger(TimeoffResource.class);

	@ApiOperation(value = "Getting the timeoff for logged in user", notes = "This REST service will get the timeoff requests of the logged in user for the given status")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "TimeoffDTO"),
			@ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/sampleservice", method = RequestMethod.GET)
	public void getMyTimeoffList() {

		log.info("sample cron service");

	}

}