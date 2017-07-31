/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.exception.UnitsEmptyException.java
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

public class UnitsEmptyException extends RuntimeException {

    private static final long serialVersionUID = 2417334211053257510L;

    public UnitsEmptyException(String exceptionMessage) {
        super(exceptionMessage);
    }
    
    public UnitsEmptyException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

}
