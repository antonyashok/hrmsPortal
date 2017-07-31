package com.tm.timesheet.timesheetview.exception;

public class CommentsNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1179781036150399453L;

	public CommentsNotFoundException() {

    }

    public CommentsNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
