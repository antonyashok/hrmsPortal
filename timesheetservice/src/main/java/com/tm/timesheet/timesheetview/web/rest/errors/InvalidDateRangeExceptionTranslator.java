package com.tm.timesheet.timesheetview.web.rest.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.timesheet.timesheetview.exception.InvalidDateRangeException;

@ControllerAdvice
public class InvalidDateRangeExceptionTranslator {

    public static final String DATE_VALIDATION_ERROR = "error.date.value";

    private MessageSourceAccessor accessor;

    public InvalidDateRangeExceptionTranslator(MessageSource messageSource) {
        accessor = new MessageSourceAccessor(messageSource);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ErrorVM> badRequestForActorType(InvalidDateRangeException ex)
            throws InvalidDateRangeException {
        Object[] params = new Object[] {ex.getMessage()};
        BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorVM errorVM = new ErrorVM(DATE_VALIDATION_ERROR,
                accessor.getMessage(DATE_VALIDATION_ERROR, params));
        return builder.body(errorVM);
    }

}
