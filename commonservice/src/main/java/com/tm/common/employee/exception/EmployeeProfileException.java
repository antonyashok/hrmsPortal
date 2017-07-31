package com.tm.common.employee.exception;

public class EmployeeProfileException extends RuntimeException {

	private static final long serialVersionUID = 8522760747173136864L;

	public EmployeeProfileException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public EmployeeProfileException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
