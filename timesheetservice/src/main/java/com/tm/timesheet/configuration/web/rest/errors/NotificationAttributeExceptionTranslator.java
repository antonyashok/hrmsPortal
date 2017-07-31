/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.web.rest.errors.NotificationAttributeExceptionTranslator.java
 * Author        : Annamalai L
 * Date Created  : Feb 06, 2017
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
import com.tm.timesheet.configuration.exception.NotificationAttributeNotFoundException;
import com.tm.timesheet.configuration.exception.NotificationEmailException;
import com.tm.timesheet.configuration.exception.NotificationEmailLimitExceedException;

@ControllerAdvice
public class NotificationAttributeExceptionTranslator {

    private MessageSourceAccessor accessor;
    
    private static final String ERR_NOTIFICATION_ATTRIBUTE_NOT_FOUND = "error.notification.attribute.not.found";
    
    private static final String ERR_NOTIFICATION_EMAIL_LIMIT_EXCEEDS = "error.notification.email.limit.exceeds";

    private static final String ERR_NOTIFICATION_EMAIL_VALUE_EXCEPTION = "error.notification.email.value";

    public NotificationAttributeExceptionTranslator(MessageSource messageSource) {
        accessor = new MessageSourceAccessor(messageSource);
    }

    @ExceptionHandler(NotificationAttributeNotFoundException.class)
    public ResponseEntity<ErrorVM> processException(NotificationAttributeNotFoundException ex) {
        Object[] params = new Object[] {ex.getMessage()};
        BodyBuilder builder = ResponseEntity.status(HttpStatus.NOT_FOUND);
        ErrorVM errorVM = new ErrorVM(ERR_NOTIFICATION_ATTRIBUTE_NOT_FOUND,
                accessor.getMessage(ERR_NOTIFICATION_ATTRIBUTE_NOT_FOUND, params));
        return builder.body(errorVM);
    }


    @ExceptionHandler(NotificationEmailLimitExceedException.class)
    public ResponseEntity<ErrorVM> processEmailException(NotificationEmailLimitExceedException ex) {
        Object[] params = new Object[] {ex.getMessage()};
        BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorVM errorVM = new ErrorVM(ERR_NOTIFICATION_EMAIL_LIMIT_EXCEEDS,
                accessor.getMessage(ERR_NOTIFICATION_EMAIL_LIMIT_EXCEEDS, params));
        return builder.body(errorVM);
    }

    @ExceptionHandler(NotificationEmailException.class)
    public ResponseEntity<ErrorVM> processEmailValueException(NotificationEmailException ex) {
        BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorVM errorVM = new ErrorVM(ERR_NOTIFICATION_EMAIL_VALUE_EXCEPTION,
                accessor.getMessage(ERR_NOTIFICATION_EMAIL_VALUE_EXCEPTION,
                        ex.getMessage()));
        return builder.body(errorVM);
    }

}
