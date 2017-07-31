package com.tm.common.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cnctr_engmt_dtl_view")
public class ContractorEngagementView implements Serializable {

	private static final long serialVersionUID = -9175936700632780130L;

	@Id
	@Column(name = "empl_id")
	private Long employeeId;
	@Column(name = "empl_engmt_id")
	private UUID employeeEngagementId;
	@Column(name = "engmt_id")
	private UUID engagementId;
	@Column(name = "empl_engmt_task_map_id")
	private UUID employeeEngagementTaskMapId;
	@Column(name = "start_day")
	private String startDay;
	@Column(name = "end_day")
	private String endDay;
	@Column(name = "start_time")
	private Date startTime;
	@Column(name = "end_time")
	private Date endTime;
	@Column(name = "engmt_strt_dt")
	private Date engagementStartDate;
	@Column(name = "engmt_end_dt")
	private Date engagementEndDate;
	@Column(name = "ofc_id")
	private Long officeId;
	@Column(name = "effctv_strt_dt")
	private Date effectiveStartDate;
	@Column(name = "effctv_end_dt")
	private Date effectiveEndDate;
	@Column(name = "bill_type_lkp_id")
	private UUID billTypeLookupId;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public UUID getEmployeeEngagementId() {
		return employeeEngagementId;
	}

	public void setEmployeeEngagementId(UUID employeeEngagementId) {
		this.employeeEngagementId = employeeEngagementId;
	}

	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}

	public UUID getEmployeeEngagementTaskMapId() {
		return employeeEngagementTaskMapId;
	}

	public void setEmployeeEngagementTaskMapId(UUID employeeEngagementTaskMapId) {
		this.employeeEngagementTaskMapId = employeeEngagementTaskMapId;
	}

	public Date getEngagementStartDate() {
		return engagementStartDate;
	}

	public void setEngagementStartDate(Date engagementStartDate) {
		this.engagementStartDate = engagementStartDate;
	}

	public Date getEngagementEndDate() {
		return engagementEndDate;
	}

	public void setEngagementEndDate(Date engagementEndDate) {
		this.engagementEndDate = engagementEndDate;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public UUID getBillTypeLookupId() {
		return billTypeLookupId;
	}

	public void setBillTypeLookupId(UUID billTypeLookupId) {
		this.billTypeLookupId = billTypeLookupId;
	}
}
