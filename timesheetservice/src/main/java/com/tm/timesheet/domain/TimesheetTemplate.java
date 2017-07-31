package com.tm.timesheet.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "timesheet_template")
public class TimesheetTemplate implements Serializable {

	private static final long serialVersionUID = 7342074500649215951L;

    public enum Active {
        Y, N
    }

    @Id
    @Column(name = "tm_template_id")
    private Long timesheetTemplateId;

    @Column(name = "actv_flg")
    @Enumerated(EnumType.STRING)
    private Active activeStatus;

    @Column(name = "tm_template_desc")
    private String timesheetTemplatedesc;

    @Column(name = "tm_template_nm")
    private String timesheetTemplateName;

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

	public Long getTimesheetTemplateId() {
		return timesheetTemplateId;
	}

	public void setTimesheetTemplateId(Long timesheetTemplateId) {
		this.timesheetTemplateId = timesheetTemplateId;
	}

	public String getTimesheetTemplatedesc() {
		return timesheetTemplatedesc;
	}

	public void setTimesheetTemplatedesc(String timesheetTemplatedesc) {
		this.timesheetTemplatedesc = timesheetTemplatedesc;
	}

	public String getTimesheetTemplateName() {
		return timesheetTemplateName;
	}

	public void setTimesheetTemplateName(String timesheetTemplateName) {
		this.timesheetTemplateName = timesheetTemplateName;
	}
}
