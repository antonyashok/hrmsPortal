package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "engagement_cnctr")
public class EngagementContractors implements Serializable {

	private static final long serialVersionUID = -4370054435898395062L;

	@Id
	@Type(type = "uuid-char")
	@Column(name = "empl_engmt_id")
	private UUID employeeengagementId;
	
	@Column(name = "engmt_id")
	@Type(type = "uuid-char")
	private UUID engagementId;
	
	@Column(name = "empl_id")
	private long employeeId;
	
	@Column(name = "emp_full_name")
	private String employeeName;

	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public UUID getEmployeeengagementId() {
		return employeeengagementId;
	}

	public void setEmployeeengagementId(UUID employeeengagementId) {
		this.employeeengagementId = employeeengagementId;
	}

	
}