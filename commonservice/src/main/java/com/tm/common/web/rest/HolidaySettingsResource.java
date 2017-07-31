package com.tm.common.web.rest;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.common.authority.RequiredAuthority;
import com.tm.common.service.HolidayCalenderService;
import com.tm.common.service.dto.HolidayCalendarDTO;
import com.tm.common.service.dto.HolidaySettingsViewDTO;
import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.commonapi.web.rest.util.HeaderUtil;

@RestController
public class HolidaySettingsResource {

	private HolidayCalenderService holidayCalenderService;

	@Inject
	public HolidaySettingsResource(HolidayCalenderService holidayCalenderService) {
		this.holidayCalenderService = holidayCalenderService;
	}

	@RequestMapping(value = "/holidaysettings", method = RequestMethod.POST)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<HolidayCalendarDTO> saveHolidaySettings(
			@Valid @RequestBody HolidayCalendarDTO holidayCalendarDTO) {
		return new ResponseEntity<>(
				holidayCalenderService.saveHolidayCalender(holidayCalendarDTO),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/holidaysettings", method = RequestMethod.PUT)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<HolidayCalendarDTO> updateHolidaySettings(
			@Valid @RequestBody HolidayCalendarDTO holidayCalendarDTO) {
		return new ResponseEntity<>(
				holidayCalenderService.saveHolidayCalender(holidayCalendarDTO),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/holidaysettings/{holidayCalendarId}")
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<HolidayCalendarDTO> getHolidayCalendar(
			@PathVariable UUID holidayCalendarId) {
		return new ResponseEntity<>(
				holidayCalenderService.getHolidayCalendarDTO(holidayCalendarId),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/holidaysettings/holidaydetails", method = RequestMethod.DELETE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<String> deleteHolidayCalendar(
			@RequestParam("holidayDetailIds") List<UUID> holidayDetailIds) {
		holidayCalenderService.deleteHolidayCalenderDetail(holidayDetailIds);
		return ResponseEntity
				.ok()
				.headers(
						HeaderUtil.createEntityDeletionAlert(
								"holidaysettings",
								holidayDetailIds.stream()
										.map(e -> e.toString())
										.reduce("", String::concat)))
				.body("OK");
	}

	@RequestMapping(value = "/holidaysettings", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
	public PagedResources<Resource<HolidaySettingsViewDTO>> getAllConfigurationGroup(
			Pageable pageable,
			PagedResourcesAssembler<HolidaySettingsViewDTO> pagedAssembler,
			String fields) {
		Page<HolidaySettingsViewDTO> result = holidayCalenderService
				.getAllHolidaySettings(pageable);
		return pagedAssembler.toResource(result);
	}
}
