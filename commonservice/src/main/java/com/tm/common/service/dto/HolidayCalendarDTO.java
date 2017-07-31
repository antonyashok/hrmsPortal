package com.tm.common.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.common.service.dto.validator.HolidayCalendar;

@Relation(value = "holidayCalendar", collectionRelation = "holidayCalendars")
@JsonIgnoreProperties({ "createdBy", "lastModifiedBy" })
@HolidayCalendar
public class HolidayCalendarDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 7073655627109708812L;

	private UUID holidayCalendarId;
	@NotEmpty(message = "{holiday.settings.name.empty}")
	private String settingName;
	@NotNull(message = "{holiday.settings.county.id}")
	private Long countryId;
	private Long createdBy;
	private Long lastModifiedBy;
	@Valid
	@NotEmpty(message = "{holiday.settings.calendar.details}")
	private List<HolidayCalendarDetailDTO> holidayCalendarDetailDTO;
	@Valid
	@NotEmpty(message = "{holiday.settings.state.province}")
	private List<HolidayStateProvinceDTO> holidayStateProvinceDTO;

	public UUID getHolidayCalendarId() {
		return holidayCalendarId;
	}

	public void setHolidayCalendarId(UUID holidayCalendarId) {
		this.holidayCalendarId = holidayCalendarId;
	}

	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public List<HolidayCalendarDetailDTO> getHolidayCalendarDetailDTO() {
		return holidayCalendarDetailDTO;
	}

	public void setHolidayCalendarDetailDTO(
			List<HolidayCalendarDetailDTO> holidayCalendarDetailDTO) {
		this.holidayCalendarDetailDTO = holidayCalendarDetailDTO;
	}

	public List<HolidayStateProvinceDTO> getHolidayStateProvinceDTO() {
		return holidayStateProvinceDTO;
	}

	public void setHolidayStateProvinceDTO(
			List<HolidayStateProvinceDTO> holidayStateProvinceDTO) {
		this.holidayStateProvinceDTO = holidayStateProvinceDTO;
	}

}