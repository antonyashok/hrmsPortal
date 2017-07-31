package com.tm.engagement.web.rest.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.engagement.exception.CustomerBadRequestException;
import com.tm.engagement.exception.CustomerProfileException;


@ControllerAdvice
public class CustomerProfileExceptionTranslator {

	private MessageSourceAccessor accessor;

	private static final String ERR_DATA_NOT_FOUND = "error.data.not.found";
	private static final String ERR_UNPROCESSABLE_ENTITY = "error.unprocessable.entity";

	public CustomerProfileExceptionTranslator(MessageSource messageSource) {
		accessor = new MessageSourceAccessor(messageSource);
	}

	@ExceptionHandler(CustomerBadRequestException.class)
	public ResponseEntity<ErrorVM> processException(CustomerBadRequestException ex) {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(accessor.getMessage(ERR_DATA_NOT_FOUND, params));
		return builder.body(errorVM);
	}

	@ExceptionHandler(CustomerProfileException.class)
	public ResponseEntity<ErrorVM> processException(CustomerProfileException ex) {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
		ErrorVM errorVM = new ErrorVM(accessor.getMessage(ERR_UNPROCESSABLE_ENTITY, params));
		return builder.body(errorVM);
	}

}
