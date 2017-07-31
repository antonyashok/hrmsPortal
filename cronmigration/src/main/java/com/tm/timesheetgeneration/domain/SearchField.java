package com.tm.timesheetgeneration.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchField implements Serializable {

	private static final long serialVersionUID = 1L;

	private String periodDateTime;
	private String lastUpdatedDateTime;
	private String submittedDateTime;
	private String approvedDateTime;
	
	
	public String getPeriodDateTime() {
		return periodDateTime;
	}
	public void setPeriodDateTime(String periodDateTime) {
		this.periodDateTime = periodDateTime;
	}
	public String getLastUpdatedDateTime() {
		return lastUpdatedDateTime;
	}
	public void setLastUpdatedDateTime(String lastUpdatedDateTime) {
		this.lastUpdatedDateTime = lastUpdatedDateTime;
	}
	public String getSubmittedDateTime() {
		return submittedDateTime;
	}
	public void setSubmittedDateTime(String submittedDateTime) {
		this.submittedDateTime = submittedDateTime;
	}
	public String getApprovedDateTime() {
		return approvedDateTime;
	}
	public void setApprovedDateTime(String approvedDateTime) {
		this.approvedDateTime = approvedDateTime;
	}

}
