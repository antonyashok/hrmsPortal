package com.tm.timesheet.timesheetview.web.rest.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.timesheet.timesheetview.exception.TimesheetDateEmptyException;

@ControllerAdvice
public class TimesheetDateEmptyExceptionTranslator {

	private MessageSourceAccessor accessor;

	private static final String TIMESHEET_DATE_EMPTY = "timesheet.date.empty";

	public TimesheetDateEmptyExceptionTranslator(MessageSource messageSource) {
		accessor = new MessageSourceAccessor(messageSource);
	}

	@ExceptionHandler(TimesheetDateEmptyException.class)
	public ResponseEntity<ErrorVM> processException(TimesheetDateEmptyException ex) {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
		ErrorVM errorVM = new ErrorVM(TIMESHEET_DATE_EMPTY, accessor.getMessage(TIMESHEET_DATE_EMPTY, params));
		return builder.body(errorVM);
	}

}
