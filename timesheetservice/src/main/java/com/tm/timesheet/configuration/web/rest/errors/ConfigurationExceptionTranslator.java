package com.tm.timesheet.configuration.web.rest.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.timesheet.configuration.exception.ConfigurationNotFoundException;

@ControllerAdvice
public class ConfigurationExceptionTranslator {
  
  private MessageSourceAccessor accessor;

  private static final String ERR_DATA_NOT_FOUND = "error.data.not.found";

  public ConfigurationExceptionTranslator(MessageSource messageSource) {
      accessor = new MessageSourceAccessor(messageSource);
  }
  
  @ExceptionHandler(ConfigurationNotFoundException.class)
  public ResponseEntity<ErrorVM> processException(ConfigurationNotFoundException ex) {
    Object[] params = new Object[]{ex.getMessage()};
    BodyBuilder builder = ResponseEntity.status(HttpStatus.NOT_FOUND);
    ErrorVM errorVM = new ErrorVM(ERR_DATA_NOT_FOUND, accessor.getMessage(ERR_DATA_NOT_FOUND,params));
    return builder.body(errorVM);
  }

}
