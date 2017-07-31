package com.tm.common.employee.exception;

public class RoleDesignationException extends RuntimeException {

	private static final long serialVersionUID = -714695125483223127L;

	public RoleDesignationException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public RoleDesignationException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
