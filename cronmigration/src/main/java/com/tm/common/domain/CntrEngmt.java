package com.tm.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "cnctr_engmts")
public class CntrEngmt implements Serializable {

	private static final long serialVersionUID = -984316159662215091L;

	public enum day {
		Sun(7), Mon(1), Tue(2), Wed(3), Thu(4), Fri(5), Sat(6);
		private int value;

		private day(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	@Column(name = "empl_id")
	private Long emplId;

	@Column(name = "engmt_id")
	private String engagementId;

	@Column(name = "emp_full_name")
	private String name;

	@Column(name = "am_empl_id")
	private Long accountManagerId;

	@Column(name = "am_full_name")
	private String accountManagerName;

	@Column(name = "am_email_id")
	private String accountManagerMailId;

	@Column(name = "rctr_empl_id")
	private Long recruiterId;

	@Column(name = "rctr_full_name")
	private String recruiterName;

	@Column(name = "rctr_email_id")
	private String recruiterMailId;

	@Column(name = "client_mgr_id")
	private Long clientManagerId;

	@Column(name = "client_mgr_full_name")
	private String clientManagerName;

	@Column(name = "client_mgr_email_id")
	private String clientManagerMailId;

	@Column(name = "file_no")
	private Long fileNumber;

	@Column(name = "empl_engmt_id")
	private String employeeEngagementId;

	@Column(name = "engmt_nm")
	private String engagementName;

	@Temporal(TemporalType.DATE)
	@Column(name = "engmt_strt_dt")
	private Date engagementStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "engmt_end_dt")
	private Date engagementEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "effctv_strt_dt")
	private Date emplEffStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "effctv_end_dt")
	private Date emplEffEndDate;

	@Column(name = "ts_entry_lkp_id")
	private String tsEntryLookUpId;

	@Column(name = "ts_entry_type")
	private String tsEntryLookUpName;

	@Column(name = "ts_method_lkp_id")
	private String tsMethodLookUpId;

	@Column(name = "ts_method")
	private String tsMethod;

	@Enumerated(EnumType.STRING)
	@Column(name = "start_day")
	private day startDay;

	@Enumerated(EnumType.STRING)
	@Column(name = "end_day")
	private day endDay;

	@Id
	@Column(name = "engmt_task_id")
	private String engagementTaskId;

	@Column(name = "empl_engmt_task_map_id")
	private String emplEngmtTaskMapId;

	@Column(name = "task_nm")
	private String taskName;

	@Column(name = "time_rule_id")
	private String timeruleId;

	@Column(name = "bill_type_lkp_id")
	private String billTypeLookupId;

	@Column(name = "bill_type")
	private String billType;

	public Long getEmplId() {
		return emplId;
	}

	public void setEmplId(Long emplId) {
		this.emplId = emplId;
	}

	public String getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(String engagementId) {
		this.engagementId = engagementId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAccountManagerId() {
		return accountManagerId;
	}

	public void setAccountManagerId(Long accountManagerId) {
		this.accountManagerId = accountManagerId;
	}

	public String getAccountManagerName() {
		return accountManagerName;
	}

	public void setAccountManagerName(String accountManagerName) {
		this.accountManagerName = accountManagerName;
	}

	public String getAccountManagerMailId() {
		return accountManagerMailId;
	}

	public void setAccountManagerMailId(String accountManagerMailId) {
		this.accountManagerMailId = accountManagerMailId;
	}

	public Long getRecruiterId() {
		return recruiterId;
	}

	public void setRecruiterId(Long recruiterId) {
		this.recruiterId = recruiterId;
	}

	public String getRecruiterName() {
		return recruiterName;
	}

	public void setRecruiterName(String recruiterName) {
		this.recruiterName = recruiterName;
	}

	public String getRecruiterMailId() {
		return recruiterMailId;
	}

	public void setRecruiterMailId(String recruiterMailId) {
		this.recruiterMailId = recruiterMailId;
	}

	public Long getClientManagerId() {
		return clientManagerId;
	}

	public void setClientManagerId(Long clientManagerId) {
		this.clientManagerId = clientManagerId;
	}

	public String getClientManagerName() {
		return clientManagerName;
	}

	public void setClientManagerName(String clientManagerName) {
		this.clientManagerName = clientManagerName;
	}

	public String getClientManagerMailId() {
		return clientManagerMailId;
	}

	public void setClientManagerMailId(String clientManagerMailId) {
		this.clientManagerMailId = clientManagerMailId;
	}

	public Long getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(Long fileNumber) {
		this.fileNumber = fileNumber;
	}

	public String getEmployeeEngagementId() {
		return employeeEngagementId;
	}

	public void setEmployeeEngagementId(String employeeEngagementId) {
		this.employeeEngagementId = employeeEngagementId;
	}

	public String getEngagementName() {
		return engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
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

	public Date getEmplEffStartDate() {
		return emplEffStartDate;
	}

	public void setEmplEffStartDate(Date emplEffStartDate) {
		this.emplEffStartDate = emplEffStartDate;
	}

	public Date getEmplEffEndDate() {
		return emplEffEndDate;
	}

	public void setEmplEffEndDate(Date emplEffEndDate) {
		this.emplEffEndDate = emplEffEndDate;
	}

	public String getTsEntryLookUpId() {
		return tsEntryLookUpId;
	}

	public void setTsEntryLookUpId(String tsEntryLookUpId) {
		this.tsEntryLookUpId = tsEntryLookUpId;
	}

	public String getTsEntryLookUpName() {
		return tsEntryLookUpName;
	}

	public void setTsEntryLookUpName(String tsEntryLookUpName) {
		this.tsEntryLookUpName = tsEntryLookUpName;
	}

	public String getTsMethodLookUpId() {
		return tsMethodLookUpId;
	}

	public void setTsMethodLookUpId(String tsMethodLookUpId) {
		this.tsMethodLookUpId = tsMethodLookUpId;
	}

	public String getTsMethod() {
		return tsMethod;
	}

	public void setTsMethod(String tsMethod) {
		this.tsMethod = tsMethod;
	}

	public day getStartDay() {
		return startDay;
	}

	public void setStartDay(day startDay) {
		this.startDay = startDay;
	}

	public day getEndDay() {
		return endDay;
	}

	public void setEndDay(day endDay) {
		this.endDay = endDay;
	}

	public String getEngagementTaskId() {
		return engagementTaskId;
	}

	public void setEngagementTaskId(String engagementTaskId) {
		this.engagementTaskId = engagementTaskId;
	}

	public String getEmplEngmtTaskMapId() {
		return emplEngmtTaskMapId;
	}

	public void setEmplEngmtTaskMapId(String emplEngmtTaskMapId) {
		this.emplEngmtTaskMapId = emplEngmtTaskMapId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTimeruleId() {
		return timeruleId;
	}

	public void setTimeruleId(String timeruleId) {
		this.timeruleId = timeruleId;
	}

	public String getBillTypeLookupId() {
		return billTypeLookupId;
	}

	public void setBillTypeLookupId(String billTypeLookupId) {
		this.billTypeLookupId = billTypeLookupId;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

}