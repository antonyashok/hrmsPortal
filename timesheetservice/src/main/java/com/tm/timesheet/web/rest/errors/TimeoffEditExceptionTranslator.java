/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.web.rest.errors.TimeoffEditExceptionTranslator.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.web.rest.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.timesheet.exception.TimeoffEditException;

@ControllerAdvice
public class TimeoffEditExceptionTranslator {
  
  public TimeoffEditExceptionTranslator(MessageSource messageSource) {
      new MessageSourceAccessor(messageSource);
  }
  
  @ExceptionHandler(TimeoffEditException.class)
  public ResponseEntity<ErrorVM> processException(TimeoffEditException ex) {
		BodyBuilder builder;
		ErrorVM errorVM;
		builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
		errorVM = new ErrorVM(ex.getMessage(), ex.getMessage());
		return builder.body(errorVM);
  }

}
