package com.tm.common.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.common.exception.CommonLookupException;
import com.tm.commonapi.web.rest.errors.ErrorVM;

@ControllerAdvice
public class CommonLookupExceptionTranslator {

	public static final String ERR_DATA_NOT_FOUND = "error.data.not.found";

	@ExceptionHandler(CommonLookupException.class)
	public ResponseEntity<ErrorVM> processException(CommonLookupException ex) {
		BodyBuilder builder = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE);
		ErrorVM errorVM = new ErrorVM(ERR_DATA_NOT_FOUND, ex.getMessage());
		return builder.body(errorVM);
	}

}
