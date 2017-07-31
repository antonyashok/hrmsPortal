package com.tm.timesheet.service.dto;

import java.io.Serializable;
import java.util.List;

public class TimesheetDetailedReportHeaderListDTO implements Serializable {

    private static final long serialVersionUID = -3314835450286024459L;

    private List<TimesheetDetailedReportHeaderDTO> columnList;

    public List<TimesheetDetailedReportHeaderDTO> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<TimesheetDetailedReportHeaderDTO> columnList) {
        this.columnList = columnList;
    }

}
