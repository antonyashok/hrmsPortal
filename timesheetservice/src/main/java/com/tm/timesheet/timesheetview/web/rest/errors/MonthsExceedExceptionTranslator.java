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
import com.tm.timesheet.timesheetview.exception.MonthsExceedException;

@ControllerAdvice
public class MonthsExceedExceptionTranslator {

    public static final String ERR_MONTHS_EXCEED = "error.months.exceed";

    private MessageSourceAccessor accessor;

    public MonthsExceedExceptionTranslator(MessageSource messageSource) {
        accessor = new MessageSourceAccessor(messageSource);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MonthsExceedException.class)
    public ResponseEntity<ErrorVM> badRequestForActorType(MonthsExceedException ex)
            throws MonthsExceedException {
        Object[] params = new Object[] {ex.getMessage()};
        BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorVM errorVM = new ErrorVM(ERR_MONTHS_EXCEED,
                accessor.getMessage(ERR_MONTHS_EXCEED, params));
        return builder.body(errorVM);
    }
}
