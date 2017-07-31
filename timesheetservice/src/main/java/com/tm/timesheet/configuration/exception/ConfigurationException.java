/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.exception.ConfigurationException.java
 * Author        : Annamalai L
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.exception;

public class ConfigurationException extends RuntimeException {

    private static final long serialVersionUID = -7042645303986355155L;

    public ConfigurationException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public ConfigurationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
