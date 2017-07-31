package com.tm.engagement.exception;

public class EngagementTaskException extends RuntimeException {


    private static final long serialVersionUID = -7042645303986355155L;

    public EngagementTaskException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public EngagementTaskException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
