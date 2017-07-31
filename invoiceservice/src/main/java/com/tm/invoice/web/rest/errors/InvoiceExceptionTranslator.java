package com.tm.invoice.web.rest.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.invoice.exception.InvoiceBadRequestException;
import com.tm.invoice.exception.InvoiceEngagementRequestException;

@ControllerAdvice
public class InvoiceExceptionTranslator {

	private MessageSourceAccessor accessor;

	private static final String ERR_DATA_NOT_FOUND = "error.invoice.data.not.found";

    private static final String ENGAGEMENT_ID_IS_REQUIRED = "Engagement is Required";

    private static final String ENGAGEMENT_DATA_IS_NOT_AVAILABLE =
            "Engagement data is not available";

	public InvoiceExceptionTranslator(MessageSource messageSource) {
		accessor = new MessageSourceAccessor(messageSource);
	}

	@ExceptionHandler(InvoiceBadRequestException.class)
	public ResponseEntity<ErrorVM> processException(InvoiceBadRequestException ex) {
		Object[] params = new Object[] { ex.getMessage() };
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(accessor.getMessage(ERR_DATA_NOT_FOUND, params));
		return builder.body(errorVM);
	}
	
	@ExceptionHandler(InvoiceEngagementRequestException.class)
    public ResponseEntity<ErrorVM> processException(InvoiceEngagementRequestException ex) {
        BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
        ErrorVM errorVM = new ErrorVM(ex.getMessage());
        return builder.body(errorVM);
    }
	
	

}
