package com.tm.common.engagement.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "engagement_hldy")
public class CntrHolidays implements Serializable {

	private static final long serialVersionUID = 7453310039042610019L;

	@Id
	@Column(name = "engmt_hldy_id")
	private String engagementHolidayId;

	@Column(name = "engmt_id")
	private String engagementId;

	@Temporal(TemporalType.DATE)
	@Column(name = "hldy_date")
	private Date holidayDate;

	@Column(name = "hldy_desc")
	private String holidayDescription;

	public String getEngagementHolidayId() {
		return engagementHolidayId;
	}

	public void setEngagementHolidayId(String engagementHolidayId) {
		this.engagementHolidayId = engagementHolidayId;
	}

	public String getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(String engagementId) {
		this.engagementId = engagementId;
	}

	public Date getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	public String getHolidayDescription() {
		return holidayDescription;
	}

	public void setHolidayDescription(String holidayDescription) {
		this.holidayDescription = holidayDescription;
	}
}
