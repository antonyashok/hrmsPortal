/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.exception.TimeRuleConfigurationNotFoundException.java
 * Author        : Hemanth Kumar
 * Date Created  : Feb 28, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.exception;

public class TimeRuleConfigurationInActiveException extends RuntimeException {

	private static final long serialVersionUID = 7495901298774495490L;

	public TimeRuleConfigurationInActiveException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public TimeRuleConfigurationInActiveException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}

