package com.tm.timesheet.timesheetview.exception;

public class TimesheetDetailsNotFoundException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7824013059711255563L;

	public TimesheetDetailsNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public TimesheetDetailsNotFoundException(final String msg, final Throwable cause) {
        super(msg, cause);
    }


}
