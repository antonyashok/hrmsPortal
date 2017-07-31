package com.tm.timesheet.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "TimesheetAttachments", collectionRelation = "TimesheetAttachments")
public class TimesheetAttachmentsDTO extends ResourceSupport implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8535624909834341306L;
    private String timesheetAttachmentId;
    private UUID timesheetId;
    private String fileName;
    private String uploadedDate;
    private byte[] content;
    private String contentType;

    public String getTimesheetAttachmentId() {
        return timesheetAttachmentId;
    }

    public void setTimesheetAttachmentId(String timesheetAttachmentId) {
        this.timesheetAttachmentId = timesheetAttachmentId;
    }

    public UUID getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(UUID timesheetId) {
        this.timesheetId = timesheetId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(String uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
