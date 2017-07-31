package com.tm.common.exception;

public class AclPermissionException extends RuntimeException {

	private static final long serialVersionUID = -714695125483223127L;

	public AclPermissionException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public AclPermissionException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
