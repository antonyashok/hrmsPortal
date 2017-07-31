package com.tm.timesheet.timesheetview.exception;

public class InvalidDateRangeException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 7827488089186449889L;

    public InvalidDateRangeException(String exceptionMessage) {
        super(exceptionMessage);
    }

}
