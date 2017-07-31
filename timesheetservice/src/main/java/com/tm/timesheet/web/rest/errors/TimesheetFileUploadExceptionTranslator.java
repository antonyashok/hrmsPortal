package com.tm.timesheet.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.timesheet.exception.TimesheetFileUploadException;

@ControllerAdvice
public class TimesheetFileUploadExceptionTranslator {
	
	@ExceptionHandler(TimesheetFileUploadException.class)
	public ResponseEntity<ErrorVM> processException(TimesheetFileUploadException ex) {
		BodyBuilder builder = ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY);
		ErrorVM errorVM = new ErrorVM("error.timesheet.fileUpload", ex.getMessage());
		return builder.body(errorVM);
	}
}
