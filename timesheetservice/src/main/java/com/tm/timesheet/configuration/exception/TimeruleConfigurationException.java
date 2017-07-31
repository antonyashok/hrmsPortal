/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.exception.TimeruleConfigurationException.java
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

public class TimeruleConfigurationException extends RuntimeException {

	private static final long serialVersionUID = -7042645303986355155L;

	public TimeruleConfigurationException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public TimeruleConfigurationException(final String msg, final Throwable cause) {
		super(msg, cause);
	}
}
