package com.tm.timesheet.timeoff.exception;

public class TimeoffBadRequestException extends RuntimeException {

    private static final long serialVersionUID = 6896036549922872822L;

    public TimeoffBadRequestException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public TimeoffBadRequestException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
