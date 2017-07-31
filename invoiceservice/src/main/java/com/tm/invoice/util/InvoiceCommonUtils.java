package com.tm.invoice.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.validation.ConstraintViolation;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.exception.ApplicationException;
import com.tm.commonapi.exception.BusinessException;
import com.tm.commonapi.exception.ValidationException;
import com.tm.commonapi.web.core.data.BaseDTO;
import com.tm.commonapi.web.core.data.BaseEntity;
import com.tm.invoice.constants.ApplicationConstants;

@Component
public class InvoiceCommonUtils {

	private static final Logger log = LoggerFactory
			.getLogger(InvoiceCommonUtils.class);
	
	/* Report Constants */
	public static final String PDF = "pdf";
	public static final String XLS = "xls";
	public static final String XLSX = "xlsx";
	public static final String CSV = "csv";
	public static final String FILENAME = "fileName";
	public static final String ACTUAL_FILENAME = "actualFileName";
	public static final String ATTACHMENT = "attachment";
	public static final String CACHE_CONTROL = "must-revalidate, post-check=0, pre-check=0";
	public static final String APPLICATION_PDF = "application/pdf";
	public static final String APPLICATION_CSV = "text/csv";
	public static final String APPLICATION_EXCEL = "application/vnd.ms-excel";
	public static final String APPLICATION_SPREAD_SHEET = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	/* Invoice Report */
	public static final String INVOICE_REPORT_ALREADY_EXISTS = "Invoice Report Name already Exists";
	public static final String ALL_REPORT = "Invoice_Report";

	public static final String DATE_FORMAT = "MM/dd/yyyy";
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(
			DATE_FORMAT);
	public static final String DEFAULT_AMOUNT = "0.00";

	public static final String EEE_MMM_DD_YYYY_HH_MM = "MMM dd, yyyy - hh:mm:ss a";
	private static final String INVOICE_NOTES_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";

	public static final ObjectMapper objectMapper;

	private static LocalValidatorFactoryBean validator;

	static {
		objectMapper = new ObjectMapper();
		validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();
	}

	public static <T extends BaseEntity> T updateEntity(T actual,
			ObjectNode updater) {
		T updatedEntity = null;
		ObjectReader updateReader = objectMapper.readerForUpdating(actual);
		try {
			updatedEntity = updateReader.readValue(objectMapper
					.writeValueAsString(updater));
		} catch (IOException ioe) {
			throw new ApplicationException(
					"An IO exception occured while constructing updated object.",
					ioe);
		}
		if (updatedEntity == null) {
			throw new ApplicationException(
					"An exception occured while constructing updated object.");
		}
		return updatedEntity;
	}

	public static <T extends BaseDTO> void validate(T baseDTO) {
		Set<ConstraintViolation<BaseDTO>> violations = validator
				.validate(baseDTO);
		for (ConstraintViolation<BaseDTO> violation : violations) {
			throw new ValidationException(violation.getMessage());
		}
	}

	public static <T extends BaseDTO> T validateForPartialUpdate(
			ObjectNode objectNode, Class<T> type) {
		BaseDTO baseDTO = null;
		try {
			baseDTO = objectMapper.readValue(
					objectMapper.writeValueAsString(objectNode), type);
		} catch (IOException ex) {
			throw new ApplicationException(
					"An exception occurred while instantiating value object.",
					ex);
		}
		Set<ConstraintViolation<BaseDTO>> violations = validator
				.validate(baseDTO);
		for (ConstraintViolation<BaseDTO> violation : violations) {
			try {
				Field field = baseDTO.getClass().getDeclaredField(
						((PathImpl) violation.getPropertyPath()).getLeafNode()
								.getName());
				if (objectNode.findValue(field.getName()) != null) {
					throw new ValidationException(violation.getMessage());
				}
			} catch (NoSuchFieldException nsfe) {
				throw new ApplicationException(
						"An exception occured while validating updated object.",
						nsfe);
			}
		}
		if (type.isInstance(baseDTO)) {
			return type.cast(baseDTO);
		}
		if (baseDTO == null) {
			throw new ApplicationException(
					"An exception occurred while instantiating updated object.");
		}
		return null;
	}

	public static ObjectNode getResponseObjectNode(String message,
			Boolean success) {
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.put("timestamp", System.currentTimeMillis());
		objectNode.put("message", message);
		objectNode.put("status", success ? ApplicationConstants.STATUS_OK
				: ApplicationConstants.STATUS_ERROR);
		return objectNode;
	}

	public static ObjectNode getResponseObjectNode(String message,
			Boolean success, Boolean unAuthorized) {
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.put("timestamp", System.currentTimeMillis());
		objectNode.put("message", message);
		objectNode.put("status",
				unAuthorized ? ApplicationConstants.STATUS_UNAUTHORIZED
						: ApplicationConstants.STATUS_ERROR);
		return objectNode;
	}

