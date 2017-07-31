package com.tm.commonapi.rest.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tm.commonapi.domain.UserInfoData;
import com.tm.commonapi.exception.AuthoritiesException;

public class EmployeeRestTemplate {

	private static final String EMPLOYEE_PROFILE_INFO_URI = "/common/getUserInfo";
	private RestTemplate restTemplate;
	private String comtrackServiceUrl;
	private String requestedToken;

	private final Logger log = LoggerFactory.getLogger(EmployeeRestTemplate.class);

	public EmployeeRestTemplate(@LoadBalanced RestTemplate restTemplate, String comtrackServiceUrl, String token) {
		this.restTemplate = restTemplate;
		this.comtrackServiceUrl = comtrackServiceUrl.startsWith("http://") ? comtrackServiceUrl
				: new StringBuilder("http://").append(comtrackServiceUrl).toString();
		this.requestedToken = token;
	}

	public UserInfoData getUserInfoData() {
		try {
			ParameterizedTypeReference<UserInfoData> responseType = new ParameterizedTypeReference<UserInfoData>() {
			};
			comtrackServiceUrl = comtrackServiceUrl.startsWith("http://") ? comtrackServiceUrl
					: new StringBuilder("http://").append(comtrackServiceUrl).toString();
			ResponseEntity<UserInfoData> resp = restTemplate.exchange(
					new StringBuilder(comtrackServiceUrl).append(EMPLOYEE_PROFILE_INFO_URI).toString(),
					HttpMethod.GET, new HttpEntity<>(createHeaders(requestedToken)), responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			}
		} catch (Exception exception) {
			log.error("Error collecting employee details from common-services through rest template.", exception);
			throw new AuthoritiesException("Authority rest template exception");
		}
		return new UserInfoData();
	}

	private static HttpHeaders createHeaders(String token) {
		return new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L; {
				set(HttpHeaders.AUTHORIZATION, new StringBuilder("Bearer").append(" ").append(token).toString());
			}
		};
	}
}