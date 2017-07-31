package com.tm.common.service.dto;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Relation(value = "holidayStateProvince", collectionRelation = "holidayStateProvinces")
@JsonIgnoreProperties({"holidayCalendarId"})
public class HolidayStateProvinceDTO extends ResourceSupport implements Serializable {
	
	private static final long serialVersionUID = 1564501324683137003L;

	private UUID holidayStateProvinceId;
    private UUID holidayCalendarId;
    @NotNull(message = "{holiday.settings.state.province.id}")
    private Long stateProvinceId;

	public UUID getHolidayStateProvinceId() {
		return holidayStateProvinceId;
	}

	public void setHolidayStateProvinceId(UUID holidayStateProvinceId) {
		this.holidayStateProvinceId = holidayStateProvinceId;
	}

	public UUID getHolidayCalendarId() {
		return holidayCalendarId;
	}

	public void setHolidayCalendarId(UUID holidayCalendarId) {
		this.holidayCalendarId = holidayCalendarId;
	}

	public Long getStateProvinceId() {
		return stateProvinceId;
	}

	public void setStateProvinceId(Long stateProvinceId) {
		this.stateProvinceId = stateProvinceId;
	}
}