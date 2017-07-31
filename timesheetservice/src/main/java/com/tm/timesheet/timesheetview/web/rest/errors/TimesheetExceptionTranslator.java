package com.tm.timesheet.timesheetview.web.rest.errors;

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
import com.tm.timesheet.timesheetview.exception.ActivityLogNotFoundException;
import com.tm.timesheet.timesheetview.exception.CommentsNotFoundException;


@ControllerAdvice
public class TimesheetExceptionTranslator {

    public final String ERR_ACTIVITY_LOG_NOT_FOUND = "activity.logs.not.found";
    public final String ERR_COMMENTS_NOT_FOUND = "comments.not.found";

    private MessageSourceAccessor accessor;

    public TimesheetExceptionTranslator(MessageSource messageSource) {
        accessor = new MessageSourceAccessor(messageSource);
    }

    @ExceptionHandler(ActivityLogNotFoundException.class)
    public ResponseEntity<List<ErrorVM>> processException(ActivityLogNotFoundException ex) {
        Object[] params = new Object[] {ex.getMessage()};
        BodyBuilder builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorVM errorVM = new ErrorVM(ERR_ACTIVITY_LOG_NOT_FOUND,
                accessor.getMessage(ERR_ACTIVITY_LOG_NOT_FOUND, params));
        List<ErrorVM> errorVMs = new ArrayList<ErrorVM>();
        errorVMs.add(errorVM);
        return builder.body(errorVMs);
    }
    
    @ExceptionHandler(CommentsNotFoundException.class)
    public ResponseEntity<ErrorVM> processException(CommentsNotFoundException ex) {
        Object[] params = new Object[] {ex.getMessage()};
        BodyBuilder builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorVM errorVM = new ErrorVM(ERR_COMMENTS_NOT_FOUND,
                accessor.getMessage(ERR_COMMENTS_NOT_FOUND, params));
        List<ErrorVM> errorVMs = new ArrayList<ErrorVM>();
        errorVMs.add(errorVM);
        return builder.body(errorVM);
    }


}
