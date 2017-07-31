package com.tm.engagement.exception;

public class DateNotNullException extends RuntimeException {

	private static final long serialVersionUID = -8436756451946542005L;

	public DateNotNullException(String exceptionMessage){
		 super(exceptionMessage);
	}
}
