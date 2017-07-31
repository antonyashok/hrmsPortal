package com.tm.timesheet.timeoff.service.hystrix.commands;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tm.timesheet.timeoff.service.dto.EntityAttributeDTO;

public class PtoTypeCommand {

	private final Logger log = LoggerFactory.getLogger(PtoTypeCommand.class);

	public static final String HTTP = "http://";
	private static final String BEARER = "Bearer ";
	private static final String COMTRACK_HOLIDAY_URI = "/common/entity-values/lookup?attribute=TIMEOFF_PTO_TYPE_ID&entity=TIME_OFF";
	private String comtrackServiceUrl;
	private RestTemplate restTemplate;
	private String requestedToken;

	public PtoTypeCommand(RestTemplate restTemplate, String comtrackServiceUrl, String token) {
		this.restTemplate = restTemplate;
		this.comtrackServiceUrl = comtrackServiceUrl.startsWith(HTTP) ? comtrackServiceUrl
				: new StringBuilder(HTTP).append(comtrackServiceUrl).toString();
		this.requestedToken = token;
	}

	public List<EntityAttributeDTO> getPtoType(){
		log.debug("Getting in hystric run()");
		ParameterizedTypeReference<List<EntityAttributeDTO>> responseType = new ParameterizedTypeReference<List<EntityAttributeDTO>>() {
		};
		ResponseEntity<List<EntityAttributeDTO>> resp = restTemplate
				.exchange(
						comtrackServiceUrl + COMTRACK_HOLIDAY_URI ,
						HttpMethod.GET, new HttpEntity<>(createHeaders(requestedToken)), responseType);
		
		if (resp.getStatusCode() == HttpStatus.OK) {
			try {							
				List<EntityAttributeDTO> holidayList = resp.getBody();
				return holidayList;
			} catch (Exception exception) {
				log.error("Error collecting holidays from holiday-services.");
			}
		}
		return new ArrayList<>();

	}

	private HttpHeaders createHeaders(String token) {
		return new HttpHeaders() {
			private static final long serialVersionUID = -6189386900709137018L;
			{
				String authHeader = BEARER + token;
				set(AUTHORIZATION, authHeader);
			}
		};
	}

}
