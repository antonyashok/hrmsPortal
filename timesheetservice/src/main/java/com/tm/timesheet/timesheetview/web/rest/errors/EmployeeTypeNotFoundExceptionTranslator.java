package com.tm.timesheet.timesheetview.web.rest.errors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.commonapi.web.rest.errors.FieldErrorVM;
import com.tm.timesheet.timesheetview.constants.TimesheetViewConstants;
import com.tm.timesheet.timesheetview.exception.EmployeeTypeNotFoundException;

@ControllerAdvice
public class EmployeeTypeNotFoundExceptionTranslator {

    public static final String ERR_EMPLOYEE_TYPE_NULL = "error.employeeType.null";
    public static final String ERR_FIELD_NULL = "error.field.null";

    private MessageSourceAccessor accessor;

    public EmployeeTypeNotFoundExceptionTranslator(MessageSource messageSource) {
        accessor = new MessageSourceAccessor(messageSource);
    }

    @ExceptionHandler(EmployeeTypeNotFoundException.class)
    public ResponseEntity<ErrorVM> handleEmployeeTypeNotFoundException(
            EmployeeTypeNotFoundException response) throws IOException {
        Object[] params = new Object[] {response.getMessage()};
        BodyBuilder builder = ResponseEntity.status(HttpStatus.NOT_FOUND);
        FieldErrorVM fieldError =
                new FieldErrorVM(ERR_EMPLOYEE_TYPE_NULL, TimesheetViewConstants.EMPLOYEE_TYPE,
                        accessor.getMessage(ERR_EMPLOYEE_TYPE_NULL, params));
        List<FieldErrorVM> fieldErrors = new ArrayList<>();
        fieldErrors.add(fieldError);
        ErrorVM errorVM = new ErrorVM(ERR_FIELD_NULL, accessor.getMessage(ERR_FIELD_NULL, params),
                fieldErrors);
        return builder.body(errorVM);
    }

}
