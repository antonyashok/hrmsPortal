package com.tm.common.employee.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

public class ReportingManagerDTO extends ResourceSupport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String employeeId;
    private String employeeName;
    
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
