package com.tm.timesheet.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cnctr_employee_engmts")
public class Contractor implements Serializable {

    private static final long serialVersionUID = 3309491753146532351L;

    @Id
    @Column(name = "empl_id")
    private Long employeeId;

    @Column(name = "emp_full_name")
    private String name;

    @Column(name = "emp_role_name")
    private String designation;

    @Column(name = "engmt_id")
    private String projectId;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }   

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
