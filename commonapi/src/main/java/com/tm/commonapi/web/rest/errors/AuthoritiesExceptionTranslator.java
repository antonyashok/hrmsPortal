package com.tm.commonapi.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.exception.AuthoritiesException;

@ControllerAdvice
public class AuthoritiesExceptionTranslator {

	private static final String EXP_BUSINESS_VALIDATION = "error.authorities.validation";

	@ExceptionHandler(AuthoritiesException.class)
	public ResponseEntity<ErrorVM> configurationGroupProcessException(AuthoritiesException ex) {
		BodyBuilder builder = ResponseEntity.status(HttpStatus.FORBIDDEN);
		ErrorVM errorVM = new ErrorVM(EXP_BUSINESS_VALIDATION, ex.getMessage());
		return builder.body(errorVM);
	}
}
