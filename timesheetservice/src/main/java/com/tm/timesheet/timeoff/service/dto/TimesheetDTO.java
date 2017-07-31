package com.tm.timesheet.timeoff.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "Timesheet", collectionRelation = "Timesheets")
public class TimesheetDTO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = -2267206880645715813L;

    private String timesheetId;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String startDate;
    private String endDate;
    private String status;
    private Double totalHours;
    private Double totalUnits;
    private Boolean paidStatus = Boolean.FALSE;
    private Double ptoHours;
    private Double nonBillablePTO;
    private Double workHours;
    private Double leaveHours;
    private Double stHours;
    private Double otHours;
    private Double dtHours;
    private String configGroupId;
    private Double holidayHours;
    private String source;
    private String period;
    private String dateRange;
	public String getTimesheetId() {
		return timesheetId;
	}
	public void setTimesheetId(String timesheetId) {
		this.timesheetId = timesheetId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Double getTotalHours() {
		return totalHours;
	}
	public void setTotalHours(Double totalHours) {
		this.totalHours = totalHours;
	}
	public Double getTotalUnits() {
		return totalUnits;
	}
	public void setTotalUnits(Double totalUnits) {
		this.totalUnits = totalUnits;
	}
	public Boolean getPaidStatus() {
		return paidStatus;
	}
	public void setPaidStatus(Boolean paidStatus) {
		this.paidStatus = paidStatus;
	}
	public Double getPtoHours() {
		return ptoHours;
	}
	public void setPtoHours(Double ptoHours) {
		this.ptoHours = ptoHours;
	}
	public Double getNonBillablePTO() {
		return nonBillablePTO;
	}
	public void setNonBillablePTO(Double nonBillablePTO) {
		this.nonBillablePTO = nonBillablePTO;
	}
	public Double getWorkHours() {
		return workHours;
	}
	public void setWorkHours(Double workHours) {
		this.workHours = workHours;
	}
	public Double getLeaveHours() {
		return leaveHours;
	}
	public void setLeaveHours(Double leaveHours) {
		this.leaveHours = leaveHours;
	}
	public Double getStHours() {
		return stHours;
	}
	public void setStHours(Double stHours) {
		this.stHours = stHours;
	}
	public Double getOtHours() {
		return otHours;
	}
	public void setOtHours(Double otHours) {
		this.otHours = otHours;
	}
	public Double getDtHours() {
		return dtHours;
	}
	public void setDtHours(Double dtHours) {
		this.dtHours = dtHours;
	}
	public String getConfigGroupId() {
		return configGroupId;
	}
	public void setConfigGroupId(String configGroupId) {
		this.configGroupId = configGroupId;
	}
	public Double getHolidayHours() {
		return holidayHours;
	}
	public void setHolidayHours(Double holidayHours) {
		this.holidayHours = holidayHours;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getDateRange() {
		return dateRange;
	}
	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

    

}
