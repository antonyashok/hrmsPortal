package com.tm.commonapi.exception;

public class UnAuthorizedException extends RuntimeException {

    private static final long serialVersionUID = -4048879670586706994L;

    private String exceptionMessage;

    public UnAuthorizedException(String exceptionMessage) {
        super(exceptionMessage);
        this.exceptionMessage = exceptionMessage;
    }

    public UnAuthorizedException(String exceptionMessage, Throwable cause) {
        super(exceptionMessage, cause);
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }


}
