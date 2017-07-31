package com.tm.common.holiday.exception;

public class HolidayNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -443081238875914899L;

	public HolidayNotFoundException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public HolidayNotFoundException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
