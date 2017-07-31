package com.tm.invoice.web.rest.errors;

import java.io.IOException;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.exception.FileUploadException;
import com.tm.commonapi.web.rest.errors.ErrorVM;

@ControllerAdvice
public class FileUploadExceptionTranslator {

    public static final String FILE_SIZE_EXCEED = "error.fileSize.value";

    public FileUploadExceptionTranslator(MessageSource messageSource) {
        new MessageSourceAccessor(messageSource);
    }

    @SuppressWarnings("rawtypes")
	@ExceptionHandler(FileUploadException.class)
    public ResponseEntity handleFileUploadException(FileUploadException response)
            throws IOException {
        BodyBuilder builder = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY);
        ErrorVM errorVM = new ErrorVM(response.getMessage(), response.getMessage());
        return builder.body(errorVM);
    }
}
