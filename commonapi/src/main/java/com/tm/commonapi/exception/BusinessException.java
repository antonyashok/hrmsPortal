package com.tm.commonapi.exception;

import java.util.Properties;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 5281364421866367094L;

    private String errorConstant;

    Properties exceptionProperties = new Properties();

    public BusinessException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public BusinessException(final String msg, final Throwable cause) {
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
