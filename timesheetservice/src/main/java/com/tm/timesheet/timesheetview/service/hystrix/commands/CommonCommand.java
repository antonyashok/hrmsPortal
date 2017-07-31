package com.tm.timesheet.timesheetview.service.hystrix.commands;

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
import com.tm.timesheet.configuration.service.hystrix.commands.OfficeLocationCommand;
import com.tm.timesheet.service.dto.CommonEngagementDTO;

public class CommonCommand extends HystrixCommand<CommonEngagementDTO> {

    private final Logger log = LoggerFactory.getLogger(CommonCommand.class);

    public static final String BEARER = "Bearer ";

    public static final String AUTHORIZATION = "Authorization";

    public static final String COMTRACK_GROUP_KEY = "EMPLOYEEMANAGEMENT";

    private static final String EMPLOYEE_MANAGEMENT_URI = "/common/engagements";

    private String comtrackServiceUrl;

    private RestTemplate restTemplate;

    private String requestedToken;

	public CommonCommand(RestTemplate restTemplate, String comtrackServiceUrl, String token,
			String engagementId, Long contractorId) {
		super(HystrixCommandGroupKey.Factory
				.asKey(OfficeLocationCommand.COMMON_GROUP_KEY));
		this.restTemplate = restTemplate;
		this.comtrackServiceUrl = comtrackServiceUrl.startsWith("http://") ? comtrackServiceUrl
				: "http://" + comtrackServiceUrl + EMPLOYEE_MANAGEMENT_URI + "/" + engagementId + "/tasks?contractorId="
						+ contractorId;
		this.requestedToken = token;
	}

	@Override
	protected CommonEngagementDTO run() throws Exception {
		log.info("Getting in hystric run()");
		ParameterizedTypeReference<CommonEngagementDTO> responseType = new ParameterizedTypeReference<CommonEngagementDTO>() {
		};
		CommonEngagementDTO commonEngagementDTO = new CommonEngagementDTO();
		try {
			ResponseEntity<CommonEngagementDTO> resp = restTemplate.exchange(comtrackServiceUrl, HttpMethod.GET,
					new HttpEntity<>(createHeaders(requestedToken)), responseType);
			log.info("This is inside the CommonCommand" + resp.getBody().toString());
			if (resp.getStatusCode() == HttpStatus.OK) {

				return resp.getBody();
			}
		} catch (Exception exception) {
			log.error("Error collecting engagements from common-lookup-services.");
		}
		return commonEngagementDTO;
	}

    @Override
    protected CommonEngagementDTO getFallback() {
        log.info("Fallback to default engagements.");
        CommonEngagementDTO commonEngagementDTO = new CommonEngagementDTO();
        return commonEngagementDTO;
    }

    private HttpHeaders createHeaders(String token) {
        return new HttpHeaders() {
            private static final long serialVersionUID = -6189386900709137018L;
            {
                String authHeader = BEARER + token;
                set(AUTHORIZATION, authHeader);
            }
        };
    }
}
