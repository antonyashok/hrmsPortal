package com.tm.engagement.service.dto;

import java.io.Serializable;
import java.util.UUID;

public class EngagementContractorsDTO implements Serializable {

	private static final long serialVersionUID = -4370054435898395062L;

	private UUID employeeengagementId;

	private UUID engagementId;

	private long employeeId;

	private String employeeName;
	
	private String employeeDesignationName;
	
	private Boolean checkFlag;
	
	public String getEmployeeDesignationName() {
		return employeeDesignationName;
	}

	public void setEmployeeDesignationName(String employeeDesignationName) {
		this.employeeDesignationName = employeeDesignationName;
	}

	public Boolean getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(Boolean checkFlag) {
		this.checkFlag = checkFlag;
	}

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