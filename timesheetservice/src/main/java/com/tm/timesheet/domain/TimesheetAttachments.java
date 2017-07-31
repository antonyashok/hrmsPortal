package com.tm.timesheet.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "timesheetAttachments")
public class TimesheetAttachments implements Serializable {

    private static final long serialVersionUID = -5869412205555328304L;

    @Id
    @Column(name = "_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private UUID timesheetId;

    private AuditFields updated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(UUID timesheetId) {
        this.timesheetId = timesheetId;
    }

    public AuditFields getUpdated() {
        return updated;
    }

    public void setUpdated(AuditFields updated) {
        this.updated = updated;
    }

}
