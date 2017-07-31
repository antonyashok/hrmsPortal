package com.tm.common.exception;

public class CommonLookupException extends RuntimeException {

	private static final long serialVersionUID = -7042645303986355155L;

	public CommonLookupException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public CommonLookupException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
