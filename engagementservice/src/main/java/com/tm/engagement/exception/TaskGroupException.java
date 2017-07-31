package com.tm.engagement.exception;

public class TaskGroupException extends RuntimeException {

    private static final long serialVersionUID = -7042645303986355155L;

    public TaskGroupException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public TaskGroupException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
