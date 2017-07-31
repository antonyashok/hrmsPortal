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

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.tm.timesheet.configuration.service.hystrix.commands.OfficeLocationCommand;
import com.tm.timesheet.service.dto.HolidayDTO;

public class HolidayViewCommand extends HystrixCommand<List<HolidayDTO>> {

    private final Logger log = LoggerFactory.getLogger(HolidayViewCommand.class);

    public static final String BEARER = "Bearer ";

    public static final String AUTHORIZATION = "Authorization";

    private static final String COMTRACK_HOLIDAY_URI = "/common/holidays/province/";

    private String comtrackServiceUrl;

    private Long provinceId;

    private String startDate;

    private String endDate;

    private RestTemplate restTemplate;

    // Propagate request header to get this token
    private String requestedToken;

    public HolidayViewCommand(RestTemplate restTemplate, String comtrackServiceUrl, String token,
            Long provinceId, String startDate, String endDate) {
		super(HystrixCommandGroupKey.Factory
				.asKey(OfficeLocationCommand.COMMON_GROUP_KEY));
		this.restTemplate = restTemplate;
        this.comtrackServiceUrl = comtrackServiceUrl.startsWith("http://") ? comtrackServiceUrl
                : "http://" + comtrackServiceUrl;
        this.requestedToken = token;
        this.provinceId = provinceId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    protected List<HolidayDTO> run() throws Exception {
        log.debug("Getting in hystric run()");
        ParameterizedTypeReference<List<HolidayDTO>> responseType =
                new ParameterizedTypeReference<List<HolidayDTO>>() {};
       
        try {
        	 ResponseEntity<List<HolidayDTO>> resp = restTemplate.exchange(
                     comtrackServiceUrl + COMTRACK_HOLIDAY_URI + provinceId + "?startDate=" + startDate
                             + "&endDate=" + endDate,
                     HttpMethod.GET, new HttpEntity<>(createHeaders(requestedToken)), responseType);
             log.info("This is inside the HolidayCommand:: " + resp.getBody().toString());
	        if (resp.getStatusCode() == HttpStatus.OK) {
	            try {
	                return resp.getBody();
	            } catch (Exception exception) {
	                log.error("Error collecting holidays from holiday-services.");
	            }
	        }
	    } catch (Exception exception) {
	        log.info("Error from holiday-services."+exception);
	    }
        return Collections.<HolidayDTO>emptyList();

    }

    @Override
    protected List<HolidayDTO> getFallback() {
        log.info("Fallback to default holiday.");
        return Collections.<HolidayDTO>emptyList();
    }

    private HttpHeaders createHeaders(String token) {
        return new HttpHeaders() {
            /**
             * 
             */
            private static final long serialVersionUID = -6189386900709137018L;

            {
                String authHeader = BEARER + token;
                set(AUTHORIZATION, authHeader);
            }
        };
    }

}
