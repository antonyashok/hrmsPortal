package com.tm.timesheet.timesheetview.exception;

public class DaysExceedException extends RuntimeException {

    
    private static final long serialVersionUID = 1179781036150399453L;

    public DaysExceedException() {

    }

    public DaysExceedException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
