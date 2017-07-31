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

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.tm.employee.dto.EmployeeDTO;
import com.tm.invoice.dto.EmployeeProfileDTO;

public class EmployeeRestTemplate {

	public static final String BEARER = "Bearer";
	
	public static final String HTTP = "http://";

	public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";

	private static final String EMPLOYEE_PROFILE_INFO_URI = "/common/employee-profile?emailId=";
	
	private static final String EMPLOYEE_PROFILE_INFO = "/common/employee-profile/";

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
			comtrackServiceUrl = comtrackServiceUrl.startsWith(HTTP) ? comtrackServiceUrl
					: new StringBuilder(HTTP).append(comtrackServiceUrl).toString();

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

	private static HttpHeaders createHeaders(String token) {
		return new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder(BEARER).append(" ").append(token).toString());
			}
		};
	}
	
	public EmployeeDTO getEmployeeDetails(Long employeeId) {
	    ParameterizedTypeReference<EmployeeDTO> responseType = new ParameterizedTypeReference<EmployeeDTO>() {
        };
        try {
            ResponseEntity<EmployeeDTO> resp = restTemplate.exchange(
                    new StringBuilder(comtrackServiceUrl)
                            .append(EMPLOYEE_PROFILE_INFO)
                            .append(employeeId).toString(), HttpMethod.GET,
                    new HttpEntity<>(createHeaders(requestedToken)),
                    responseType);
            if (resp.getStatusCode() == HttpStatus.OK) {
                return resp.getBody();
            }
        } catch (Exception exception) {
            log.error(
                    "Error collecting employee details from employee-services through rest template.",
                    exception);
        }
        return new EmployeeDTO();
    }

	
}
