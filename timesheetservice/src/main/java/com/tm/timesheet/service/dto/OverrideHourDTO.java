package com.tm.timesheet.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "OverrideHour", collectionRelation = "OverrideHours")
public class OverrideHourDTO implements Serializable {

	private static final long serialVersionUID = 6349332657839009425L;

	private String startTime;
	private String endTime;
	private String breakHours;
	private String hours;
	private String reason;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getBreakHours() {
		return breakHours;
	}

	public void setBreakHours(String breakHours) {
		this.breakHours = breakHours;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
