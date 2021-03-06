/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.web.rest.errors.StartDateEmptyExceptionTranslator.java
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
import com.tm.timesheet.exception.StartDateEmptyException;

@ControllerAdvice
public class StartDateEmptyExceptionTranslator {
  
  private MessageSourceAccessor accessor;

  private static final String WORKED_HOURS_EXCEED = "start.date.empty";

  public StartDateEmptyExceptionTranslator(MessageSource messageSource) {
      accessor = new MessageSourceAccessor(messageSource);
  }
  
  @ExceptionHandler(StartDateEmptyException.class)
  public ResponseEntity<ErrorVM> processException(StartDateEmptyException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
		ErrorVM errorVM = new ErrorVM(WORKED_HOURS_EXCEED,
				accessor.getMessage(WORKED_HOURS_EXCEED, params));
		return builder.body(errorVM);
  }

}
