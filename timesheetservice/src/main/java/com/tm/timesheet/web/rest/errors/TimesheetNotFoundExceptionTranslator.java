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
package com.tm.timesheet.web.rest.errors;

import java.io.IOException;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.timesheet.exception.TimesheetNotFoundException;

@ControllerAdvice
public class TimesheetNotFoundExceptionTranslator {

	public static final String TIMESHEET_NOT_FOUND_EXCEPTION = "error.timesheet.not.found";

	private MessageSourceAccessor accessor;

	public TimesheetNotFoundExceptionTranslator(MessageSource messageSource) {
		accessor = new MessageSourceAccessor(messageSource);
	}

	@ExceptionHandler(TimesheetNotFoundException.class)
	public ResponseEntity<ErrorVM> handleTimesheetDetailsNotFoundException(TimesheetNotFoundException ex)
			throws IOException {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.NOT_FOUND);
		ErrorVM errorVM = new ErrorVM(TIMESHEET_NOT_FOUND_EXCEPTION,
				accessor.getMessage(TIMESHEET_NOT_FOUND_EXCEPTION, params));
		return builder.body(errorVM);
	}

}
