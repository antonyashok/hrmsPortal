package com.tm.timesheet.exception;

public class TimesheetFileUploadException extends RuntimeException {

	private static final long serialVersionUID = 2830260906686658247L;
	
	public TimesheetFileUploadException(String exceptionMessage) {
		super(exceptionMessage);
	}
	
	public TimesheetFileUploadException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
