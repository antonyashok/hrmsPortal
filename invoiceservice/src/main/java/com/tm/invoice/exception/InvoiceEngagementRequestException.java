package com.tm.invoice.exception;

public class InvoiceEngagementRequestException extends RuntimeException {

    private static final long serialVersionUID = -6639633224128982443L;

    public InvoiceEngagementRequestException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public InvoiceEngagementRequestException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
    
}
