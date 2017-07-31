/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.invoice.web.rest.errors.PoNotExistExceptionTranslator.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.invoice.web.rest.errors;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tm.commonapi.web.rest.errors.ErrorVM;
import com.tm.invoice.exception.PoNotExistException;
import com.tm.invoice.exception.PoNotMappedException;
import com.tm.invoice.exception.PurchaseOrderUpdateException;

@ControllerAdvice
public class PoNotExistExceptionTranslator {
  
  private MessageSourceAccessor accessor;

  private static final String PO_NOT_EXIST = "po.not.exist";
  
  private static final String PO_MAPPED_EXIST = "po.mapped.exist";
  
  public PoNotExistExceptionTranslator(MessageSource messageSource) {
      accessor = new MessageSourceAccessor(messageSource);
  }
  
  @ExceptionHandler(PoNotMappedException.class)
  public ResponseEntity<ErrorVM> processExceptionn(PoNotExistException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(PO_MAPPED_EXIST,
				accessor.getMessage(PO_MAPPED_EXIST, params));
		return builder.body(errorVM);
  }
  
  @ExceptionHandler(PoNotExistException.class)
  public ResponseEntity<ErrorVM> processException(PoNotExistException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(PO_NOT_EXIST,
				accessor.getMessage(PO_NOT_EXIST, params));
		return builder.body(errorVM);
  }
  
  @ExceptionHandler(PurchaseOrderUpdateException.class)
    public ResponseEntity<ErrorVM> processException(PurchaseOrderUpdateException ex) {
        BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
        ErrorVM errorVM = new ErrorVM(ex.getMessage(), ex.getMessage());
        return builder.body(errorVM);
    }
  
  
}