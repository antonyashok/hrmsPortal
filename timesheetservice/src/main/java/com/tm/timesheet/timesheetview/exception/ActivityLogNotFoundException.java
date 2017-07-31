package com.tm.timesheet.timesheetview.exception;

public class ActivityLogNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3543052929720193882L;

	public ActivityLogNotFoundException() {

    }

    public ActivityLogNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }

}
