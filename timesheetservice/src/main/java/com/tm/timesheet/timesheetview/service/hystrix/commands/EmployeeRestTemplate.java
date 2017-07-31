package com.tm.timesheet.timesheetview.service.hystrix.commands;

import java.util.Collections;
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

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.configuration.service.hystrix.commands.OfficeLocationCommand;

public class EmployeeRestTemplate {

	public static final String HTTP = "http://";

	public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";

	private static final String EMPLOYEE_PROFILE_INFO_URI = "/common/employee-profile?emailId=";
	
	/*Account manager employees profile list*/
	private static final String AM_EMPLOYEES_PROFILE_INFO_URI = "/common/employee-profile/am-employees?managerEmployeeId=";

	private RestTemplate restTemplate;

	private String comtrackServiceUrl;

	private String requestedToken;

	private final Logger log = LoggerFactory.getLogger(EmployeeRestTemplate.class);

	public EmployeeRestTemplate(RestTemplate restTemplate, String comtrackServiceUrl, String token) {
		this.restTemplate = restTemplate;
		this.comtrackServiceUrl = comtrackServiceUrl.startsWith(HTTP) ? comtrackServiceUrl
				: new StringBuilder(HTTP).append(comtrackServiceUrl).toString();
		this.requestedToken = token;
	}

	public EmployeeProfileDTO getEmployeeProfileDTO() {
		JWTClaimsSet claimsSet = null;

		try {
			claimsSet = JWTParser.parse(requestedToken).getJWTClaimsSet();

			String email = (String) claimsSet.getClaim("email");
			ParameterizedTypeReference<EmployeeProfileDTO> responseType = new ParameterizedTypeReference<EmployeeProfileDTO>() {
			};
			comtrackServiceUrl = comtrackServiceUrl.startsWith(OfficeLocationCommand.HTTP) ? comtrackServiceUrl
					: new StringBuilder(OfficeLocationCommand.HTTP).append(comtrackServiceUrl).toString();

			ResponseEntity<EmployeeProfileDTO> resp = restTemplate.exchange(
					new StringBuilder(comtrackServiceUrl).append(EMPLOYEE_PROFILE_INFO_URI).append(email).toString(),
					HttpMethod.GET, new HttpEntity<>(createHeaders(requestedToken)), responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			} 
		} catch (Exception exception) {
			log.error("Error collecting employee details from common-services through rest template.", exception);
		}

		return new EmployeeProfileDTO();
	}
	
	public List<EmployeeProfileDTO> getEmployeeProfileDTOs(Long managerEmployeeId) {

		try {
			ParameterizedTypeReference<List<EmployeeProfileDTO>> responseType = new ParameterizedTypeReference<List<EmployeeProfileDTO>>() {
			};
			comtrackServiceUrl = comtrackServiceUrl.startsWith(OfficeLocationCommand.HTTP) ? comtrackServiceUrl
					: new StringBuilder(OfficeLocationCommand.HTTP).append(comtrackServiceUrl).toString();

			ResponseEntity<List<EmployeeProfileDTO>> resp = restTemplate.exchange(
					new StringBuilder(comtrackServiceUrl).append(AM_EMPLOYEES_PROFILE_INFO_URI).append(managerEmployeeId).toString(),
					HttpMethod.GET, new HttpEntity<>(createHeaders(requestedToken)), responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			} 
		} catch (Exception exception) {
			log.error("Error collecting employee details from common-services through rest template.", exception);
		}

		return Collections.<EmployeeProfileDTO> emptyList();
	}

	private static HttpHeaders createHeaders(String token) {
		return new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder(OfficeLocationCommand.BEARER).append(" ").append(token).toString());
			}
		};
	}
}
