package com.tm.timesheet.timesheetview.exception;

public class EmployeeTypeNotFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5588555146374095023L;

    public EmployeeTypeNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }

}
