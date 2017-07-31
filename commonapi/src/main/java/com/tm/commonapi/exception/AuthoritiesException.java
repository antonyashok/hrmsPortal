package com.tm.commonapi.exception;

public class AuthoritiesException extends RuntimeException {

    private static final long serialVersionUID = -4048879670586706994L;

    public AuthoritiesException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public AuthoritiesException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
