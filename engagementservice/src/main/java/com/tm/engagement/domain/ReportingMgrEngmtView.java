package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "reporting_mgr_engagement_view")
public class ReportingMgrEngmtView implements Serializable {

	private static final long serialVersionUID = 7625169642524716239L;

	@Id
	@Column(name = "engmt_id")
	@Type(type = "uuid-char")
	private UUID engagementId;

	@Column(name = "engmt_nm")
	private String engagementName;

	@Column(name = "rep_mgr_id")
	private Long reportManagerId;

	@Column(name = "actv_flg")
	private String activeFlag;

	@Column(name = "empl_actv_flg")
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