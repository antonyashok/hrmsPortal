package com.tm.timesheet.timeoff.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "pto_avail")
public class PtoAvailableView implements Serializable {

	private static final long serialVersionUID = -3996522176189454337L;

	@Id
	@Column(name = "pto_avl_id", length = 36)
	private String ptoAvaliableId;

	@Column(name = "empl_id")
	private Long employeeId;

	@Column(name = "full_name")
	private String employeeName;

	@Column(name = "mgr_empl_id")
	private Long managerEmployeeId;

	@Column(name = "alloted")
	private Double allotedHours;

	@Column(name = "draft")
	private Double draftHours;

	@Column(name = "availed")
	private Double availedHours;

	@Column(name = "requested")
	private Double requestedHours;

	@Column(name = "approved")
	private Double approvedHours;

	@Column(name = "balance")
	private Double balanceHours;
	
	@Type(type = "uuid-char")
	@Column(name = "engmt_id")
	private UUID engagementId;
	
	@Column(name = "engmt_nm")
	private String engagementName;
	 

	public String getPtoAvaliableId() {
		return ptoAvaliableId;
	}

	public void setPtoAvaliableId(String ptoAvaliableId) {
		this.ptoAvaliableId = ptoAvaliableId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Double getAllotedHours() {
		return allotedHours;
	}

	public void setAllotedHours(Double allotedHours) {
		this.allotedHours = allotedHours;
	}

	public Double getDraftHours() {
		return draftHours;
	}

	public void setDraftHours(Double draftHours) {
		this.draftHours = draftHours;
	}

	public Double getAvailedHours() {
		return availedHours;
	}

	public void setAvailedHours(Double availedHours) {
		this.availedHours = availedHours;
	}

	public Double getRequestedHours() {
		return requestedHours;
	}

	public void setRequestedHours(Double requestedHours) {
		this.requestedHours = requestedHours;
	}

	public Double getApprovedHours() {
		return approvedHours;
	}

	public void setApprovedHours(Double approvedHours) {
		this.approvedHours = approvedHours;
	}

	public Long getManagerEmployeeId() {
		return managerEmployeeId;
	}

	public void setManagerEmployeeId(Long managerEmployeeId) {
		this.managerEmployeeId = managerEmployeeId;
	}

	public Double getBalanceHours() {
		return balanceHours;
	}

	public void setBalanceHours(Double balanceHours) {
		this.balanceHours = balanceHours;
	}

	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}

	public String getEngagementName() {
		return engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
	}
	
	

}
