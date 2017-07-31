package com.tm.timesheet.timesheetview.exception;

public class EmployeeTypeMisMatchException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 2417334211053257510L;

    public EmployeeTypeMisMatchException(String exceptionMessage) {
        super(exceptionMessage);
    }

}
