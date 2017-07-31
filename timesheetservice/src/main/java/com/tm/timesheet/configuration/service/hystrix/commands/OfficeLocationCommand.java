/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.service.hystrix.commands.OfficeLocationCommand.java
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
import com.tm.timesheet.configuration.service.dto.OfficeLocationDTO;

public class OfficeLocationCommand extends
        HystrixCommand<List<OfficeLocationDTO>> {

    private final Logger log = LoggerFactory
            .getLogger(OfficeLocationCommand.class);

    public static final String HTTP = "http://";
    
    public static final String BEARER = "Bearer";

    public static final String COMMON_GROUP_KEY = "COMMONSERVICEMANAGEMENT";

    private static final String COMMON_OFFICE_LOCATION_URI = "/common/officelocations";

    private String comtrackServiceUrl;

    private RestTemplate restTemplate;

    // Propagate request header to get this token
    private String requestedToken;

    public OfficeLocationCommand(RestTemplate restTemplate,
            String comtrackServiceUrl, String token) {
        super(HystrixCommandGroupKey.Factory.asKey(COMMON_GROUP_KEY));
        this.restTemplate = restTemplate;
        this.comtrackServiceUrl = comtrackServiceUrl.startsWith(HTTP) ? comtrackServiceUrl
                : new StringBuilder(HTTP).append(comtrackServiceUrl)
                        .toString();
        this.requestedToken = token;
    }

    @Override
    protected List<OfficeLocationDTO> run() throws Exception {
        ParameterizedTypeReference<List<OfficeLocationDTO>> responseType = new ParameterizedTypeReference<List<OfficeLocationDTO>>() {
        };
        try {
	        ResponseEntity<List<OfficeLocationDTO>> resp = restTemplate.exchange(
	                new StringBuilder(comtrackServiceUrl).append(
	                        COMMON_OFFICE_LOCATION_URI).toString(),
	                HttpMethod.GET,
	                new HttpEntity<>(createHeaders(requestedToken)), responseType);
	
	        if (resp.getStatusCode() == HttpStatus.OK) {
	            try {
	                return resp.getBody();
	            } catch (Exception exception) {
	                log.error(
	                        "Error collecting office locations from common-services.",
	                        exception);
	            }
	        }
        } catch (Exception exception) {
            log.error(
                    "Error collecting office locations from common-services.",
                    exception);
        }
        return Collections.<OfficeLocationDTO> emptyList();

    }

    @Override
    protected List<OfficeLocationDTO> getFallback() {
        log.info("Fallback calling for.. Returning empty office locations.");
        return Collections.<OfficeLocationDTO> emptyList();
    }

    private HttpHeaders createHeaders(String token) {
        return new HttpHeaders() {
            private static final long serialVersionUID = -6189386900709137018L;
            {
                set(HttpHeaders.AUTHORIZATION, new StringBuilder(BEARER).append(" ").append(token).toString());
            }
        };
    }
}
