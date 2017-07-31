package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "employee_engagement")
public class EmployeeEngagement extends AbstractAuditingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5095639679714523383L;

	@Id
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "empl_engmt_id")
	private UUID employeeEngagamentId;

	@Column(name = "empl_id")
	private Long employeeId;

	@Column(name = "effctv_strt_dt")
	private Date effectiveStartDate;

	@Column(name = "effctv_end_dt")
	private Date effectiveEndDate;

	@Column(name = "am_empl_id")
	private Long accountManagerEmployeeId;

	@Column(name = "cust_mgr_contact_id")
	private Long custManagerEmployeeId;

	@Column(name = "actv_flg")
	private String activeFlag;

	@Column(name = "rctr_empl_id")
	private Long recruiterEmployeeId;
	
	@Column(name = "contr_empl_id")
	private String contractorEmployeeId;
	
	@Column(name = "contr_email_id")
	private String contractorEmailId;
	
	@Column(name = "office_id")
	private Long officeId;
	
	@Column(name = "timesheet_generation")
	private String timesheetGeneratedFlag;

	@ManyToOne
	@JoinColumn(name = "engmt_id", insertable = true, updatable = true)
	private Engagement engagement;

	@ManyToOne
	@JoinColumn(name = "wk_pln_id")
	private WeekPlan weekPlan;

	public UUID getEmployeeEngagamentId() {
		return employeeEngagamentId;
	}

	public void setEmployeeEngagamentId(UUID employeeEngagamentId) {
		this.employeeEngagamentId = employeeEngagamentId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public WeekPlan getWeekPlan() {
		return weekPlan;
	}

	public void setWeekPlan(WeekPlan weekPlan) {
		this.weekPlan = weekPlan;
	}

	public Long getAccountManagerEmployeeId() {
		return accountManagerEmployeeId;
	}

	public void setAccountManagerEmployeeId(Long accountManagerEmployeeId) {
		this.accountManagerEmployeeId = accountManagerEmployeeId;
	}

	public Engagement getEngagement() {
		return engagement;
	}

	public void setEngagement(Engagement engagement) {
		this.engagement = engagement;
	}

	public Long getCustManagerEmployeeId() {
		return custManagerEmployeeId;
	}

	public void setCustManagerEmployeeId(Long custManagerEmployeeId) {
		this.custManagerEmployeeId = custManagerEmployeeId;
	}

	public Long getRecruiterEmployeeId() {
		return recruiterEmployeeId;
	}

	public void setRecruiterEmployeeId(Long recruiterEmployeeId) {
		this.recruiterEmployeeId = recruiterEmployeeId;
	}

	public String getContractorEmployeeId() {
		return contractorEmployeeId;
	}

	public void setContractorEmployeeId(String contractorEmployeeId) {
		this.contractorEmployeeId = contractorEmployeeId;
	}

	public String getContractorEmailId() {
		return contractorEmailId;
	}

	public void setContractorEmailId(String contractorEmailId) {
		this.contractorEmailId = contractorEmailId;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public String getTimesheetGeneratedFlag() {
		return timesheetGeneratedFlag;
	}

	public void setTimesheetGeneratedFlag(String timesheetGeneratedFlag) {
		this.timesheetGeneratedFlag = timesheetGeneratedFlag;
	}
	
	
}
