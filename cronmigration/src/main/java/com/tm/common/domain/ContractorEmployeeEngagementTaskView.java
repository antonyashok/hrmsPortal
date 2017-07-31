package com.tm.common.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "cnctr_employee_engmt_task")
public class ContractorEmployeeEngagementTaskView implements Serializable {

	private static final long serialVersionUID = -8933732934977458539L;

	@Id
	@Column(name = "empl_engmt_task_map_id")
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID employeeEngagementTaskMapId;

	@Column(name = "empl_engmt_id")
	@Type(type = "uuid-char")
	private UUID employeeEngagementId;
 
	@Column(name = "task_id")
	@Type(type = "uuid-char")
	private UUID taskId;

	@Column(name = "task_nm")
	private String taskName;

	public UUID getEmployeeEngagementTaskMapId() {
		return employeeEngagementTaskMapId;
	}

	public void setEmployeeEngagementTaskMapId(UUID employeeEngagementTaskMapId) {
		this.employeeEngagementTaskMapId = employeeEngagementTaskMapId;
	}

	public UUID getEmployeeEngagementId() {
		return employeeEngagementId;
	}

	public void setEmployeeEngagementId(UUID employeeEngagementId) {
		this.employeeEngagementId = employeeEngagementId;
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

}