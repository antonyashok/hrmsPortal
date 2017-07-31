package com.tm.common.engagement.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "CntrHolidays", collectionRelation = "CntrHolidays")
public class ContractorHolidayDTO extends ResourceSupport implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7972522617425269701L;

	private String engagementHolidayId;

	private String engagementId;

	private Date holidayDate;

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
