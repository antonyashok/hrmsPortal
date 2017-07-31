/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.common.service.dto.HolidaySettingsViewDTO.java
 * Author        : Annamalai 
 * Date Created  : Mar 14, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.common.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.core.Relation;

@Relation(value = "holidaySettingsView", collectionRelation = "holidaySettingsViews")
public class HolidaySettingsViewDTO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3214514832575615659L;
	private UUID holidayCalendarId;
	private String settingName;
    private Long countryId;
    private String countryName;
    //private Long stateProvinceId;
    private String stateProvinceId;
    private String stateProvinceName;
    private Long holidayCount;
    private String lastModifiedDate;

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

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
