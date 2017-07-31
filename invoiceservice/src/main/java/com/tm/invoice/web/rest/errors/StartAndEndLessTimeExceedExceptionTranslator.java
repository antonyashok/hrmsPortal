/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.invoice.web.rest.errors.StartAndEndLessTimeExceedExceptionTranslator.java
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
import com.tm.invoice.exception.BillProfileAlreadyMappedException;
import com.tm.invoice.exception.BillProfileNotExpiredException;
import com.tm.invoice.exception.ContractorEmailIdAlreadyExistException;
import com.tm.invoice.exception.ContractorIdAlreadyExistException;
import com.tm.invoice.exception.ProfileEndDateLessThanEffectiveEndDateException;
import com.tm.invoice.exception.ProfileStartDateGreaterThanJoiningDateException;
import com.tm.invoice.exception.ProfileStartDateLessThanEffectiveStartDateException;
import com.tm.invoice.exception.ProjectStartAndEndDateBetweenPoDateException;
import com.tm.invoice.exception.StartDateAndEndDateExceedException;
import com.tm.invoice.exception.TaskNameAlreadyExistException;
import com.tm.invoice.exception.TaskNotExistException;

@ControllerAdvice
public class StartAndEndLessTimeExceedExceptionTranslator {
  
  private MessageSourceAccessor accessor;

  private static final String START_END_DATE_EXCEED = "start.end.date.less.than";
  private static final String EFFECTIVE_START_DATE_EXCEED = "effective.start.date.less.than";
  private static final String EFFECTIVE_END_DATE_EXCEED = "effective.end.date.less.than";
  private static final String TASK_NOT_EXIST = "task.not.exist";
  private static final String TASK_ALREADY_EXIST = "task.already.exist";
  private static final String PO_DATE_BETWEEN_PROJECT_START_END_DATE = "po.date.between.project.start.end.date";
  private static final String BILLPROFILE_ALREADY_EXIST = "billprofile.already.exist";
  private static final String BILLPROFILE_INACTIVE_EXIST = "billprofile.inactive.exist";
  private static final String CONTRACTORID_ALREDADY_EXIST = "contractorId.already.exist";
  private static final String CONTRACTOREMAILID_ALREDADY_EXIST = "contractoremailid.already.exist";
  private static final String PROF_START_DATE_EXCEP = "The given profile start date should be greater than or equal to employee joining date";

  public StartAndEndLessTimeExceedExceptionTranslator(MessageSource messageSource) {
      accessor = new MessageSourceAccessor(messageSource);
  }
  
  @ExceptionHandler(StartDateAndEndDateExceedException.class)
  public ResponseEntity<ErrorVM> processException(StartDateAndEndDateExceedException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(START_END_DATE_EXCEED,
				accessor.getMessage(START_END_DATE_EXCEED, params));
		return builder.body(errorVM);
  }
  
  @ExceptionHandler(ProfileStartDateLessThanEffectiveStartDateException.class)
  public ResponseEntity<ErrorVM> processException(ProfileStartDateLessThanEffectiveStartDateException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(EFFECTIVE_START_DATE_EXCEED,
				accessor.getMessage(EFFECTIVE_START_DATE_EXCEED, params));
		return builder.body(errorVM);
  }
  
  @ExceptionHandler(ProfileEndDateLessThanEffectiveEndDateException.class)
  public ResponseEntity<ErrorVM> processException(ProfileEndDateLessThanEffectiveEndDateException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(EFFECTIVE_END_DATE_EXCEED,
				accessor.getMessage(EFFECTIVE_END_DATE_EXCEED, params));
		return builder.body(errorVM);
  }
  
  @ExceptionHandler(TaskNotExistException.class)
  public ResponseEntity<ErrorVM> processException(TaskNotExistException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(TASK_NOT_EXIST,
				accessor.getMessage(TASK_NOT_EXIST, params));
		return builder.body(errorVM);
  }
  
  @ExceptionHandler(TaskNameAlreadyExistException.class)
  public ResponseEntity<ErrorVM> processException(TaskNameAlreadyExistException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(TASK_ALREADY_EXIST,
				accessor.getMessage(TASK_ALREADY_EXIST, params));
		return builder.body(errorVM);
  }
  
  @ExceptionHandler(ProjectStartAndEndDateBetweenPoDateException.class)
  public ResponseEntity<ErrorVM> processException(ProjectStartAndEndDateBetweenPoDateException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(PO_DATE_BETWEEN_PROJECT_START_END_DATE,
				accessor.getMessage(PO_DATE_BETWEEN_PROJECT_START_END_DATE, params));
		return builder.body(errorVM);
  }

  @ExceptionHandler(BillProfileAlreadyMappedException.class)
  public ResponseEntity<ErrorVM> processException(BillProfileAlreadyMappedException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(BILLPROFILE_ALREADY_EXIST,
				accessor.getMessage(BILLPROFILE_ALREADY_EXIST, params));
		return builder.body(errorVM);
  }
  
  @ExceptionHandler(BillProfileNotExpiredException.class)
  public ResponseEntity<ErrorVM> processException(BillProfileNotExpiredException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(BILLPROFILE_INACTIVE_EXIST,
				accessor.getMessage(BILLPROFILE_INACTIVE_EXIST, params));
		return builder.body(errorVM);
  }
  
  @ExceptionHandler(ContractorIdAlreadyExistException.class)
  public ResponseEntity<ErrorVM> processException(ContractorIdAlreadyExistException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(CONTRACTORID_ALREDADY_EXIST,
				accessor.getMessage(CONTRACTORID_ALREDADY_EXIST, params));
		return builder.body(errorVM);
  }
  
  @ExceptionHandler(ContractorEmailIdAlreadyExistException.class)
  public ResponseEntity<ErrorVM> processException(ContractorEmailIdAlreadyExistException ex) {
    Object[] params = new Object[]{ex.getMessage()};
		BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
		ErrorVM errorVM = new ErrorVM(CONTRACTOREMAILID_ALREDADY_EXIST,
				accessor.getMessage(CONTRACTOREMAILID_ALREDADY_EXIST, params));
		return builder.body(errorVM);
  }
  
  
  @ExceptionHandler(ProfileStartDateGreaterThanJoiningDateException.class)
  public ResponseEntity<ErrorVM> processException(ProfileStartDateGreaterThanJoiningDateException ex) {
        BodyBuilder builder = ResponseEntity.status(HttpStatus.BAD_REQUEST);
        ErrorVM errorVM = new ErrorVM(PROF_START_DATE_EXCEP,ex.getMessage());
        return builder.body(errorVM);
  }
}
