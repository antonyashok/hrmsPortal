package com.tm.timesheet.service.dto;

import java.io.Serializable;
import java.util.UUID;

public class EmployeeReportDTO implements Serializable {

    private static final long serialVersionUID = 7267991576900678915L;

    private Long employeeId;
    private String employeeName;
    private String designation;
    private String projectId;

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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }


}
