package com.tm.common.service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MyActiveTaskDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String taskName;
	
	private Integer approvalCount;
	
	private Integer escalateCount;
	
	private String timesheetSubmissionDate;

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getApprovalCount() {
		return approvalCount;
	}

	public void setApprovalCount(Integer approvalCount) {
		this.approvalCount = approvalCount;
	}

	public Integer getEscalateCount() {
		return escalateCount;
	}

	public void setEscalateCount(Integer escalateCount) {
		this.escalateCount = escalateCount;
	}

	public String getTimesheetSubmissionDate() {
		return timesheetSubmissionDate;
	}

	public void setTimesheetSubmissionDate(String timesheetSubmissionDate) {
		this.timesheetSubmissionDate = timesheetSubmissionDate;
	}
}
