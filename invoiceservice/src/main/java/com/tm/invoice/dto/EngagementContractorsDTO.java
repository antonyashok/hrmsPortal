package com.tm.invoice.dto;

import java.util.UUID;

public class EngagementContractorsDTO {

	private UUID employeeengagementId;

	private UUID engagementId;

	private long employeeId;

	private String employeeName;

	public UUID getEmployeeengagementId() {
		return employeeengagementId;
	}

	public void setEmployeeengagementId(UUID employeeengagementId) {
		this.employeeengagementId = employeeengagementId;
	}

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

}