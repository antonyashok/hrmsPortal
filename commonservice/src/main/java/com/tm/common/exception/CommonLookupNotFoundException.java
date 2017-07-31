package com.tm.common.exception;

public class CommonLookupNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7042645303986355155L;

	public CommonLookupNotFoundException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public CommonLookupNotFoundException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
