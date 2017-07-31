package com.tm.timesheet.timesheetview.web.rest.errors;

import java.io.IOException;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.timesheet.timesheetview.exception.TimesheetDetailsNotFoundException;

@ControllerAdvice
public class TimesheetDetailsNotFoundExceptionTranslator {

	public static final String TIMESHEET_NOT_FOUND_EXCEPTION = "error.timesheet.not.found";

	private MessageSourceAccessor accessor;

	public TimesheetDetailsNotFoundExceptionTranslator(MessageSource messageSource) {
		accessor = new MessageSourceAccessor(messageSource);
	}

	@ExceptionHandler(TimesheetDetailsNotFoundException.class)
	public ResponseEntity<ErrorVM> handleTimesheetDetailsNotFoundException(TimesheetDetailsNotFoundException ex)
			throws IOException {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.NOT_FOUND);
		ErrorVM errorVM = new ErrorVM(TIMESHEET_NOT_FOUND_EXCEPTION,
				accessor.getMessage(TIMESHEET_NOT_FOUND_EXCEPTION, params));
		return builder.body(errorVM);
	}

}
