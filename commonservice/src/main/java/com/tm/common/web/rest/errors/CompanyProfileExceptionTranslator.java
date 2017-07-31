package com.tm.common.web.rest.errors;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.common.employee.exception.CompanyProfileException;
import com.tm.commonapi.web.rest.errors.ErrorVM;

@ControllerAdvice
public class CompanyProfileExceptionTranslator {

	private static final String ERR_UNPROCESSABLE_ENTITY = "error.roleDesignation.unprocessable.entity";

	private MessageSourceAccessor accessor;

	public CompanyProfileExceptionTranslator(MessageSource messageSource) {
		accessor = new MessageSourceAccessor(messageSource);
	}

	@ExceptionHandler(CompanyProfileException.class)
	public ResponseEntity<ErrorVM> processException(CompanyProfileException ex) {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
		ErrorVM errorVM = new ErrorVM(ERR_UNPROCESSABLE_ENTITY, accessor.getMessage(ERR_UNPROCESSABLE_ENTITY, params));
		List<ErrorVM> errorVMs = new ArrayList<ErrorVM>();
		errorVMs.add(errorVM);
		return builder.body(errorVM);
	}

}
