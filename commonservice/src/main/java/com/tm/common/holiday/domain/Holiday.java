package com.tm.common.holiday.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * view: hldy_dtl_view
 */
@Entity
@Table(name = "hldy_dtl_view")
public class Holiday implements Serializable {

	private static final long serialVersionUID = -3243751767314137005L;

	@Column(name = "hldy_st_prv_id")
	private String holidayStateProvinceId;

	@Column(name = "st_prv_id")
	private Long stateProvinceId;

	@Id
	@Column(name = "hldy_cldr_dtl_id")
	private String holidayCalendarDetailId;

	@Temporal(TemporalType.DATE)
	@Column(name = "hldy_dt")
	private Date holidayDate;

	@Column(name = "hldy_nm")
	private String description;

	@Column(name = "setting_nm")
	private String settingName;

	@Column(name = "cntry_id")
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
