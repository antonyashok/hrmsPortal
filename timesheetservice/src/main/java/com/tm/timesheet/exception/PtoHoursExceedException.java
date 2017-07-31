/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.exception.PtoHoursExceedException.java
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

public class PtoHoursExceedException extends RuntimeException {

    private static final long serialVersionUID = 7827488089186449889L;

    public PtoHoursExceedException(String exceptionMessage) {
        super(exceptionMessage);
    }

}
