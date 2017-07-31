package com.tm.timesheet.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "timesheetReport", collectionRelation = "timesheetReports")
public class TimesheetReportExportDTO implements Serializable {

    private static final long serialVersionUID = -6509083926104416762L;

    private byte[] content;
    private String contentType;
    private String fileName;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }



}
