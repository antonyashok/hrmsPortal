package com.tm.common.holiday.web.rest;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.text.ParseException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.common.authority.RequiredAuthority;
import com.tm.common.holiday.service.HolidayService;
import com.tm.common.holiday.service.dto.HolidayDTO;
import com.tm.commonapi.security.AuthoritiesConstants;


@RestController
@RequestMapping("/holidays")
public class HolidayResource {

	private HolidayService holidayService;

	@Inject
	public HolidayResource(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	@RequestMapping(value = "/province/{id}", produces = { HAL_JSON_VALUE, APPLICATION_JSON_VALUE })
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<List<HolidayDTO>> getHolidaysByProvinceId(@PathVariable Long id,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) throws ParseException {
		List<HolidayDTO> holidayDTOs = holidayService.getHolidays(id, startDate, endDate);
		return new ResponseEntity<>(holidayDTOs, HttpStatus.OK);
	}
}
