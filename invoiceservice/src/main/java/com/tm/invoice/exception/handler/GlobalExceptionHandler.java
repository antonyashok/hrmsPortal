package com.tm.invoice.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tm.commonapi.exception.RecordNotFoundException;
import com.tm.invoice.constants.ApplicationConstants;
import com.tm.commonapi.exception.ApplicationException;
import com.tm.commonapi.exception.BusinessException;
import com.tm.commonapi.exception.UnAuthorizedException;
import com.tm.invoice.util.InvoiceCommonUtils;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(
            value = {RecordNotFoundException.class, HttpRequestMethodNotSupportedException.class,
                    MissingServletRequestParameterException.class, BusinessException.class})
    public ObjectNode handleException(Exception inputException) {
        log.error("Exception : {}", inputException.getClass(), inputException);
        return InvoiceCommonUtils.getResponseObjectNode(inputException.getMessage(), false);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {ApplicationException.class})
    public ObjectNode handleApplicationException(ApplicationException inputException) {
        log.error("ApplicationException : {}", inputException.getClass(), inputException);
        return InvoiceCommonUtils.getResponseObjectNode(ApplicationConstants.SERVICE_NOT_AVAILABLE, false);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ObjectNode handleDataIntegrityViolationException(DataIntegrityViolationException dive) {
        log.error("DataIntegrityViolationException :", dive);
        return InvoiceCommonUtils.getResponseObjectNode(ApplicationConstants.DATA_INTEGRITY_MESSAGE,
                false);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {NullPointerException.class})
    public ObjectNode handleNullPointerException(NullPointerException npe) {
        log.error("NullPointerException :", npe);
        return InvoiceCommonUtils.getResponseObjectNode(ApplicationConstants.INTERNAL_ERROR, false);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public ObjectNode handleEmptyResultDataAccessException(EmptyResultDataAccessException erdae) {
        log.error("EmptyResultDataAccessException :", erdae);
        return InvoiceCommonUtils.getResponseObjectNode(ApplicationConstants.MISSING_RECORD_MESSAGE,
                false);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ObjectNode handleHttpMessageNotReadableException(HttpMessageNotReadableException hmnre) {
        log.error("HttpMessageNotReadableException :", hmnre);
        return InvoiceCommonUtils.getResponseObjectNode(hmnre.getMostSpecificCause().getMessage(), false);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = InvalidDataAccessApiUsageException.class)
    public ObjectNode handleInvalidDataAccessApiUsageException(
            InvalidDataAccessApiUsageException idaue) {
        log.error("InvalidDataAccessApiUsageException :", idaue);
        return InvoiceCommonUtils.getResponseObjectNode(ApplicationConstants.INVALID_REQUEST, false);
    }

    /*@ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ObjectNode handleMethodArgumentNotValidException(MethodArgumentNotValidException manve) {
        BindingResult bindingResult = manve.getBindingResult();
        if (bindingResult != null) {
            return CommonUtils.getValidationErrorResponseObjectNode(
                    ApplicationConstants.VALIDATION_ERROR, bindingResult);
        }
        return CommonUtils.getResponseObjectNode(ApplicationConstants.INVALID_REQUEST, false);
    }*/

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = UnAuthorizedException.class)
    public ObjectNode handleUnAuthorizedException(UnAuthorizedException uae) {
        log.error("UnAuthorized Exception : {}", uae.getClass(), uae);
        return InvoiceCommonUtils.getResponseObjectNode(uae.getExceptionMessage(), false, true);
    }
}
