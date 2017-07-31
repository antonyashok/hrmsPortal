package com.tm.engagement.exception;

public class CustomerProfileException extends RuntimeException {

    private static final long serialVersionUID = -7042645303986355155L;

    public CustomerProfileException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public CustomerProfileException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
