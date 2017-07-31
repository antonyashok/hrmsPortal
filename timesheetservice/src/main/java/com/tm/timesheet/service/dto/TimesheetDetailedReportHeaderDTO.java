package com.tm.timesheet.service.dto;

import java.io.Serializable;

public class TimesheetDetailedReportHeaderDTO implements Serializable {

    private static final long serialVersionUID = -802155024168485075L;

    private String field;
    private String header;
    private boolean isSort;
    private String className;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public boolean isSort() {
        return isSort;
    }

    public void setSort(boolean isSort) {
        this.isSort = isSort;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    
}
