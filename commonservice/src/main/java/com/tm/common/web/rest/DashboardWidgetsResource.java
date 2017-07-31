package com.tm.common.web.rest;

import java.text.ParseException;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tm.common.authority.RequiredAuthority;
import com.tm.common.service.DashboardWidgetsService;
import com.tm.commonapi.security.AuthoritiesConstants;

@RestController
public class DashboardWidgetsResource {

	DashboardWidgetsService dashboardService; 
	
	@Inject
	public DashboardWidgetsResource(DashboardWidgetsService dashboardService) {
		this.dashboardService = dashboardService;
	}
	
	@RequestMapping(value = "/getmyactivetasks", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.ALL})
	public ResponseEntity<Map<String, Object>> getCountByUserIdAndStatus() throws ParseException {
		return new ResponseEntity<>(dashboardService.getMyActiveTasks(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getbirthdaydetails", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.ALL})
	public ResponseEntity<Map<String, Object>> getBirthdayDetails() throws ParseException {
		return new ResponseEntity<>(dashboardService.getBirthdayDetails(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getnewsletter", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.ALL})
	public ResponseEntity<Map<String, Object>> getNewsLetter() throws ParseException {
		return new ResponseEntity<>(dashboardService.getNewsLetter(), HttpStatus.OK);
	}

	@RequestMapping(value = "/getaccolades", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.ALL})
	public ResponseEntity<Map<String, Object>> getAccolades() throws ParseException {
		return new ResponseEntity<>(dashboardService.getAccolades(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getannouncement", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.ALL})
	public ResponseEntity<Map<String, Object>> getAnnouncement() throws ParseException {
		return new ResponseEntity<>(dashboardService.getAnnouncement(), HttpStatus.OK);
	}

}
