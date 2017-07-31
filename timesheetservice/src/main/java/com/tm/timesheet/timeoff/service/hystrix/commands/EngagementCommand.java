package com.tm.timesheet.timeoff.service.hystrix.commands;

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

import com.tm.timesheet.service.dto.EngagementDTO;

public class EngagementCommand {

	private final Logger log = LoggerFactory.getLogger(EngagementCommand.class);

	private static final String BEARER = "Bearer ";
	public static final String COMTRACK_GROUP_KEY = "ENGAGEMENTMANAGEMENT";
	private static final String COMTRACK_ENGAGEMENT_HOLIDAY_URI = "/engagements";
	private static final String COMTRACK_HOLIDAY_URI = "engagements";
	private String comtrackServiceUrl;
	private RestTemplate restTemplate;
	private String requestedToken;
	private Long userId;

	public EngagementCommand(RestTemplate restTemplate, String comtrackServiceUrl, String token, Long userId) {
		this.restTemplate = restTemplate;
		this.comtrackServiceUrl = comtrackServiceUrl.startsWith("http://") ? comtrackServiceUrl
				: "http://" + comtrackServiceUrl;
		this.requestedToken = token;
		this.userId = userId;
	}

	public List<EngagementDTO> getEngagements() {
		log.debug("Getting in hystric run()");
		ParameterizedTypeReference<List<EngagementDTO>> responseType = new ParameterizedTypeReference<List<EngagementDTO>>() {
		};
		try {
			log.error("" + comtrackServiceUrl + COMTRACK_ENGAGEMENT_HOLIDAY_URI + "/" + COMTRACK_HOLIDAY_URI
					+ "?userId=" + userId);
			ResponseEntity<List<EngagementDTO>> resp = restTemplate
					.exchange(
							comtrackServiceUrl + COMTRACK_ENGAGEMENT_HOLIDAY_URI + "/" + COMTRACK_HOLIDAY_URI
									+ "?userId=" + userId,
							HttpMethod.GET, new HttpEntity<>(createHeaders(requestedToken)), responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			}
		} catch (Exception exception) {
			log.error("Error collecting Projects.");
		}
		return null;

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
