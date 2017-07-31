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

import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.configuration.service.hystrix.commands.OfficeLocationCommand;

public class EmployeeCommand {

	public static final String HTTP = "http://";

	public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";

	private static final String EMPLOYEE_PROFILE_INFO_URI = "/common/employee-profile";
	
	private RestTemplate restTemplate;

	private String comtrackServiceUrl;

	private String requestedToken;
	
	private Long employeeId;

	private final Logger log = LoggerFactory.getLogger(EmployeeCommand.class);

	public EmployeeCommand(RestTemplate restTemplate, String comtrackServiceUrl, String token,Long employeeId) {
		this.restTemplate = restTemplate;
		this.comtrackServiceUrl = comtrackServiceUrl.startsWith(HTTP) ? comtrackServiceUrl
				: new StringBuilder(HTTP).append(comtrackServiceUrl).toString();
		this.requestedToken = token;
		this.employeeId=employeeId;
	}

	public EmployeeProfileDTO getEmployeeProfileDTO() {
		
		log.debug("Getting in hystric run()");
		ParameterizedTypeReference<EmployeeProfileDTO> responseType = new ParameterizedTypeReference<EmployeeProfileDTO>() {
		};
		ResponseEntity<EmployeeProfileDTO> resp = null;
		try {					
			resp = restTemplate.exchange(
					comtrackServiceUrl + EMPLOYEE_PROFILE_INFO_URI + "/" + employeeId, HttpMethod.GET,
					new HttpEntity<>(createHeaders(requestedToken)), responseType);
			if (resp.getStatusCode() == HttpStatus.OK) {
				return resp.getBody();
			} 
		} catch (Exception e) {
			log.error("Error in collecting employee profile details from employee-profile service.", e);
			e.printStackTrace();
		}
		return new EmployeeProfileDTO();
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
