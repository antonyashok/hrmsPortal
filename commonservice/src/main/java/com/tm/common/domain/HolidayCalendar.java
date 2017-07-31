package com.tm.common.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "hldy_cldr")
public class HolidayCalendar implements Serializable {

	private static final long serialVersionUID = -4617352042614748830L;

	@Id
	@Column(name = "hldy_cldr_id", nullable = false)
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID holidayCalendarId;

	@Column(name = "setting_nm", nullable = false, length = 100)
	private String settingName;

	@Column(name = "cntry_id", nullable = false)
	private Long countryId;

	@OneToMany(mappedBy = "holidayCalendar", cascade = CascadeType.ALL)
	private List<HolidayCalendarDetail> holidayCalendarDetail;

	@OneToMany(mappedBy = "holidayCalendar", cascade = CascadeType.ALL)
	private List<HolidayStateProvince> holidayStateProvince;

	@CreatedBy
	@Column(name = "created_by", nullable = false, length = 50, updatable = false)
	@JsonIgnore
	private Long createdBy;

	@CreatedDate
	@Column(name = "create_dt", nullable = false)
	@JsonIgnore
	private ZonedDateTime createdDate = ZonedDateTime.now();

	@LastModifiedBy
	@Column(name = "updated_by", length = 50)
	@JsonIgnore
	private Long lastModifiedBy;

	@JsonIgnore
	@Column(name = "last_updt_dt")
	private ZonedDateTime lastModifiedDate = ZonedDateTime.now();

	public List<HolidayCalendarDetail> getHolidayCalendarDetail() {
		return holidayCalendarDetail;
	}

	public void setHolidayCalendarDetail(
			List<HolidayCalendarDetail> holidayCalendarDetail) {
		this.holidayCalendarDetail = holidayCalendarDetail;
	}

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

	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Long getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public ZonedDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public List<HolidayStateProvince> getHolidayStateProvince() {
		return holidayStateProvince;
	}

	public void setHolidayStateProvince(
			List<HolidayStateProvince> holidayStateProvince) {
		this.holidayStateProvince = holidayStateProvince;
	}
}