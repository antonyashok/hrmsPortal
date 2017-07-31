package com.tm.timesheet.timeoff.service.hystrix.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tm.timesheet.timeoff.service.dto.ContractorEmployeeEngagementView;

public class EmployeeEngagementCommand {

	private final Logger log = LoggerFactory
			.getLogger(EmployeeEngagementCommand.class);

	private static final String BEARER = "Bearer ";
	private static final String COMTRACK_HOLIDAY_URI = "/common/engagements/employeeengagementdetails";
	private String comtrackServiceUrl;
	private RestTemplate restTemplate;
	private String requestedToken;
	private Long userId;
	private String engagementId;

	public EmployeeEngagementCommand(RestTemplate restTemplate,
			String comtrackServiceUrl, String token, Long userId,
			String engagementId) {
		this.restTemplate = restTemplate;
		this.comtrackServiceUrl = comtrackServiceUrl.startsWith("http://") ? comtrackServiceUrl
				: "http://" + comtrackServiceUrl;
		this.requestedToken = token;
		this.userId = userId;
		this.engagementId = engagementId;
	}

	public ContractorEmployeeEngagementView getEmployeeEngagementDetails() {
		ParameterizedTypeReference<ContractorEmployeeEngagementView> responseType = new ParameterizedTypeReference<ContractorEmployeeEngagementView>() {
		};
		try {
			ResponseEntity<ContractorEmployeeEngagementView> resp = restTemplate
					.exchange(comtrackServiceUrl + COMTRACK_HOLIDAY_URI
							+ "?userId=" + userId + "&engagementId="
							+ engagementId, HttpMethod.GET, new HttpEntity<>(
							createHeaders(requestedToken)), responseType);

			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			}
		} catch (Exception ex) {
			log.error("Error collecting from employee engagement common-services."
					+ ex);
		}
		return new ContractorEmployeeEngagementView();

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
