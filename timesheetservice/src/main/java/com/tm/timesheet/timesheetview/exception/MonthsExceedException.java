package com.tm.timesheet.timesheetview.exception;

public class MonthsExceedException extends RuntimeException {

    
    private static final long serialVersionUID = 1179781036150399453L;

    public MonthsExceedException() {

    }

    public MonthsExceedException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
