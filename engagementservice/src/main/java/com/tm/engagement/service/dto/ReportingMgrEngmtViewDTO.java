package com.tm.engagement.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;

public class ReportingMgrEngmtViewDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -6249337517554435018L;

	private UUID engagementId;

	private String engagementName;

	private Long reportManagerId;

	private String activeFlag;

	private String emplActiveFlag;

	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}

	public String getEngagementName() {
		return engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
	}

	public Long getReportManagerId() {
		return reportManagerId;
	}

	public void setReportManagerId(Long reportManagerId) {
		this.reportManagerId = reportManagerId;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getEmplActiveFlag() {
		return emplActiveFlag;
	}

	public void setEmplActiveFlag(String emplActiveFlag) {
		this.emplActiveFlag = emplActiveFlag;
	}

}
