package com.tm.engagement.exception;

public class BillingProfileExistException extends RuntimeException {

    private static final long serialVersionUID = -7042645303986355155L;

    public BillingProfileExistException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public BillingProfileExistException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
