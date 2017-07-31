package com.tm.engagement.web.rest.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.engagement.exception.BillingProfileExistException;

@ControllerAdvice
public class BillingProfileExistExceptionTranslator {

	private MessageSourceAccessor accessor;

	private static final String ERR_UNPROCESSABLE_ENTITY = "error.billing.profile.employee.engagement.mapped";
	
	public BillingProfileExistExceptionTranslator(MessageSource messageSource) {
		accessor = new MessageSourceAccessor(messageSource);
	}
	
	@ExceptionHandler(BillingProfileExistException.class)
	public ResponseEntity<ErrorVM> processException(BillingProfileExistException ex) {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
		ErrorVM errorVM = new ErrorVM(accessor.getMessage(ERR_UNPROCESSABLE_ENTITY, params));
		return builder.body(errorVM);
	}
}
