package com.tm.common.holiday.web.rest.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tm.common.holiday.exception.HolidayNotFoundException;
import com.tm.common.holiday.exception.InvalidDateRangeException;
import com.tm.commonapi.web.rest.errors.ErrorVM;

@ControllerAdvice
public class HolidayExceptionTranslator {

	public static final String ERR_HOLIDAY_NOT_FOUND = "holidays.details.not.found";
	public static final String DATE_VALIDATION_ERROR = "error.date.value";

	private MessageSourceAccessor accessor;

	public HolidayExceptionTranslator(MessageSource messageSource) {
		accessor = new MessageSourceAccessor(messageSource);
	}

	@ExceptionHandler(HolidayNotFoundException.class)
	public ResponseEntity<ErrorVM> processException(HolidayNotFoundException ex) {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.NOT_FOUND);
		ErrorVM errorVM = new ErrorVM(ERR_HOLIDAY_NOT_FOUND,
				accessor.getMessage(ERR_HOLIDAY_NOT_FOUND, params));
		return builder.body(errorVM);
	}

	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ExceptionHandler(InvalidDateRangeException.class)
	public ResponseEntity<ErrorVM> badRequestForActorType(
			InvalidDateRangeException ex) {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity
				.status(HttpStatus.UNPROCESSABLE_ENTITY);
		ErrorVM errorVM = new ErrorVM(DATE_VALIDATION_ERROR,
				accessor.getMessage(DATE_VALIDATION_ERROR, params));
		return builder.body(errorVM);
	}
}
