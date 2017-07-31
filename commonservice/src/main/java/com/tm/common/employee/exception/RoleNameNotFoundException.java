package com.tm.common.employee.exception;

public class RoleNameNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6596460529417560638L;

    public RoleNameNotFoundException() {

    }

    public RoleNameNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public RoleNameNotFoundException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
