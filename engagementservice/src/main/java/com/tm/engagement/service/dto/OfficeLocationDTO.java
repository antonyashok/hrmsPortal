package com.tm.engagement.service.dto;

import java.io.Serializable;

public class OfficeLocationDTO implements Serializable {

	private static final long serialVersionUID = 2756667699778872521L;

	private Long officeId;
	private String officeName;
	private String activeFlag;
	private String isConfigured = "N";

	public OfficeLocationDTO() {

	}

	public OfficeLocationDTO(Long officeId, String officeName, String isConfigured) {
		this(officeId, officeName, isConfigured, null);
	}

	public OfficeLocationDTO(Long officeId, String officeName, String isConfigured, String activeFlag) {
		this.officeId = officeId;
		this.officeName = officeName;
		this.isConfigured = isConfigured;
		this.activeFlag = activeFlag;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getIsConfigured() {
		return isConfigured;
	}

	public void setIsConfigured(String isConfigured) {
		this.isConfigured = isConfigured;
	}

}
