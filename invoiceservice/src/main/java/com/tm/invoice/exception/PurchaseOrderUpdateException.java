package com.tm.invoice.exception;

public class PurchaseOrderUpdateException extends RuntimeException {

    private static final long serialVersionUID = -5968352764794497870L;

    public PurchaseOrderUpdateException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public PurchaseOrderUpdateException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
