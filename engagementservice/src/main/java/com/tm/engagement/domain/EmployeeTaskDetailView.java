package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "employee_task_detail_view")
public class EmployeeTaskDetailView implements Serializable {

	private static final long serialVersionUID = -4688877122490578837L;

	@Id
	@Column(name = "empl_engmt_task_map_id")
	@Type(type = "uuid-char")
	private UUID employeeengagementId;
	
	@Column(name = "engmt_id")
	@Type(type = "uuid-char")
	private UUID engagementId;

	@Column(name = "task_id")
	@Type(type = "uuid-char")
	private UUID taskId;
	
	@Column(name = "task_nm")
	private String taskName;
	
	@Column(name = "task_desc")
	private String taskDesc;
	
	@Column(name = "empl_id")
	private String employeeId;

	@Column(name = "employee_name")
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