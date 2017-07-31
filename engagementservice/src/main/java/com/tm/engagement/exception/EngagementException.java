package com.tm.engagement.exception;

public class EngagementException extends RuntimeException {

    private static final long serialVersionUID = -7042645303986355155L;

    public EngagementException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public EngagementException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
