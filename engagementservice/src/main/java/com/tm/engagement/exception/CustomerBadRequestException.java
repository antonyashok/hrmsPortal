package com.tm.engagement.exception;

public class CustomerBadRequestException extends RuntimeException {

    private static final long serialVersionUID = 6896036549922872822L;

    public CustomerBadRequestException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public CustomerBadRequestException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
