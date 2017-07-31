/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.exception.NotificationAttributeNotFoundException.java
 * Author        : Annamalai L
 * Date Created  : Feb 06, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.exception;

public class NotificationAttributeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -4357571202858310718L;

	public NotificationAttributeNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public NotificationAttributeNotFoundException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}