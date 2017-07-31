/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.exception.TimesheetNotFoundException.java
 * Author        : Annamalai L
 * Date Created  : Mar 16, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.exception;

public class TimesheetNotFoundException extends RuntimeException{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7824013059711255563L;

	public TimesheetNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public TimesheetNotFoundException(final String msg, final Throwable cause) {
        super(msg, cause);
    }


}
