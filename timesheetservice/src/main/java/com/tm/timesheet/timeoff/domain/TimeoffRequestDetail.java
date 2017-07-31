package com.tm.timesheet.timeoff.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ptoRequestDetail")
public class TimeoffRequestDetail {

	private UUID timesheetId;
	private String requestedHours;
	private Date requestedDate;
	private Long updatedBy;
	@Column(name = "updatedDate", insertable = false)
	private Date updatedDate;
	private String updatorName;
	private String status;
	private String createdBy;
	private Date createdDate;
	private String creatorName;
	private String comments;
	

	public UUID getTimesheetId() {
		return timesheetId;
	}

	public void setTimesheetId(UUID timesheetId) {
		this.timesheetId = timesheetId;
	}

	public String getRequestedHours() {
		return requestedHours;
	}

	public void setRequestedHours(String requestedHours) {
		this.requestedHours = requestedHours;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatorName() {
		return updatorName;
	}

	public void setUpdatorName(String updatorName) {
		this.updatorName = updatorName;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	
}