package com.tm.invoice.service.resttemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tm.invoice.domain.PtoAvailableDTO;
import com.tm.invoice.dto.BillingQueueDTO;

public class BillingProfileRestTemplate {

	private static final String ERROR_COLLECTING_ENGAGEMENTS = "Error collecting Projects.";

	private static final String GETTING_IN_HYSTRIC_RUN = "Getting in hystric run()";

	private final Logger log = LoggerFactory.getLogger(BillingProfileRestTemplate.class);

	private static final String BEARER = "Bearer ";
	public static final String COMTRACK_GROUP_KEY = "ENGAGEMENTMANAGEMENT";
	private static final String COMTRACK_ENGAGEMENT_HOLIDAY_URI = "/engagements/employeeEngagement";
	
	public static final String COMTRACK_GROUP_KEY_TIMEOFF = "TIMESHEETMANAGEMENT";
	private static final String COMTRACK_TIMEOFF_URI = "/timetrack/ptoAvailable";
	private String comtrackServiceUrl;
	private RestTemplate restTemplate;
	private String requestedToken;

	public BillingProfileRestTemplate(RestTemplate restTemplate, String comtrackServiceUrl, String token) {
		this.restTemplate = restTemplate;
		this.comtrackServiceUrl = comtrackServiceUrl.startsWith("http://") ? comtrackServiceUrl
				: "http://" + comtrackServiceUrl;
		this.requestedToken = token;
	}

	public BillingQueueDTO createBillingProfile(BillingQueueDTO billingQueueDTO) {
		log.debug(BillingProfileRestTemplate.GETTING_IN_HYSTRIC_RUN);
		ParameterizedTypeReference<BillingQueueDTO> responseType = new ParameterizedTypeReference<BillingQueueDTO>() {
		};
		try {
			HttpEntity<?> request = new HttpEntity<>(billingQueueDTO, createHeaders(requestedToken));
			ResponseEntity<BillingQueueDTO> resp = restTemplate.exchange(
					comtrackServiceUrl + COMTRACK_ENGAGEMENT_HOLIDAY_URI, HttpMethod.POST, request, responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			}
		} catch (Exception exception) {
			log.error(BillingProfileRestTemplate.ERROR_COLLECTING_ENGAGEMENTS+exception);
		}
		return null;
	}
	
	public String updateBillingProfile(String employeeEngagementId) {
		log.debug(BillingProfileRestTemplate.GETTING_IN_HYSTRIC_RUN);
		ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
		};
		try {
			HttpEntity<?> request = new HttpEntity<>(employeeEngagementId, createHeaders(requestedToken));
			ResponseEntity<String> resp = restTemplate.exchange(
					comtrackServiceUrl + COMTRACK_ENGAGEMENT_HOLIDAY_URI+"/"+employeeEngagementId, HttpMethod.PUT, request, responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			}
		} catch (Exception exception) {
			log.error(BillingProfileRestTemplate.ERROR_COLLECTING_ENGAGEMENTS+exception);
		}
		return null;
	}
	
	public String createPTOAvailable(PtoAvailableDTO ptoAvailableDTO) {
		log.debug(BillingProfileRestTemplate.GETTING_IN_HYSTRIC_RUN);
		ParameterizedTypeReference<PtoAvailableDTO> responseType = new ParameterizedTypeReference<PtoAvailableDTO>() {
		};
		try {
			HttpEntity<?> request = new HttpEntity<>(ptoAvailableDTO, createHeaders(requestedToken));
			ResponseEntity<PtoAvailableDTO> resp = restTemplate.exchange(
					comtrackServiceUrl + COMTRACK_TIMEOFF_URI, HttpMethod.POST, request,responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return "Success";
			}
		} catch (Exception exception) {
			log.error(BillingProfileRestTemplate.ERROR_COLLECTING_ENGAGEMENTS+exception);
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
