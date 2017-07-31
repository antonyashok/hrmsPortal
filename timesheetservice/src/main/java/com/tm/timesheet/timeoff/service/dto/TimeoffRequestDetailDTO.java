package com.tm.timesheet.timeoff.service.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TimeoffRequestDetailDTO implements Serializable {

	private static final long serialVersionUID = -3481700213887059631L;
	private static final String REQUESTED_DATE_IS_REQUIRED = "Requested Date is Required";
	private UUID timesheetId;
	//@Max(value = 8, message = INVALID_NUMBER)
	//@Min(value = 1, message = INVALID_NUMBER)
	
	//@Max(value = 8, message = INVALID_NUMBER)
	//@Min(value = 1, message = INVALID_NUMBER)
	//@Pattern(regexp = "[0-8][0-8]?(\\.[0-9][0-9]?)?", message = INVALID_HOURS)
	private String requestedHours;
	@NotBlank(message = REQUESTED_DATE_IS_REQUIRED)
	private String requestedDate;
	private String requestedDateStr;
	private Long updatedBy;
	private Date updatedDate = new Timestamp(System.currentTimeMillis());
	private String updatorName;
	private String status;
	private String description;
	private String ptoFlag;
	private Boolean joiningStatus;
	private Boolean weekOffStatus;
	private Long createdBy;
	private Date createdDate = new Timestamp(System.currentTimeMillis());
	private String creatorName;
	private String comments;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

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

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getPtoFlag() {
		if (Objects.isNull(ptoFlag))
			return StringUtils.EMPTY;
		return ptoFlag;
	}

	public void setPtoFlag(String ptoFlag) {
		this.ptoFlag = ptoFlag;
	}

	public Boolean getJoiningStatus() {
		if (Objects.isNull(joiningStatus))
			return false;
		return joiningStatus;
	}

	public void setJoiningStatus(Boolean joiningStatus) {
		this.joiningStatus = joiningStatus;
	}

	public Boolean getWeekOffStatus() {
		return weekOffStatus;
	}

	public void setWeekOffStatus(Boolean weekOffStatus) {
		this.weekOffStatus = weekOffStatus;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
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

	public String getRequestedDateStr() {
		return requestedDateStr;
	}

	public void setRequestedDateStr(String requestedDateStr) {
		this.requestedDateStr = requestedDateStr;
	}
	
	

}