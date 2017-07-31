package com.tm.engagement.service.dto;

import java.io.Serializable;
import java.util.UUID;

public class EmployeeTaskDetailDTO implements Serializable {

	private static final long serialVersionUID = -4688877122490578837L;

	private UUID engagementId;

	private UUID employeeengagementId;

	private UUID taskId;
	
	private String taskName;
	
	private String taskDesc;
	
	private String employeeId;

	private String employeeName;

	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}

	public UUID getEmployeeengagementId() {
		return employeeengagementId;
	}

	public void setEmployeeengagementId(UUID employeeengagementId) {
		this.employeeengagementId = employeeengagementId;
	}

	public UUID getTaskId() {
		return taskId;
	}

	public void setTaskId(UUID taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

}