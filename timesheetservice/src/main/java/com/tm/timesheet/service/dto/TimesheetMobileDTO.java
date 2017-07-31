package com.tm.timesheet.service.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "Timesheet", collectionRelation = "Timesheets")
public class TimesheetMobileDTO implements Serializable {

	private static final long serialVersionUID = 5751577696430402760L;

	private String totalHours;
	private List<TimesheetDetailsDTO> timesheetDetails;

	public String getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(String totalHours) {
		this.totalHours = totalHours;
	}

	public List<TimesheetDetailsDTO> getTimesheetDetails() {
		return timesheetDetails;
	}

	public void setTimesheetDetails(List<TimesheetDetailsDTO> timesheetDetails) {
		this.timesheetDetails = timesheetDetails;
	}

}
