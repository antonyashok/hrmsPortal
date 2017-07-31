package com.tm.invoice.exception;

public class ContractorEmailIdAlreadyExistException extends RuntimeException{

    private static final long serialVersionUID = -7359214132311718338L;

    public ContractorEmailIdAlreadyExistException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public ContractorEmailIdAlreadyExistException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
    
    
}
