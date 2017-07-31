package com.tm.commonapi.web.rest.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tm.commonapi.constants.ApplicationConstants;
import com.tm.commonapi.core.audit.mysql.BaseAuditableEntity;
import com.tm.commonapi.core.audit.mysql.MySQLAuditDetails;
import com.tm.commonapi.exception.ApplicationException;
import com.tm.commonapi.exception.ValidationException;
import com.tm.commonapi.web.core.data.BaseDTO;
import com.tm.commonapi.web.core.data.BaseEntity;

@Component
public class CommonUtils {

	private static final String STATUS = "status";

	private CommonUtils() {

	}

	public static final ObjectMapper objectMapper;

	private static final Logger log = LoggerFactory
			.getLogger(CommonUtils.class);

	private static LocalValidatorFactoryBean validator;

	private static final String EEE_MMM_DD_YYYY_HH_MM = "MMM dd, yyyy - hh:mm:ss a";
	private static final String DATE_FORMAT_OF_MMDDYYY = "MM/dd/yyyy";
	private static final String DATE_FORMAT_OF_MMDDYY = "MM/dd/yy";
	private static final String DATE_TIME_FORMAT = "MMM dd, yyyy' - 'HH:mm:ss a";
	private static final String DATE_FORMAT = "MMM dd, yyyy";
	private static final String DEFAULT_HOUR = "0.00";
	private static final String UI_DATE_FORMAT_REQUEST = "MM/dd/yyyy";
	private static final String DATE_FORMAT_OF_EEEEMMMDDYYYY = "EEEE -MMM dd, yyyy";
	private static final String DATE_FORMAT_OF_MMDDYYYYHH = "MM-dd-yyyy hh:mm:ss";
	private static final String DATE_FORMAT_OF_MMMDDYYYY = "MMM dd,yyyy";
	private static final String DATE_FORMAT_OF_MMDD = "MM/dd";
	private static final String DATE_FORMAT_OF_E = "E";

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
		objectNode.put(CommonUtils.STATUS,
				success ? ApplicationConstants.STATUS_OK
						: ApplicationConstants.STATUS_ERROR);
		return objectNode;
	}

	public static ObjectNode getResponseObjectNode(String message,
			Boolean success, Boolean unAuthorized) {
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.put("timestamp", System.currentTimeMillis());
		objectNode.put("message", message);
		objectNode.put(CommonUtils.STATUS,
				unAuthorized ? ApplicationConstants.STATUS_UNAUTHORIZED
						: ApplicationConstants.STATUS_ERROR);
		return objectNode;
	}

	public static ObjectNode getValidationErrorResponseObjectNode(
			String message, BindingResult errors) {
		ObjectNode objectNode = getResponseObjectNode(message, false);
		if (errors.hasGlobalErrors()) {
			objectNode.putPOJO(
					"GlobalValidationErrors",
					getListOfObjectNodesFromObjectError(
							errors.getGlobalErrors(), "GlobalValidationError"));
		}
		if (errors.hasFieldErrors()) {
			objectNode.putPOJO(
					"FieldValidationErrors",
					getListOfObjectNodesFromObjectError(
							errors.getFieldErrors(), "FieldValidationError"));
		}
		return objectNode;
	}

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

	public static <T extends BaseAuditableEntity> void populateAuditDetailsForSave(
			T baseAuditableEntity) {
		MySQLAuditDetails mySQLAuditDetails = new MySQLAuditDetails();
		Date currentDate = new Date(System.currentTimeMillis());
		mySQLAuditDetails.setCreatedBy(1L);
		mySQLAuditDetails.setUpdatedBy(1L);
		mySQLAuditDetails.setCreateDate(currentDate);
		mySQLAuditDetails.setLastUpdateDate(currentDate);
		baseAuditableEntity.setAuditDetails(mySQLAuditDetails);
	}

	public static <T extends BaseAuditableEntity> void populateAuditDetailsForCreate(
			T baseAuditableEntity) {
		MySQLAuditDetails mySQLAuditDetails = (MySQLAuditDetails) baseAuditableEntity
				.getAuditDetils();
		Date currentDate = new Date(System.currentTimeMillis());
		mySQLAuditDetails.setCreatedBy(1L);
		mySQLAuditDetails.setCreateDate(currentDate);
		baseAuditableEntity.setAuditDetails(mySQLAuditDetails);
	}

	public static <T extends BaseAuditableEntity> void populateAuditDetailsForUpdate(
			T baseAuditableEntity) {
		MySQLAuditDetails mySQLAuditDetails = (MySQLAuditDetails) baseAuditableEntity
				.getAuditDetils();
		Date currentDate = new Date(System.currentTimeMillis());
		mySQLAuditDetails.setUpdatedBy(1L);
		mySQLAuditDetails.setLastUpdateDate(currentDate);
		baseAuditableEntity.setAuditDetails(mySQLAuditDetails);
	}

	public static boolean isValidEmail(String email) {
		return EmailValidator.getInstance().isValid(email);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String prepareErrorResponse(String message) {

		String jsonResponse = null;

		Map<String, Object> responseMap = new HashMap();
		responseMap.put(CommonUtils.STATUS, "error");
		responseMap.put("errorMessage", message);

		try {
			jsonResponse = new ObjectMapper().writeValueAsString(responseMap);
		} catch (JsonProcessingException e) {
			log.error("Error while prepareErrorResponse() :: " + e);
			throw new ApplicationException(e.getMessage());
		}

		return jsonResponse;
	}

	public static String convertObjectToJsonString(Object input)
			throws JsonGenerationException, JsonMappingException, IOException {
		return (new ObjectMapper()).writeValueAsString(input);
	}

	public static <T extends BaseAuditableEntity> void populateAuditDetailsForSaveAndUpdate(
			T baseAuditableEntity, Long createdBy, Long updatedBy) {
		MySQLAuditDetails mySQLAuditDetails = new MySQLAuditDetails();
		Date currentDate = new Date(System.currentTimeMillis());
		mySQLAuditDetails.setCreatedBy(createdBy);
		mySQLAuditDetails.setUpdatedBy(updatedBy);
		mySQLAuditDetails.setCreateDate(currentDate);
		mySQLAuditDetails.setLastUpdateDate(currentDate);
		baseAuditableEntity.setAuditDetails(mySQLAuditDetails);
	}

	public static Date getCurrentTimeToESTFormate() {
		try {
			Calendar currentdate = Calendar.getInstance();
			String strdate = null;
			DateFormat formatter = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
			strdate = formatter.format(currentdate.getTime());
			TimeZone obj = TimeZone.getTimeZone("EST");
			formatter.setTimeZone(obj);
			return formatter.parse(strdate);
		} catch (Exception e) {
			log.error("Error while getCurrentTimeToESTFormate() :: " + e);
		}
		return null;
	}

	public static void inputStreamSafeClose(InputStream inputStream) {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (Exception e) {
				log.error("inputStreamSafeClose close exception ", e);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static String mergeTemplateWithData(String templateContent,
			Map<String, Object> mergeDetails) {
		String templateContentParam = templateContent;
		for (Iterator iter = mergeDetails.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			if (null != mergeDetails.get(key)) {
				templateContentParam = StringUtils.replace(
						templateContentParam, key, mergeDetails.get(key)
								.toString());
			}
		}
		return templateContentParam;
	}

	public static String getRecipientName(String recipientName) {
		StringBuilder recipientNameWithComma = new StringBuilder("");
		if (StringUtils.isNotBlank(recipientName)) {
			recipientNameWithComma.append(recipientName + ",");
		}

		return recipientNameWithComma.toString();
	}

	public static String convertDateFormatForActivity(Date date)
			throws ParseException {
		SimpleDateFormat dateformater = new SimpleDateFormat(
				EEE_MMM_DD_YYYY_HH_MM);
		return dateformater.format(date);
	}

	public static boolean isValidDateRange(Date startDate, Date endDate) {
		return (startDate.compareTo(endDate) <= 0) ? true : false;
	}

	public static Date convertStringToDate(String date) {
		DateFormat format = new SimpleDateFormat(DATE_FORMAT_OF_MMDDYYY);
		try {
			return format.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date convertDateFormat(String date) throws ParseException {
		DateFormat format = new SimpleDateFormat(DATE_FORMAT_OF_MMDDYYY);
		return format.parse(date);
	}

	public static String getFormattedDate(Date startDate) {
		SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT_OF_MMDDYYY);
		return fmt.format(startDate);
	}

	public static String getFormattedDateString(String startDate)
			throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_OF_MMDDYYYYHH);
		Date date = sdf1.parse(startDate);
		SimpleDateFormat formatter = new SimpleDateFormat(
				DATE_FORMAT_OF_MMDDYYY);
		return formatter.format(date);
	}

	public static String getStringToDateFormat(String startDateString)
			throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_OF_MMMDDYYYY);
		Date startDate = df.parse(startDateString);
		return df.format(startDate);
	}

	public static String getdayNameDateFormat(Date startDate) {
		Format formatter = new SimpleDateFormat(DATE_FORMAT_OF_EEEEMMMDDYYYY);
		return formatter.format(startDate);
	}

	public static Date convertStringToDateFormat(String startDateString) {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_OF_MMDDYYY);
		try {
			return df.parse(startDateString);
		} catch (ParseException e) {
			return null;
		}

	}

	public static String dateToRequestedFormat(Date date) {
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT_OF_MMDD);
		return formatter.format(date);
	}

	public static Date getAfterDate(Date date, int size) {
		LocalDateTime localDateTime = date.toInstant()
				.atZone(ZoneId.systemDefault()).toLocalDateTime();
		localDateTime = localDateTime.plusDays(size);
		return Date.from(localDateTime.atZone(ZoneId.systemDefault())
				.toInstant());
	}

	public static String getDayName(Date date) {
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT_OF_E);
		return formatter.format(date);
	}

	public static String getFormattedDateRange(Date startDate, Date endDate) {
		DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		return formatter.format(startDate) + " - " + formatter.format(endDate);
	}
	
	public static LocalDate convertStringToLocalDate(String date) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_OF_MMDDYYY);
	    return LocalDate.parse(date, formatter);
	}
	
	public static String convertLocalDateToString(LocalDate localDate) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_OF_MMDDYYY);
	    return formatter.format(localDate);
	}

	public static String roundOfValue(String value) {
		if (StringUtils.isEmpty(value)) {
			return "";
		} else {
			Double convertValue = Double.parseDouble(value);
			DecimalFormat decim = new DecimalFormat(DEFAULT_HOUR);
			return decim.format(convertValue);
		}
	}

	public static Date checkconvertStringToISODate(String date) {
		DateFormat df = new SimpleDateFormat(UI_DATE_FORMAT_REQUEST);
		Date convertUtilDate = null;
		if (StringUtils.isNotBlank(date)) {
			try {
				convertUtilDate = df.parse(date);
			} catch (ParseException e) {
				// throw new TimeoffException(INVALID_DATE_FORMAT);
			}
		}
		return convertUtilDate;
	}

	public static Date convertStringDate(String dateStr) {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String convertDateToString(Date date) {
		SimpleDateFormat fmt = new SimpleDateFormat(DATE_FORMAT_OF_MMDDYY);
		return fmt.format(date);
	}

	public static Date convertStringDateToUtilDate(String dateStr)
			throws ParseException {
		DateFormat format = new SimpleDateFormat(DATE_FORMAT);
		return format.parse(dateStr);
	}
	
	public static Date convertStringDateToUtilDateInDefaultFormat(String dateStr) throws ParseException {
        DateFormat format = new SimpleDateFormat(DATE_FORMAT_OF_MMDDYYY);
        return format.parse(dateStr);
    }

	public static String convertDateFormatForScreenshots(Date date)
			throws ParseException {
		SimpleDateFormat dateformater = new SimpleDateFormat(
				EEE_MMM_DD_YYYY_HH_MM);
		return dateformater.format(date);

	}

	public static String getCurrentDateWithTime(Date date) {
		DateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
		Date currentDate = date;
		return df.format(currentDate);
	}

	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static UUID stringToUUIDConversion(String timesheetId) {
		return UUID.fromString(timesheetId);

	}

	private static String defaultTimeStamp = " 00:00:00";

	public static Date parseUtilDateFormatWithDefaultTime(Date givenDate)
			throws ParseException {
		String convertDefaultTimeFormat = simpleMysqlDateFormat.get().format(
				givenDate)
				+ defaultTimeStamp;
		log.info("convertDefaultTimeFormat --->" + convertDefaultTimeFormat);
		return simpleMysqlDateFormatTime.get().parse(convertDefaultTimeFormat);
	}

	private static final ThreadLocal<SimpleDateFormat> simpleMysqlDateFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	private static final ThreadLocal<SimpleDateFormat> simpleMysqlDateFormatTime = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private static final String HOST = "host";
	private static final String CONTEXT_PATH = "context";
	private static final String LOGO_URL = "logo.url";

	public static String formLogoImageURL() {
		StringBuilder logoUrl = new StringBuilder();
		InputStream input = null;
		try {
			input = new FileInputStream("system.properties");
			Properties properties = new Properties();
			properties.load(input);
			logoUrl.append(properties.getProperty(HOST))
					.append(properties.getProperty(CONTEXT_PATH))
					.append(properties.getProperty(LOGO_URL));			
		} catch (IOException ex) {
			log.error("Error while formLogoImageURL() :: "+ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					log.error("Error while formLogoImageURL() :: "+e);
				}
			}
		}
		return logoUrl.toString();
	}

}
