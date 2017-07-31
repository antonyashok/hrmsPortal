package com.tm.common.holiday.exception;

public class InvalidDateRangeException extends RuntimeException {

	private static final long serialVersionUID = 1426155040350358174L;

	public InvalidDateRangeException() {
	}

	public InvalidDateRangeException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
