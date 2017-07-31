/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.exception.BreakHoursExceedException.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.exception;

public class BreakHoursExceedException extends RuntimeException {

    private static final long serialVersionUID = 5588555146374095023L;

    public BreakHoursExceedException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public BreakHoursExceedException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
