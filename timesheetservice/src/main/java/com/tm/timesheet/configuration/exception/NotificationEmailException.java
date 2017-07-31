/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.configuration.exception.NotificationEmailException.java
 * Author        : Annamalai L
 * Date Created  : Mar 11, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/

package com.tm.timesheet.configuration.exception;

public class NotificationEmailException extends RuntimeException {

    private static final long serialVersionUID = 6896036549922872822L;

    public NotificationEmailException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public NotificationEmailException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
