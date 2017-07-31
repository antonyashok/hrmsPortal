package com.tm.common.configuration.service.hystrix.commands;

import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class LoginAuthTokenCommand {

	private static final String LOGIN_AUTH_KEY = "/realms/timesheet/protocol/openid-connect/token";
	
	private static final String HTTP = "http://";
	
	private static final String GRANT_TYPE = "grant_type";
	
	private static final String PASSWORD = "password";
	
	private static final String CLIENT_ID = "client_id";
	
	private static final String ADMIN_CLI = "admin-cli";
	
	private static final String USER_NAME = "username";

	private RestTemplate restTemplate;

	private String keyCloakServiceUrl;

	private String userId;
	
	private String userPassword;

	private final Logger log = LoggerFactory.getLogger(LoginAuthTokenCommand.class);

	public LoginAuthTokenCommand(RestTemplate restTemplate, String keyCloakServiceUrl, String userId, String userPassword) {
		
		this.restTemplate = restTemplate;
		this.keyCloakServiceUrl = keyCloakServiceUrl.startsWith(HTTP) ? keyCloakServiceUrl 
				: new StringBuilder(HTTP).append(keyCloakServiceUrl).toString();
		this.userId = userId;
		this.userPassword = userPassword;
	}

	public boolean login() {
		try {

			FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
			restTemplate.getMessageConverters().add(formHttpMessageConverter);
			URI uri = new URI(new StringBuilder(keyCloakServiceUrl).append(LOGIN_AUTH_KEY).toString());
			Map<?, ?> response = restTemplate.postForObject(uri, getRequestBody(), Map.class);
			if (response.keySet().contains("access_token")) {
				return true;
			}
		} catch (org.springframework.web.client.HttpClientErrorException exception) {
			log.error("Unauthorized user or Incorrect password", exception);
		} catch (Exception exception) {
			log.error("Error calling key cloak server login auth api through rest template", exception);
		}

		return false;
	}

	private MultiValueMap<String, String> getRequestBody() {

		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add(CLIENT_ID, ADMIN_CLI);
		form.add(GRANT_TYPE, PASSWORD);
		form.add(USER_NAME, userId);
		form.add(PASSWORD, userPassword);
		
		return form;
	}
}