	/*
	 * public static ObjectNode getValidationErrorResponseObjectNode(String
	 * message, BindingResult errors) { ObjectNode objectNode =
	 * getResponseObjectNode(message, false); if (errors.hasGlobalErrors()) {
	 * objectNode.putPOJO("GlobalValidationErrors",
	 * getListOfObjectNodesFromObjectError(errors.getGlobalErrors(),
	 * "GlobalValidationError")); } if (errors.hasFieldErrors()) {
	 * objectNode.putPOJO("FieldValidationErrors",
	 * getListOfObjectNodesFromObjectError(errors.getFieldErrors(),
	 * "FieldValidationError")); } return objectNode; }
	 */

	@SuppressWarnings("unused")
	private static List<ObjectNode> getListOfObjectNodesFromObjectError(
			List<? extends ObjectError> inputList, String keyPrefix) {
		List<ObjectNode> objectNodeList = new ArrayList<>();
		for (ObjectError objectError : inputList) {
			ObjectNode objectNode = objectMapper.createObjectNode();
			objectNode.put(keyPrefix, objectError.getDefaultMessage());
			objectNodeList.add(objectNode);
		}
		return objectNodeList;
	}

	/*public static <T extends BaseAuditableEntity> void populateAuditDetailsForSave(
			T baseAuditableEntity) {
		MySQLAuditDetails mySQLAuditDetails = new MySQLAuditDetails();
		Date currentDate = new Date(System.currentTimeMillis());
		mySQLAuditDetails.setCreatedBy(0L);
		mySQLAuditDetails.setUpdatedBy(0L);
		mySQLAuditDetails.setCreatedOn(currentDate);
		mySQLAuditDetails.setUpdatedOn(currentDate);
		baseAuditableEntity.setAuditDetails(mySQLAuditDetails);
	}

	public static <T extends BaseAuditableEntity> void populateAuditDetailsForUpdate(
			T baseAuditableEntity) {
		MySQLAuditDetails mySQLAuditDetails = (MySQLAuditDetails) baseAuditableEntity
				.getAuditDetails();
		Date currentDate = new Date(System.currentTimeMillis());
		mySQLAuditDetails.setUpdatedBy(0L);
		mySQLAuditDetails.setUpdatedOn(currentDate);
		baseAuditableEntity.setAuditDetails(mySQLAuditDetails);

	}*/

	public static String convertCurrentTimeToESTFormate()
			throws ApplicationException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"MM/dd/yyy h:mm:ss a");
		return dateFormat.format(System.currentTimeMillis());
	}

	public static void compareDate(String fromDate, String toDate)
			throws ParseException {
		Date date1 = SIMPLE_DATE_FORMAT.parse(fromDate);
		Date date2 = SIMPLE_DATE_FORMAT.parse(toDate);
		if (date1.after(date2)) {
			throw new BusinessException("From date less than to To date");
		}
	}

	public static String convertDateFormatForScreenshots(Date date)
			 {
		SimpleDateFormat dateformater = new SimpleDateFormat(
				EEE_MMM_DD_YYYY_HH_MM);
		return dateformater.format(date);
	}

	public static Date convertStringToDate(String date) {
		DateFormat format = new SimpleDateFormat(
				InvoiceConstants.DATE_FORMAT_OF_MMDDYYY);
		try {
			return format.parse(date);
		} catch (ParseException e) {

		}
		return null;
	}
	
	public static String convertDateToString(Date date) {
		SimpleDateFormat dateformater = new SimpleDateFormat(
				InvoiceConstants.DATE_FORMAT_OF_MMDDYYY);
			return dateformater.format(date);
	}

	public static String roundOfValue(Double value) {
		DecimalFormat decim = new DecimalFormat(DEFAULT_AMOUNT);
		return decim.format(value);
	}

	public static String getFormattedDate(Date startDate) {
		SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT);
		return fmt.format(startDate);
	}

	public static byte[] convertPDFToImage(byte[] pdfContent)
			throws IOException {
		PDDocument document = PDDocument.load(pdfContent);
		PDFRenderer renderer = new PDFRenderer(document);
		BufferedImage image = renderer.renderImage(0);
		return convertBufferedImageToByteArray(image);
	}

	public static byte[] convertBufferedImageToByteArray(BufferedImage image) {
		byte[] imageInByte = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
		} catch (IOException ex) {
			log.error("Error while convertBufferedImageToByteArray() :: "+ex);
		}
		return imageInByte;
	}
	
    public static java.sql.Date getCurrentDate() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            Date currentDate = dateFormat.parse(dateFormat.format(date));
            java.sql.Date currentSqlDate = new java.sql.Date(currentDate.getTime());
            return currentSqlDate;
        } catch (Exception e) {
            throw new BusinessException("Date Parse Exception");
        }
    }

	public static String convertInvoiceNotesDateFormat(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				INVOICE_NOTES_DATE_FORMAT);
		return dateFormat.format(date);
	}

    public static String generateRandomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

	private InvoiceCommonUtils() {
		
	}
}