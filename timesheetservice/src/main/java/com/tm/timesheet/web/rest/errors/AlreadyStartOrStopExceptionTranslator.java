/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.web.rest.errors.WorkedHoursExceedExceptionTranslator.java
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
import com.tm.timesheet.exception.AlreadyTimerStartException;
import com.tm.timesheet.exception.AlreadyTimerStopException;

@ControllerAdvice
public class AlreadyStartOrStopExceptionTranslator {
  
  private MessageSourceAccessor accessor;

  private static final String ALREADY_TIMER_STARTED = "already.timer.started";
  private static final String ALREADY_TIMER_STOPED = "already.timer.stoped";

  public AlreadyStartOrStopExceptionTranslator(MessageSource messageSource) {
      accessor = new MessageSourceAccessor(messageSource);
  }
  
  @ExceptionHandler(AlreadyTimerStartException.class)
  public ResponseEntity<ErrorVM> processException(AlreadyTimerStartException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
		ErrorVM errorVM = new ErrorVM(ALREADY_TIMER_STARTED,
				accessor.getMessage(ALREADY_TIMER_STARTED, params));
		return builder.body(errorVM);
  }
  
  @ExceptionHandler(AlreadyTimerStopException.class)
  public ResponseEntity<ErrorVM> processException(AlreadyTimerStopException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
		ErrorVM errorVM = new ErrorVM(ALREADY_TIMER_STOPED,
				accessor.getMessage(ALREADY_TIMER_STOPED, params));
		return builder.body(errorVM);
  }

}
