/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.common.service.hystrix.commands.EmployeeProfileCommand.java
 * Author        : Annamalai L
 * Date Created  : March 20, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.common.service.hystrix.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.configuration.service.hystrix.commands.OfficeLocationCommand;

public class EmployeeProfileCommand extends HystrixCommand<EmployeeProfileDTO> {

	private final Logger log = LoggerFactory
			.getLogger(EmployeeProfileCommand.class);

	private static final String EMPLOYEE_PROFILE_INFO_URI = "/common/employee-profile?emailId=";

	private String comtrackServiceUrl;

	private RestTemplate restTemplate;

	private String requestedToken;

	public EmployeeProfileCommand(RestTemplate restTemplate,
			String comtrackServiceUrl, String token) {
		super(HystrixCommandGroupKey.Factory
				.asKey(OfficeLocationCommand.COMMON_GROUP_KEY));
		this.restTemplate = restTemplate;
		this.comtrackServiceUrl = comtrackServiceUrl
				.startsWith(OfficeLocationCommand.HTTP) ? comtrackServiceUrl
				: new StringBuilder(OfficeLocationCommand.HTTP).append(
						comtrackServiceUrl).toString();
		this.requestedToken = token;
	}

	@Override
	protected EmployeeProfileDTO run() throws Exception {

		ParameterizedTypeReference<EmployeeProfileDTO> responseType = new ParameterizedTypeReference<EmployeeProfileDTO>() {
		};

		JWTClaimsSet claimsSet;
		try {
			claimsSet = JWTParser.parse(requestedToken).getJWTClaimsSet();
			String email = (String) claimsSet.getClaim("email");

			ResponseEntity<EmployeeProfileDTO> resp = this.restTemplate
					.exchange(
							new StringBuilder(comtrackServiceUrl)
									.append(EMPLOYEE_PROFILE_INFO_URI)
									.append(email).toString(), HttpMethod.GET,
							new HttpEntity<>(createHeaders(requestedToken)),
							responseType);

			if (resp.getStatusCode() == HttpStatus.OK) {
				try {
					return resp.getBody();
				} catch (Exception exception) {
					log.error(
							"Error collecting employee profile from common-services.",
							exception);
				}
			}
		} catch (Exception exception) {
			log.error(
					"Error collecting employee profile from common rest services.",
					exception);
		}
		return new EmployeeProfileDTO();

	}

	@Override
	protected EmployeeProfileDTO getFallback() {
		log.info("Fallback calling for.. Returning empty employee profile.");
		return new EmployeeProfileDTO();
	}

	private HttpHeaders createHeaders(String token) {
		return new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION, new StringBuilder(
						OfficeLocationCommand.BEARER).append(" ").append(token)
						.toString());
			}
		};
	}
}
