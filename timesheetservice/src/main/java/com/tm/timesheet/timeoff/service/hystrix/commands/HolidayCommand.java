package com.tm.timesheet.timeoff.service.hystrix.commands;

import java.util.Date;
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

import com.tm.timesheet.timeoff.exception.TimeoffException;
import com.tm.timesheet.timeoff.service.dto.HolidayDTO;
import com.tm.timesheet.timeoff.web.rest.util.DateUtil;

public class HolidayCommand {

	private final Logger log = LoggerFactory.getLogger(HolidayCommand.class);

	private static final String BEARER = "Bearer ";
	private static final String COMTRACK_HOLIDAY_URI = "/common/holidays/province";
	private String comtrackServiceUrl;
	private RestTemplate restTemplate;
	private String requestedToken;
	private String startdate;
	private String enddate;
	private Long provinceid;

	public HolidayCommand(RestTemplate restTemplate, String comtrackServiceUrl, String token, String startDate,
			String endDate, Long provinceId) {
		this.restTemplate = restTemplate;
		this.comtrackServiceUrl = comtrackServiceUrl.startsWith("http://") ? comtrackServiceUrl
				: "http://" + comtrackServiceUrl;
		this.requestedToken = token;
		this.startdate = startDate;
		this.enddate = endDate;
		this.provinceid = provinceId;
	}

	public List<HolidayDTO> getHolidays() {
		log.debug("Getting in hystric run()");
		ParameterizedTypeReference<List<HolidayDTO>> responseType = new ParameterizedTypeReference<List<HolidayDTO>>() {
		};
		try{
			log.info(""+
					comtrackServiceUrl + COMTRACK_HOLIDAY_URI + "/" + provinceid + "?startDate=" + startdate
					+ "&endDate=" + enddate);
		ResponseEntity<List<HolidayDTO>> resp = restTemplate
				.exchange(
						comtrackServiceUrl + COMTRACK_HOLIDAY_URI + "/" + provinceid + "?startDate=" + startdate
								+ "&endDate=" + enddate,
						HttpMethod.GET, new HttpEntity<>(createHeaders(requestedToken)), responseType);
		
		if (resp.getStatusCode() == HttpStatus.OK) {
			try {							
				List<HolidayDTO> holidayList = resp.getBody();
				return holidayList;
			} catch (Exception exception) {
				log.error("Error collecting holidays from holiday-services.");
			}
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;

	}
	

	public static Boolean compareDates(String weekStartDate, String weekEndDate, Date holidayDate) {
		boolean dateTrue = false;
		java.util.Date startDate = null;
		java.util.Date endDate = null;
		if (null != weekStartDate && null != weekEndDate) {
			try {
				startDate = DateUtil.checkconvertStringToISODate(weekStartDate);
				endDate = DateUtil.checkconvertStringToISODate(weekEndDate);
			} catch (Exception e) {
				throw new TimeoffException("Invalid date format", e);
			}
		}
		if (null != holidayDate && ((holidayDate.after(startDate) && holidayDate.before(endDate))
				|| (holidayDate.equals(startDate) || holidayDate.equals(endDate)))) {
			return !dateTrue;
		}
		return dateTrue;
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
