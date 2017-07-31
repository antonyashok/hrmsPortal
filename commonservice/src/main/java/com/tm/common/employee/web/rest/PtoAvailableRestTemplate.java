package com.tm.common.employee.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tm.common.employee.service.dto.PtoAvailableDTO;

public class PtoAvailableRestTemplate {

	private static final String ERROR_COLLECTING_ENGAGEMENTS = "Error collecting Projects.";

	private static final String GETTING_IN_HYSTRIC_RUN = "Getting in hystric run()";

	private final Logger log = LoggerFactory.getLogger(PtoAvailableRestTemplate.class);

	private static final String BEARER = "Bearer ";

	public static final String COMTRACK_GROUP_KEY_TIMEOFF = "TIMESHEETMANAGEMENT";

	private static final String COMTRACK_TIMEOFF_URI = "/timetrack/ptoAvailable";
	private String comtrackServiceUrl;
	private RestTemplate restTemplate;
	private String requestedToken;

	public PtoAvailableRestTemplate(RestTemplate restTemplate, String comtrackServiceUrl, String token) {
		this.restTemplate = restTemplate;
		this.comtrackServiceUrl = comtrackServiceUrl.startsWith("http://") ? comtrackServiceUrl
				: "http://" + comtrackServiceUrl;
		this.requestedToken = token;
	}

	public String createPTOAvailable(PtoAvailableDTO ptoAvailableDTO) {
		log.debug(PtoAvailableRestTemplate.GETTING_IN_HYSTRIC_RUN);
		ParameterizedTypeReference<PtoAvailableDTO> responseType = new ParameterizedTypeReference<PtoAvailableDTO>() {
		};
		try {
			HttpEntity<?> request = new HttpEntity<>(ptoAvailableDTO, createHeaders(requestedToken));
			ResponseEntity<PtoAvailableDTO> resp = restTemplate.exchange(comtrackServiceUrl + COMTRACK_TIMEOFF_URI,
					HttpMethod.POST, request, responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return "Success";
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			log.error(PtoAvailableRestTemplate.ERROR_COLLECTING_ENGAGEMENTS + exception);
		}
		return null;
	}

	private HttpHeaders createHeaders(String token) {
		return new HttpHeaders() {
			private static final long serialVersionUID = -6189386900709137018L;
			{
				String authHeader = BEARER + token;
				set(AUTHORIZATION, authHeader);
				set("Content-Type", "application/json");
			}
		};
	}

}
