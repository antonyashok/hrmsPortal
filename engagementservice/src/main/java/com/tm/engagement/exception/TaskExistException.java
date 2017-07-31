package com.tm.engagement.exception;

public class TaskExistException extends RuntimeException {

    private static final long serialVersionUID = -7042645303986355155L;

    public TaskExistException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public TaskExistException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
