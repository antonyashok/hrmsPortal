/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.cron.dto.TimesheetDTO.java
 * Author        : Annamalai L
 * Date Created  : Apr 13th, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheetgeneration.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.timesheetgeneration.domain.TimesheetDetails;
import com.tm.timesheetgeneration.domain.enums.PaidStatusEnum;
import com.tm.timesheetgeneration.domain.enums.StatusEnum;
import com.tm.timesheetgeneration.dto.RecruiterTimeDTO.TimesheetTypeEnum;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimesheetDTO {
	 
    @Type(type = "uuid-char")
    private UUID configurationId;

    private UUID timesheetId;

    private Long employeeId;

    private Long officeId;

    private String rcrtrCntctInfo;

    private Date startDate;

    private Date endDate;

    private Date submissionDate;

    private String rejectedReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    private Double totalHours;

    private Integer totalUnits;

    private Double pto;

    private Double work;

    private String comment;

    private Double leaveHours;

    private Double stHours;

    private Double otHours;

    private Double dtHours;

    @Enumerated(EnumType.STRING)
    private PaidStatusEnum paidStatusFlag;

    private List<TimesheetDetails> timesheetDetails = new ArrayList<>();

    private String employeeName;

    @Enumerated(EnumType.STRING)
    private TimesheetTypeEnum timesheetTypeEnum;

	public UUID getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(UUID configurationId) {
		this.configurationId = configurationId;
	}

	public UUID getTimesheetId() {
		return timesheetId;
	}

	public void setTimesheetId(UUID timesheetId) {
		
		this.timesheetId = timesheetId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public String getRcrtrCntctInfo() {
		return rcrtrCntctInfo;
	}

	public void setRcrtrCntctInfo(String rcrtrCntctInfo) {
		this.rcrtrCntctInfo = rcrtrCntctInfo;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getRejectedReason() {
		return rejectedReason;
	}

	public void setRejectedReason(String rejectedReason) {
		this.rejectedReason = rejectedReason;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public Double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(Double totalHours) {
		this.totalHours = totalHours;
	}

	public Integer getTotalUnits() {
		return totalUnits;
	}

	public void setTotalUnits(Integer totalUnits) {
		this.totalUnits = totalUnits;
	}

	public Double getPto() {
		return pto;
	}

	public void setPto(Double pto) {
		this.pto = pto;
	}

	public Double getWork() {
		return work;
	}

	public void setWork(Double work) {
		this.work = work;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public PaidStatusEnum getPaidStatusFlag() {
		return paidStatusFlag;
	}

	public void setPaidStatusFlag(PaidStatusEnum paidStatusFlag) {
		this.paidStatusFlag = paidStatusFlag;
	}

	public List<TimesheetDetails> getTimesheetDetails() {
		return timesheetDetails;
	}

	public void setTimesheetDetails(List<TimesheetDetails> timesheetDetails) {
		this.timesheetDetails = timesheetDetails;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public TimesheetTypeEnum getTimesheetTypeEnum() {
		return timesheetTypeEnum;
	}

	public void setTimesheetTypeEnum(TimesheetTypeEnum timesheetTypeEnum) {
		this.timesheetTypeEnum = timesheetTypeEnum;
	}    
}