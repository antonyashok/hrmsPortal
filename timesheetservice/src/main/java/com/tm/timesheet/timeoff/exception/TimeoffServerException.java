package com.tm.timesheet.timeoff.exception;

public class TimeoffServerException extends RuntimeException {

    private static final long serialVersionUID = -7042645303986355155L;

    public TimeoffServerException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public TimeoffServerException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
