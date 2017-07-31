package com.tm.engagement.web.rest;

import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.engagement.exception.DateNotNullException;
import com.tm.engagement.repository.EngagementHolidayRepository;
import com.tm.engagement.resource.assemblers.EngagementHolidayResourceAssembler;
import com.tm.engagement.service.resources.HolidayResource;
import com.tm.engagement.web.rest.errors.ErrorConstants;
import com.tm.engagement.web.rest.util.EngagementHolidaysUtil;

import io.swagger.annotations.Api;

@RestController
@Api(value = "holidays", produces = MediaType.APPLICATION_JSON_VALUE)
public class EngagementHolidayResource {

	private EngagementHolidayRepository engagementHolidayRepository;
	private EngagementHolidayResourceAssembler engagementHolidayResourceAssembler;

	protected EngagementHolidayResource(EngagementHolidayRepository engagementHolidayRepository,
			EngagementHolidayResourceAssembler engagementHolidayResourceAssembler) {
		this.engagementHolidayRepository = engagementHolidayRepository;
		this.engagementHolidayResourceAssembler = engagementHolidayResourceAssembler;
	}

	@RequestMapping(value = "/{id}/holidays", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<HolidayResource>> getEngagementDetailsById(@PathVariable String id, String startDate,
			String endDate) throws ParseException {

		if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
			throw new DateNotNullException(ErrorConstants.DATE_NOT_NULL);
		}
		if (StringUtils.isBlank(startDate)) {
			throw new DateNotNullException(ErrorConstants.START_DATE_NOT_NULL);
		}
		if (StringUtils.isBlank(endDate)) {
			throw new DateNotNullException(ErrorConstants.END_DATE_NOT_NULL);
		}
		return new ResponseEntity<>(
				engagementHolidayResourceAssembler.toResources(engagementHolidayRepository.findByEngagementIdBetween(id,
						EngagementHolidaysUtil.getDate(startDate), EngagementHolidaysUtil.getDate(endDate))),
				HttpStatus.OK);
	}
}
