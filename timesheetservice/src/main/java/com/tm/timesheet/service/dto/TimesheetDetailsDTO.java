package com.tm.timesheet.service.dto;

import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "TimesheetDetail", collectionRelation = "TimesheetDetails")
public class TimesheetDetailsDTO extends ResourceSupport {
	
	public enum DayOfWeek {
		Sun, Monday, Tuesday, Wednesday, Thuresday, Friday, Saturday
	}

	public enum HolidayFlag {
		Y, N
	}

	private UUID timesheetDetailsId;
	private UUID timesheetId;
	private String employeeEngagementTaskMapId;
	private String taskName;
	private String timesheetDate;
	private String dayOfWeek;
	private String hours;
	private Long units;
	private String comments;
	private List<TimeDetailDTO> timeDetail;
	private OverrideHourDTO overrideHour;
	private Boolean joiningStatus = true;
	private Boolean holidayStatus = false;
	private String dateFormat;
	private Boolean weekOffStatus = false;
	private Boolean activeTaskFlag = true;
    private Boolean startFlag = true;
    private Boolean overrideFlag = false;
	private Boolean currentDateFlag = false;
	private String originalHours;
	private String projectName; //Mobile view
	
	public UUID getTimesheetDetailsId() {
		return timesheetDetailsId;
	}
	public void setTimesheetDetailsId(UUID timesheetDetailsId) {
		this.timesheetDetailsId = timesheetDetailsId;
	}
	public UUID getTimesheetId() {
		return timesheetId;
	}
	public void setTimesheetId(UUID timesheetId) {
		this.timesheetId = timesheetId;
	}
	public String getEmployeeEngagementTaskMapId() {
		return employeeEngagementTaskMapId;
	}
	public void setEmployeeEngagementTaskMapId(String employeeEngagementTaskMapId) {
		this.employeeEngagementTaskMapId = employeeEngagementTaskMapId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTimesheetDate() {
		return timesheetDate;
	}
	public void setTimesheetDate(String timesheetDate) {
		this.timesheetDate = timesheetDate;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public Long getUnits() {
		return units;
	}
	public void setUnits(Long units) {
		this.units = units;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public List<TimeDetailDTO> getTimeDetail() {
		return timeDetail;
	}
	public void setTimeDetail(List<TimeDetailDTO> timeDetail) {
		this.timeDetail = timeDetail;
	}
	public OverrideHourDTO getOverrideHour() {
		return overrideHour;
	}
	public void setOverrideHour(OverrideHourDTO overrideHour) {
		this.overrideHour = overrideHour;
	}
	public Boolean getJoiningStatus() {
		return joiningStatus;
	}
	public void setJoiningStatus(Boolean joiningStatus) {
		this.joiningStatus = joiningStatus;
	}
	public Boolean getHolidayStatus() {
		return holidayStatus;
	}
	public void setHolidayStatus(Boolean holidayStatus) {
		this.holidayStatus = holidayStatus;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public Boolean getWeekOffStatus() {
		return weekOffStatus;
	}
	public void setWeekOffStatus(Boolean weekOffStatus) {
		this.weekOffStatus = weekOffStatus;
	}
	public Boolean getOverrideFlag() {
		return overrideFlag;
	}
	public void setOverrideFlag(Boolean overrideFlag) {
		this.overrideFlag = overrideFlag;
	}
	public Boolean getCurrentDateFlag() {
		return currentDateFlag;
	}
	public void setCurrentDateFlag(Boolean currentDateFlag) {
		this.currentDateFlag = currentDateFlag;
	}
	public Boolean getActiveTaskFlag() {
		return activeTaskFlag;
	}
	public void setActiveTaskFlag(Boolean activeTaskFlag) {
		this.activeTaskFlag = activeTaskFlag;
	}
	public Boolean getStartFlag() {
		return startFlag;
	}
	public void setStartFlag(Boolean startFlag) {
		this.startFlag = startFlag;
	}
	public String getOriginalHours() {
		return originalHours;
	}
	public void setOriginalHours(String originalHours) {
		this.originalHours = originalHours;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
}