package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "engmt_hldy")
public class EngagementHolidays extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 7453310039042610019L;

	@Id
	@Column(name = "engmt_hldy_id")
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID engagementHolidayId;

	@Column(name = "engmt_id")
	private String engagementId;

	@Column(name = "hldy_date")
	private Date holidayDate;

	@Column(name = "hldy_desc")
	private String holidayDescription;

	public UUID getEngagementHolidayId() {
		return engagementHolidayId;
	}

	public void setEngagementHolidayId(UUID engagementHolidayId) {
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
