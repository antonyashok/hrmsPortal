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

import com.tm.invoice.dto.EmailTaskLog;
import com.tm.invoice.mongo.domain.Status;

public class EmailTaskLogRestTemplate {

    private static final String GETTING_IN_HYSTRIC_RUN = "Getting in hystric run()";

    private final Logger log = LoggerFactory.getLogger(BillingProfileRestTemplate.class);

    private static final String BEARER = "Bearer ";
    
    public static final String TIMETRACK_MAIN_URI = "TIMESHEETMANAGEMENT";
    
    private static final String EMAIL_TASK_URI = "/timetrack/emailTaskLogs";
    
    private String timetrackServiceUrl;    
    
    private RestTemplate restTemplate;
    
    private String requestedToken;
    
    public EmailTaskLogRestTemplate(RestTemplate restTemplate, String timetrackServiceUrl, String token) {
        this.restTemplate = restTemplate;
        this.timetrackServiceUrl = timetrackServiceUrl.startsWith("http://") ? timetrackServiceUrl
                : "http://" + timetrackServiceUrl;
        this.requestedToken = token;
    }
    
    public Status saveEmailTaskLog(EmailTaskLog emailTaskLog) {
        log.debug(EmailTaskLogRestTemplate.GETTING_IN_HYSTRIC_RUN);
        ParameterizedTypeReference<Status> responseType = new ParameterizedTypeReference<Status>() {
        };
        try {
            HttpEntity<?> request = new HttpEntity<>(emailTaskLog, createHeaders(requestedToken));
            ResponseEntity<Status> resp = restTemplate.exchange(
                    timetrackServiceUrl + EMAIL_TASK_URI, HttpMethod.POST, request, responseType);
            if (resp.getStatusCode() == HttpStatus.OK) {
                return resp.getBody();
            }
        } catch (Exception exception) {
            log.error("Error while saving email task log " + exception);
        }
        return null;
    }
    
    private HttpHeaders createHeaders(String token) {
        return new HttpHeaders() {
            private static final long serialVersionUID = -6189386900709137018L;
            {
                String authHeader = BEARER + token;
                set(AUTHORIZATION, authHeader);
                set("Content-Type", "application/json");
            }
        };
    }
    
}
