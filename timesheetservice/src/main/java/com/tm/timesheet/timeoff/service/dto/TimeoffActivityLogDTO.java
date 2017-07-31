package com.tm.timesheet.timeoff.service.dto;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TimeoffActivityLogDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6033097772646202554L;
	
	@Id
	private UUID id;
	private Long employeeId;
	private String employeeName;
	private String status;
	private String dateTime;
	private String comments;
	private UUID timeoffId;
	private String roleName;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public UUID getTimeoffId() {
		return timeoffId;
	}

	public void setTimeoffId(UUID timeoffId) {
		this.timeoffId = timeoffId;
	}
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
