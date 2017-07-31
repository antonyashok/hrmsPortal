package com.tm.common.employee.exception;

public class EmployeeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8181455654080158291L;

	public EmployeeNotFoundException() {

	}

	public EmployeeNotFoundException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public EmployeeNotFoundException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
