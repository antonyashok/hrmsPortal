package com.tm.common.configuration.service.hystrix.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tm.common.util.EmployeeConstants;

public class DashboardWidgetsRestTemplate<T> {

	private RestTemplate restTemplate;

	private String requestedToken;
	
	private String url;
	
	private HttpMethod httpMethod;

	private final Logger log = LoggerFactory.getLogger(DashboardWidgetsRestTemplate.class);

	public DashboardWidgetsRestTemplate(RestTemplate restTemplate, String url, String token, HttpMethod httpMethod) {
		this.restTemplate = restTemplate;
		this.url = url;
		this.requestedToken = token;
		this.httpMethod = httpMethod;
	}

	public T getResponse() {

		try {
			ParameterizedTypeReference<T> responseType = new ParameterizedTypeReference<T>() {
			};
			ResponseEntity<T> resp = restTemplate.exchange(url, httpMethod, new HttpEntity<>(createHeaders(requestedToken)), responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			}
		} catch (Exception exception) {
			log.error("Error calling dashboard services through rest template.", exception);
		}

		return null;
	}

	private static HttpHeaders createHeaders(String token) {
		return new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION, new StringBuilder(
						EmployeeConstants.BEARER).append(" ").append(token).toString());
			}
		};
	}
}
