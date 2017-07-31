package com.tm.commonapi.exception;

import java.util.Properties;

import org.springframework.http.converter.HttpMessageNotReadableException;

public class ValidationException extends HttpMessageNotReadableException {

    private static final long serialVersionUID = 5449767711232210988L;

    private String errorConstant;

    Properties exceptionProperties = new Properties();

    public ValidationException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public ValidationException(final String msg, final Throwable cause) {
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
