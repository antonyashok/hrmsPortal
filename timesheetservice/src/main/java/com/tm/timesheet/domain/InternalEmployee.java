package com.tm.timesheet.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "internal_employee")
public class InternalEmployee implements Serializable {

    private static final long serialVersionUID = 3909798456079207727L;

    @Id
    @Column(name = "empl_id")
    private Long employeeId;

    @Column(name = "full_name")
    private String name;

    @Column(name = "designation_name")
    private String designation;

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
    
}
