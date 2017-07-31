package com.tm.invoice.exception;

public class ContractorIdAlreadyExistException extends RuntimeException{

    private static final long serialVersionUID = -7359214132311718338L;

    public ContractorIdAlreadyExistException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public ContractorIdAlreadyExistException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
    
    
}
