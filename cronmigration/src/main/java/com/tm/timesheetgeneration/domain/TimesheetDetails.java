package com.tm.timesheetgeneration.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tm.invoice.domain.AuditFields;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "timesheetDetail")
public class TimesheetDetails implements Serializable {

	private static final long serialVersionUID = 5695696734026079368L;

	public static final Boolean TIMESHEET_FALSE_STATUS = false;

	@Id
	private UUID id;
	private UUID timesheetId;
	private UUID employeeEngagementTaskMapId;
	private String taskName;
	private Date timesheetDate;
	private String dayOfWeek;
	private Double hours;
	private Long units;
	private Boolean activeTaskFlag = TIMESHEET_FALSE_STATUS;
	private Boolean startFlag = TIMESHEET_FALSE_STATUS;
	private Boolean overrideFlag = TIMESHEET_FALSE_STATUS;
	private List<TimeDetail> timeDetail;
	private AuditFields created;
	private AuditFields updated;
	private String comments;

	public UUID getTimesheetId() {
		return timesheetId;
	}

	public void setTimesheetId(UUID timesheetId) {
		this.timesheetId = timesheetId;
	}

	public UUID getEmployeeEngagementTaskMapId() {
		return employeeEngagementTaskMapId;
	}

	public void setEmployeeEngagementTaskMapId(UUID employeeEngagementTaskMapId) {
		this.employeeEngagementTaskMapId = employeeEngagementTaskMapId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Date getTimesheetDate() {
		return timesheetDate;
	}

	public void setTimesheetDate(Date timesheetDate) {
		this.timesheetDate = timesheetDate;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public AuditFields getCreated() {
		return created;
	}

	public void setCreated(AuditFields created) {
		this.created = created;
	}

	public AuditFields getUpdated() {
		return updated;
	}

	public void setUpdated(AuditFields updated) {
		this.updated = updated;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Double getHours() {
		return hours;
	}

	public void setHours(Double hours) {
		this.hours = hours;
	}

	public Long getUnits() {
		return units;
	}

	public void setUnits(Long units) {
		this.units = units;
	}

	public List<TimeDetail> getTimeDetail() {
		return timeDetail;
	}

	public void setTimeDetail(List<TimeDetail> timeDetail) {
		this.timeDetail = timeDetail;
	}

	public Boolean getStartFlag() {
		return startFlag;
	}

	public void setStartFlag(Boolean startFlag) {
		this.startFlag = startFlag;
	}

	public Boolean getOverrideFlag() {
		return overrideFlag;
	}

	public void setOverrideFlag(Boolean overrideFlag) {
		this.overrideFlag = overrideFlag;
	}

	public Boolean getActiveTaskFlag() {
		return activeTaskFlag;
	}

	public void setActiveTaskFlag(Boolean activeTaskFlag) {
		this.activeTaskFlag = activeTaskFlag;
	}

}