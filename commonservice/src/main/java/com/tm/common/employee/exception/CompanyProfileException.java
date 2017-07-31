package com.tm.common.employee.exception;

public class CompanyProfileException extends RuntimeException {

	private static final long serialVersionUID = -4136979604910557396L;

	public CompanyProfileException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public CompanyProfileException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
