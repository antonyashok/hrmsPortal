package com.tm.common.employee.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee_template")
public class EmployeeTemplate implements Serializable {

	private static final long serialVersionUID = 7342074500649215951L;

    public enum Active {
        Y, N
    }

    @Id
    @Column(name = "emp_template_id")
    private Long employeeTemplateId;

    @Column(name = "actv_flg")
    @Enumerated(EnumType.STRING)
    private Active activeStatus;

    @Column(name = "emp_template_desc")
    private String employeeTemplatedesc;

    @Column(name = "emp_template_nm")
    private String employeeTemplateName;

    private byte[] template;
    
    @Column(name="created_by")
    private Long createdBy;

    @Column(name="created_on")
    private Date createdOn;
    
    @Column(name="updated_by")
    private Long updatedBy;

    @Column(name="updated_on")
    private Date updatedOn;

    public Active getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Active activeStatus) {
        this.activeStatus = activeStatus;
    }

    public byte[] getTemplate() {
        return template;
    }

    public void setTemplate(byte[] template) {
        this.template = template;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

	public Long getEmployeeTemplateId() {
		return employeeTemplateId;
	}

	public void setEmployeeTemplateId(Long employeeTemplateId) {
		this.employeeTemplateId = employeeTemplateId;
	}

	public String getEmployeeTemplatedesc() {
		return employeeTemplatedesc;
	}

	public void setEmployeeTemplatedesc(String employeeTemplatedesc) {
		this.employeeTemplatedesc = employeeTemplatedesc;
	}

	public String getEmployeeTemplateName() {
		return employeeTemplateName;
	}

	public void setEmployeeTemplateName(String employeeTemplateName) {
		this.employeeTemplateName = employeeTemplateName;
	}
}
