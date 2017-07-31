package com.tm.common.employee.exception;

public class EmployeeFileUploadException extends RuntimeException {

	private static final long serialVersionUID = 2830260906686658247L;
	
	public EmployeeFileUploadException(String exceptionMessage) {
		super(exceptionMessage);
	}
	
	public EmployeeFileUploadException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
