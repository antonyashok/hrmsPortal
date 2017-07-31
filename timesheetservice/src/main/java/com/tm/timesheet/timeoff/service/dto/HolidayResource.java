
package com.tm.timesheet.timeoff.service.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;


//@Relation(value = "CntrHolidays", collectionRelation = "CntrHolidays")
public class HolidayResource extends ResourceSupport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7972522617425269701L;

	//private String engagementHolidayId;
	private UUID engagementHolidayId;

	private String engagementId;

	private Date holidayDate;

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
