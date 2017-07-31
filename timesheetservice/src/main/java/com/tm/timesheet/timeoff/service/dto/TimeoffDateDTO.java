package com.tm.timesheet.timeoff.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "TimeoffDate", collectionRelation = "TimeoffDates")
public class TimeoffDateDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -1029456038478343798L;

	private String timeoffDate;
	private Boolean status;
	private Integer hours;
	private String description;

	public String getTimeoffDate() {
		return timeoffDate;
	}

	public void setTimeoffDate(String timeoffDate) {
		this.timeoffDate = timeoffDate;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getHours() {
		return hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}