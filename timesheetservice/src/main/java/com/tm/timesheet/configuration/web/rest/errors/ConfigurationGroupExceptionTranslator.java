package com.tm.timesheet.configuration.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.timesheet.configuration.exception.ConfigurationGroupException;

@ControllerAdvice
public class ConfigurationGroupExceptionTranslator {
  
  private static final String EXP_BUSINESS_VALIDATION = "error.configurationGroupDTO.validation";
  
  @ExceptionHandler(ConfigurationGroupException.class)
  public ResponseEntity<ErrorVM> configurationGroupProcessException(ConfigurationGroupException ex) {
    BodyBuilder builder = ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY);
    ErrorVM errorVM = new ErrorVM(EXP_BUSINESS_VALIDATION, ex.getMessage());
    return builder.body(errorVM);
  }
}
