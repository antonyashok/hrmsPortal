package com.tm.invoice.exception;

public class ActivityLogNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4639384810712959066L;

	public ActivityLogNotFoundException() {

	}

	public ActivityLogNotFoundException(String exceptionMessage) {
		super(exceptionMessage);
	}

}