package com.tm.invoice.exception;

public class ProfileStartDateGreaterThanJoiningDateException extends RuntimeException{

    private static final long serialVersionUID = -7359214132311718338L;

    public ProfileStartDateGreaterThanJoiningDateException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public ProfileStartDateGreaterThanJoiningDateException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
    
    
}
