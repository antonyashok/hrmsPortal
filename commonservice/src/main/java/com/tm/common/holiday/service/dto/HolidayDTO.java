package com.tm.common.holiday.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;

@Relation(value = "holiday", collectionRelation = "holidays")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HolidayDTO extends ResourceSupport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1060391980172924093L;
	
	private String holidayStateProvinceId;
	private Long stateProvinceId;
	private String holidayCalendarDetailId;
	private Date holidayDate;
	private String description;
	private String settingName;
	private Long countryId;

	public String getHolidayStateProvinceId() {
		return holidayStateProvinceId;
	}

	public void setHolidayStateProvinceId(String holidayStateProvinceId) {
		this.holidayStateProvinceId = holidayStateProvinceId;
	}

	public Long getStateProvinceId() {
		return stateProvinceId;
	}

	public void setStateProvinceId(Long stateProvinceId) {
		this.stateProvinceId = stateProvinceId;
	}

	public String getHolidayCalendarDetailId() {
		return holidayCalendarDetailId;
	}

	public void setHolidayCalendarDetailId(String holidayCalendarDetailId) {
		this.holidayCalendarDetailId = holidayCalendarDetailId;
	}

	public Date getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

}
