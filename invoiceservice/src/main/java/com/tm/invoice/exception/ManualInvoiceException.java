package com.tm.invoice.exception;

import java.util.Properties;

public class ManualInvoiceException extends RuntimeException {

    private static final long serialVersionUID = 3250846036282311814L;

    private String errorConstant;

    Properties exceptionProperties = new Properties();

    public ManualInvoiceException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public ManualInvoiceException(final String msg, final Throwable cause) {
        super(msg, cause);
        this.errorConstant = msg;
    }

    public void addRuntimeDataToException(String key, String value) {
        exceptionProperties.put(key, value);
    }

    /**
     * @return the errorConstant
     */
    public String getErrorConstant() {
        return errorConstant;
    }

    /**
     * @param errorConstant the errorConstant to set
     */
    public void setErrorConstant(String errorConstant) {
        this.errorConstant = errorConstant;
    }

    /**
     * @return the exceptionProperties
     */
    public Properties getExceptionProperties() {
        return exceptionProperties;
    }

    /**
     * @param exceptionProperties the exceptionProperties to set
     */
    public void setExceptionProperties(Properties exceptionProperties) {
        this.exceptionProperties = exceptionProperties;
    }


}
