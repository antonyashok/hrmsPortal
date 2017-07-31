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
import com.tm.timesheet.timesheetview.exception.DaysExceedException;

@ControllerAdvice
public class DaysExceedExceptionTranslator {

    public static final String ERR_DAYS_EXCEED = "error.days.exceed";

    private MessageSourceAccessor accessor;

    public DaysExceedExceptionTranslator(MessageSource messageSource) {
        accessor = new MessageSourceAccessor(messageSource);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(DaysExceedException.class)
    public ResponseEntity<ErrorVM> badRequestForActorType(DaysExceedException ex)
            throws DaysExceedException {
        Object[] params = new Object[] {ex.getMessage()};
        BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorVM errorVM = new ErrorVM(ERR_DAYS_EXCEED,
                accessor.getMessage(ERR_DAYS_EXCEED, params));
        return builder.body(errorVM);
    }

}
