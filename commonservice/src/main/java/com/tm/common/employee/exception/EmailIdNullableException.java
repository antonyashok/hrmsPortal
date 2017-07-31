package com.tm.common.employee.exception;

public class EmailIdNullableException extends RuntimeException {

	private static final long serialVersionUID = -2717409359073005123L;

	public EmailIdNullableException() {

	}

	public EmailIdNullableException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public EmailIdNullableException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
