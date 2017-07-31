package com.tm.engagement.configuration.service.hystrix.commands;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tm.engagement.constants.EngagementConstants;
import com.tm.engagement.service.dto.CompanyLocationDTO;
import com.tm.engagement.service.dto.CompanyProfileDTO;
import com.tm.engagement.service.dto.OfficeLocationDTO;

public class OfficeLocationCommand {

	private final Logger log = LoggerFactory.getLogger(OfficeLocationCommand.class);

	private static final String COMMON_OFFICE_LOCATION_URI = "/common/officelocations";
	
	private static final String COMMON_SAVE_OFFICE_LOCATION_URI = "/common/saveOfficeLocation";
	
	private static final String COMMON_COMPANY_PROFILE_URI = "/common/companyProfileEdit/";
	
	private static final String COMMON_GET_OFFICE_LOCATION_URI = "/common/getOfficeLocationsByOfficeIds";

	private String comtrackServiceUrl;

	private RestTemplate restTemplate;

	private String requestedToken;

	public OfficeLocationCommand(RestTemplate restTemplate, String comtrackServiceUrl, String token) {
		this.restTemplate = restTemplate;
		this.comtrackServiceUrl = comtrackServiceUrl.startsWith(EngagementConstants.HTTP) ? comtrackServiceUrl
				: new StringBuilder(EngagementConstants.HTTP).append(comtrackServiceUrl).toString();
		this.requestedToken = token;
	}

	public List<OfficeLocationDTO> getAllOfficeLocation() {
		ParameterizedTypeReference<List<OfficeLocationDTO>> responseType = new ParameterizedTypeReference<List<OfficeLocationDTO>>() {
		};
		try {
			ResponseEntity<List<OfficeLocationDTO>> resp = restTemplate.exchange(
					new StringBuilder(comtrackServiceUrl).append(COMMON_OFFICE_LOCATION_URI).toString(), HttpMethod.GET,
					new HttpEntity<>(createHeaders(requestedToken)), responseType);

			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			}
		} catch (Exception exception) {
			log.error("Error collecting office locations from common-services.", exception);
		}
		return Collections.<OfficeLocationDTO>emptyList();
	}

	public List<CompanyLocationDTO> saveOfficeLocation(CompanyProfileDTO customerProfile) {
		ParameterizedTypeReference<List<CompanyLocationDTO>> responseType = new ParameterizedTypeReference<List<CompanyLocationDTO>>() {
		};
		try {
			HttpEntity<?> request = new HttpEntity<>(customerProfile, createHeaders(requestedToken));
			ResponseEntity<List<CompanyLocationDTO>> resp = restTemplate.exchange(comtrackServiceUrl + COMMON_SAVE_OFFICE_LOCATION_URI,
					HttpMethod.POST, request, responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			}
		} catch (Exception exception) {
			log.error("Error while saveOfficeLocation() :: "+ exception);
		}
		return Collections.<CompanyLocationDTO>emptyList();
	}
	
	public Map<String, Object> getCompanyProfile(String id) {
		ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {
		};
		try {
			HttpEntity<?> request = new HttpEntity<>(id, createHeaders(requestedToken));
			ResponseEntity<Map<String, Object>> resp = restTemplate.exchange(comtrackServiceUrl + COMMON_COMPANY_PROFILE_URI,
					HttpMethod.POST, request, responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			}
		} catch (Exception exception) {
			log.error("Error while getCompanyProfile() :: "+ exception);
		}
		return new HashMap<>();
	}
	
	public List<CompanyLocationDTO> getOfficeLocationByCustomerId(CompanyProfileDTO customerProfileDTO) {
		ParameterizedTypeReference<List<CompanyLocationDTO>> responseType = new ParameterizedTypeReference<List<CompanyLocationDTO>>() {
		};
		try {
			HttpEntity<?> request = new HttpEntity<>(customerProfileDTO, createHeaders(requestedToken));
			ResponseEntity<List<CompanyLocationDTO>> resp = restTemplate.exchange(comtrackServiceUrl + COMMON_GET_OFFICE_LOCATION_URI,
					HttpMethod.POST, request, responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			}
		} catch (Exception exception) {
			log.error("Error while getCompanyProfile() :: "+ exception);
		}
		return Collections.<CompanyLocationDTO>emptyList();
	}
	
	private HttpHeaders createHeaders(String token) {
		return new HttpHeaders() {
			private static final long serialVersionUID = -6189386900709137018L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder(EngagementConstants.BEARER).append(" ").append(token).toString());
			}
		};
	}
}
