/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.common.domin.HolidaySettingsView.java
 * Author        : Annamalai 
 * Date Created  : Mar 14, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.common.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "holiday_settings_view")
public class HolidaySettingsView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3215087819535251960L;

	@Id
	@Column(name = "hldy_cldr_id")
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID holidayCalendarId;

	@Column(name = "setting_nm")
	private String settingName;

	@Column(name = "cntry_id")
	private Long countryId;

	@Column(name = "cntry_nm")
	private String countryName;

//	@Column(name = "st_prv_id")
//	private long stateProvinceId;
	
	@Column(name = "st_prv_id")
	private String stateProvinceId;

	@Column(name = "state")
	private String stateProvinceName;

	@Column(name = "hldy_count")
	private Long holidayCount;

	@Column(name = "last_updt_dt")
	private ZonedDateTime lastModifiedDate;

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

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/*public long getStateProvinceId() {
		return stateProvinceId;
	}

	public void setStateProvinceId(long stateProvinceId) {
		this.stateProvinceId = stateProvinceId;
	}*/
	
	public String getStateProvinceId() {
		return stateProvinceId;
	}

	public void setStateProvinceId(String stateProvinceId) {
		this.stateProvinceId = stateProvinceId;
	}
	

	public String getStateProvinceName() {
		return stateProvinceName;
	}

	public void setStateProvinceName(String stateProvinceName) {
		this.stateProvinceName = stateProvinceName;
	}

	public Long getHolidayCount() {
		return holidayCount;
	}

	public void setHolidayCount(Long holidayCount) {
		this.holidayCount = holidayCount;
	}

	public ZonedDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
