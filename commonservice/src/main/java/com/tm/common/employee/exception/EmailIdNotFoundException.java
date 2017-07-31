package com.tm.common.employee.exception;

public class EmailIdNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -6596460529417560638L;

	public EmailIdNotFoundException() {

	}

	public EmailIdNotFoundException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public EmailIdNotFoundException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
