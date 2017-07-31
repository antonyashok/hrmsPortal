package com.tm.invoice.exception;

public class InvoiceBadRequestException extends RuntimeException {

    private static final long serialVersionUID = 6896036549922872822L;

    public InvoiceBadRequestException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public InvoiceBadRequestException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
