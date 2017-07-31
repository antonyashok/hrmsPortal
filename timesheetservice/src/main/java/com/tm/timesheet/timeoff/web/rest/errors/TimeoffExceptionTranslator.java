package com.tm.timesheet.timeoff.web.rest.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.timesheet.timeoff.exception.TimeoffBadRequestException;
import com.tm.timesheet.timeoff.exception.TimeoffException;
import com.tm.timesheet.timeoff.exception.TimeoffServerException;

@ControllerAdvice
public class TimeoffExceptionTranslator {

	private MessageSourceAccessor accessor;

	private static final String ERR_DATA_NOT_FOUND = "error.timeoff.data.not.found";
	private static final String ERR_UNPROCESSABLE_ENTITY = "error.timeoff.unprocessable.entity";

	public TimeoffExceptionTranslator(MessageSource messageSource) {
		accessor = new MessageSourceAccessor(messageSource);
	}

	@ExceptionHandler(TimeoffBadRequestException.class)
	public ResponseEntity<ErrorVM> processException(TimeoffBadRequestException ex) {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(accessor.getMessage(ERR_DATA_NOT_FOUND, params));
		return builder.body(errorVM);
	}

	@ExceptionHandler(TimeoffException.class)
	public ResponseEntity<ErrorVM> processException(TimeoffException ex) {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
		ErrorVM errorVM = new ErrorVM(accessor.getMessage(ERR_UNPROCESSABLE_ENTITY, params));
		return builder.body(errorVM);
	}
	
	@ExceptionHandler(TimeoffServerException.class)
	public ResponseEntity<ErrorVM> processException(TimeoffServerException ex) {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
		ErrorVM errorVM = new ErrorVM(accessor.getMessage(ERR_DATA_NOT_FOUND, params));
		return builder.body(errorVM);
	}
	
	

}
