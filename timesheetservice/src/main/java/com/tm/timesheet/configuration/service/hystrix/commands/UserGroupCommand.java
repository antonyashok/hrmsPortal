/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.service.hystrix.commands.UserGroupCommand.java
 * Author        : Annamalai L
 * Date Created  : Jan 18, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.service.hystrix.commands;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tm.timesheet.configuration.service.dto.UserGroupDTO;

public class UserGroupCommand {

	private static final String COMMON_OFFICE_LOCATION_URI = "/common/usergroups";

	private String comtrackServiceUrl;

	private String requestedToken;

	@Autowired
	private RestTemplate restTemplate;

	public UserGroupCommand(RestTemplate restTemplate, String comtrackServiceUrl, String requestedToken) {
		this.restTemplate = restTemplate;
		this.requestedToken = requestedToken;
		this.comtrackServiceUrl = comtrackServiceUrl.startsWith(OfficeLocationCommand.HTTP) ? comtrackServiceUrl
				: new StringBuilder(OfficeLocationCommand.HTTP).append(comtrackServiceUrl).toString();
	}

	public List<UserGroupDTO> getAllJobTitle() throws Exception {
		ParameterizedTypeReference<List<UserGroupDTO>> responseType = new ParameterizedTypeReference<List<UserGroupDTO>>() {
		};

		ResponseEntity<List<UserGroupDTO>> resp = restTemplate.exchange(
				new StringBuilder(comtrackServiceUrl).append(COMMON_OFFICE_LOCATION_URI).toString(), HttpMethod.GET,
				new HttpEntity<>(createHeaders(requestedToken)), responseType);

		if (resp.getStatusCode() == HttpStatus.OK) {
			return resp.getBody();
		}
		return Collections.<UserGroupDTO>emptyList();
	}

	private HttpHeaders createHeaders(String token) {
		return new HttpHeaders() {
			private static final long serialVersionUID = -8149583024132678955L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder(OfficeLocationCommand.BEARER).append(" ").append(token).toString());
			}
		};
	}
}
