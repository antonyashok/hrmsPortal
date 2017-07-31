package com.tm.engagement.web.rest;

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

import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.engagement.exception.EngagementException;
import com.tm.engagement.service.EngagementService;
import com.tm.engagement.service.dto.OfficeLocationDTO;

@RestController
public class OfficeLocationResource {

	private EngagementService engagementService;

	@Inject
	public OfficeLocationResource(EngagementService engagementService) {
		this.engagementService = engagementService;
	}

	@RequestMapping(value = "/locations", method = RequestMethod.GET, produces = { APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN })
	public ResponseEntity<List<OfficeLocationDTO>> getConfiguredOfficeLocations(
			@RequestParam("engagementId") UUID engagementId) throws EngagementException {
		List<OfficeLocationDTO> result = engagementService.getConfiguredOfficeLocations(engagementId);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}