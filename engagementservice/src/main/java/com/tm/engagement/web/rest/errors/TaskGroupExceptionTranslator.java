package com.tm.engagement.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.engagement.exception.TaskExistException;
import com.tm.engagement.exception.TaskGroupException;

@ControllerAdvice
public class TaskGroupExceptionTranslator {

    public TaskGroupExceptionTranslator() {}

    @ExceptionHandler(TaskGroupException.class)
    public ResponseEntity<ErrorVM> processException(TaskGroupException ex) {
        BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorVM errorVM = new ErrorVM(ex.getMessage());
        return builder.body(errorVM);
    }
    
    @ExceptionHandler(TaskExistException.class)
    public ResponseEntity<ErrorVM> processException(TaskExistException ex) {
        BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorVM errorVM = new ErrorVM(ex.getMessage());
        return builder.body(errorVM);
    }

}
