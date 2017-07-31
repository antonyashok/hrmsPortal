package com.tm.timesheet.timesheetview.exception;

public class TimesheetDateEmptyException extends RuntimeException {

	private static final long serialVersionUID = 2417334211053257510L;

	public TimesheetDateEmptyException(String exceptionMessage) {
        super(exceptionMessage);
    }

	public TimesheetDateEmptyException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
