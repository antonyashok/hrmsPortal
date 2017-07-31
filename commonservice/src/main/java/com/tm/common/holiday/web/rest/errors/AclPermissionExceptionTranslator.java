package com.tm.common.holiday.web.rest.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.common.exception.AclPermissionException;
import com.tm.commonapi.web.rest.errors.ErrorVM;

@ControllerAdvice
public class AclPermissionExceptionTranslator {

	private MessageSourceAccessor accessor;

	private static final String ERR_UNPROCESSABLE_ENTITY = "error.roleDesignation.unprocessable.entity";
	
	public AclPermissionExceptionTranslator(MessageSource messageSource) {
		accessor = new MessageSourceAccessor(messageSource);
	}
	
	@ExceptionHandler(AclPermissionException.class)
	public ResponseEntity<ErrorVM> processException(AclPermissionException ex) {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
		ErrorVM errorVM = new ErrorVM(accessor.getMessage(ERR_UNPROCESSABLE_ENTITY, params));
		return builder.body(errorVM);
	}
}
