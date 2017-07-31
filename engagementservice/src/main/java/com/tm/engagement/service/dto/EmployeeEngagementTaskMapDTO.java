package com.tm.engagement.service.dto;

import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

public class EmployeeEngagementTaskMapDTO {

	private String employeeEngagementTaskMapId;

	private String activeFlag;

	@OneToMany(mappedBy = "emplEngmtTaskMap")
	private List<BillingProfileTimeBasedTaskRateDTO> billingProfileTimeBasedTaskRate;

	@OneToMany(mappedBy = "emplEngmtTaskMap")
	private List<BillingProfileUnitsBasedTaskRateDTO> billingProfileUnitsBasedTaskRate;

	@ManyToOne
	@JoinColumn(name = "empl_engmt_id")
	private EmployeeEngagementDTO employeeEngagement;

	private TaskDetailDTO task;

	public String getEmployeeEngagementTaskMapId() {
		return employeeEngagementTaskMapId;
	}

	public void setEmployeeEngagementTaskMapId(String employeeEngagementTaskMapId) {
		this.employeeEngagementTaskMapId = employeeEngagementTaskMapId;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public List<BillingProfileTimeBasedTaskRateDTO> getBillingProfileTimeBasedTaskRate() {
		return billingProfileTimeBasedTaskRate;
	}

	public void setBillingProfileTimeBasedTaskRate(
			List<BillingProfileTimeBasedTaskRateDTO> billingProfileTimeBasedTaskRate) {
		this.billingProfileTimeBasedTaskRate = billingProfileTimeBasedTaskRate;
	}

	public List<BillingProfileUnitsBasedTaskRateDTO> getBillingProfileUnitsBasedTaskRate() {
		return billingProfileUnitsBasedTaskRate;
	}

	public void setBillingProfileUnitsBasedTaskRate(
			List<BillingProfileUnitsBasedTaskRateDTO> billingProfileUnitsBasedTaskRate) {
		this.billingProfileUnitsBasedTaskRate = billingProfileUnitsBasedTaskRate;
	}

	public EmployeeEngagementDTO getEmployeeEngagement() {
		return employeeEngagement;
	}

	public void setEmployeeEngagement(EmployeeEngagementDTO employeeEngagement) {
		this.employeeEngagement = employeeEngagement;
	}

	public TaskDetailDTO getTask() {
		return task;
	}

	public void setTask(TaskDetailDTO task) {
		this.task = task;
	}

}
