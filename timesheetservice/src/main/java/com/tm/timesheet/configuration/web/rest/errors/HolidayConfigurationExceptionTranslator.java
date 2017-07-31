/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.web.rest.errors.HolidayConfigurationExceptionTranslator.java
 * Author        : Annamalai L
 * Date Created  : Feb 15, 2017
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
import com.tm.timesheet.configuration.exception.HolidayConfigurationException;
import com.tm.timesheet.configuration.exception.HolidayDateConfigurationException;
import com.tm.timesheet.configuration.exception.HolidayDescriptionConfigurationException;

@ControllerAdvice
public class HolidayConfigurationExceptionTranslator {

    private MessageSourceAccessor accessor;
    
    private static final String HOLIDAY_DATE_ALREADY_EXISTS = "error.holiday.date.already.exists";
    
    private static final String HOLIDAY_DESC_EMPTY = "error.holiday.desc.empty";
    
    private static final String HOLIDAY_DATE_EMPTY = "error.holiday.date.empty";

    public HolidayConfigurationExceptionTranslator(MessageSource messageSource) {
        accessor = new MessageSourceAccessor(messageSource);
    }

    @ExceptionHandler(HolidayConfigurationException.class)
    public ResponseEntity<ErrorVM> processException(HolidayConfigurationException ex) {
        Object[] params = new Object[]{ex.getMessage()};
        BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorVM errorVM = new ErrorVM(HOLIDAY_DATE_ALREADY_EXISTS,
                accessor.getMessage(HOLIDAY_DATE_ALREADY_EXISTS, params));
        return builder.body(errorVM);
    }

    @ExceptionHandler(HolidayDescriptionConfigurationException.class)
    public ResponseEntity<ErrorVM> processException(HolidayDescriptionConfigurationException ex) {
        Object[] params = new Object[]{ex.getMessage()};
        BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorVM errorVM = new ErrorVM(HOLIDAY_DESC_EMPTY,
                accessor.getMessage(HOLIDAY_DESC_EMPTY, params));
        return builder.body(errorVM);
    }

    @ExceptionHandler(HolidayDateConfigurationException.class)
    public ResponseEntity<ErrorVM> processException(HolidayDateConfigurationException ex) {
        Object[] params = new Object[]{ex.getMessage()};
        BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorVM errorVM = new ErrorVM(HOLIDAY_DATE_EMPTY,
                accessor.getMessage(HOLIDAY_DATE_EMPTY, params));
        return builder.body(errorVM);
    }

}
