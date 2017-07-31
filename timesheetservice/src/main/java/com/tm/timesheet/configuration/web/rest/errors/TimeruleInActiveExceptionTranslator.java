/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.web.rest.errors.TimeruleExceptionTranslator.java
 * Author        : Hemanth Kumar
 * Date Created  : Feb 28, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.web.rest.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.timesheet.configuration.exception.TimeRuleConfigurationInActiveException;

@ControllerAdvice
public class TimeruleInActiveExceptionTranslator {

	private MessageSourceAccessor accessor;
	
	public static final String TIMERULE_ID_DOES_NOT_EXIST = "timerule.error.inactive";

	public TimeruleInActiveExceptionTranslator(MessageSource messageSource) {
		accessor = new MessageSourceAccessor(messageSource);
	}
 

	@ExceptionHandler(TimeRuleConfigurationInActiveException.class)
	public ResponseEntity<ErrorVM> processRuntimeExceptions(TimeRuleConfigurationInActiveException ex) {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
		ErrorVM errorVM = new ErrorVM(TIMERULE_ID_DOES_NOT_EXIST,
				accessor.getMessage(TIMERULE_ID_DOES_NOT_EXIST, params));
		return builder.body(errorVM);
	}
}
