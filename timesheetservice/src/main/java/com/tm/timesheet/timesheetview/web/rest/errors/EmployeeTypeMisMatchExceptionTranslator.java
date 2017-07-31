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
import com.tm.timesheet.timesheetview.exception.EmployeeTypeMisMatchException;

@ControllerAdvice
public class EmployeeTypeMisMatchExceptionTranslator {

    public static final String ERR_EMPLOYEE_TYPE_MISMATCH = "error.employeeType.mismatch";

    private MessageSourceAccessor accessor;

    public EmployeeTypeMisMatchExceptionTranslator(MessageSource messageSource) {
        accessor = new MessageSourceAccessor(messageSource);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(EmployeeTypeMisMatchException.class)
    public ResponseEntity<ErrorVM> badRequestForActorType(EmployeeTypeMisMatchException ex)
            throws EmployeeTypeMisMatchException {
        Object[] params = new Object[] {ex.getMessage()};
        BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorVM errorVM = new ErrorVM(ERR_EMPLOYEE_TYPE_MISMATCH,
                accessor.getMessage(ERR_EMPLOYEE_TYPE_MISMATCH, params));
        return builder.body(errorVM);
    }

}
