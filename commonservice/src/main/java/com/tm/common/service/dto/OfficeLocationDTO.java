package com.tm.common.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "officeLocations", collectionRelation = "officeLocations")
public class OfficeLocationDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -5171148441505304857L;

	private Long officeId;

	private String officeName;

	private String activeFlag;

	private String isConfigured = "N";
	
	private String lastModifiedDate;
	
	private String createdDate;

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

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

}
