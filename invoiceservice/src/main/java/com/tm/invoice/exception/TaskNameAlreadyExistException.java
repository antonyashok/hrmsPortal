/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.exception.StartAndEndLessTimeExceedException.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.invoice.exception;

public class TaskNameAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 5588555146374095023L;

    public TaskNameAlreadyExistException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public TaskNameAlreadyExistException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
