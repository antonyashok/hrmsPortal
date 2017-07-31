package com.tm.timesheetgeneration.exception;

public class TimeoffException extends RuntimeException {

	private static final long serialVersionUID = -7042645303986355155L;

	public TimeoffException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public TimeoffException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
