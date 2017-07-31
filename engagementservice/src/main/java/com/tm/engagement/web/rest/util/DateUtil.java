package com.tm.engagement.web.rest.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;

import com.tm.engagement.exception.CustomerBadRequestException;
import com.tm.engagement.exception.CustomerProfileException;

public class DateUtil {

	private static final String INVALID_DATE_FORMAT = "Invalid date format";
	private static final String INVALID_DATE_RANGE = "Invalid Date Range";
	private static final String FROM_DATE_IS_REQUIRED = "From Date is Required";
	private static final String TO_DATE_IS_REQUIRED = "To Date is Required";

	private static final String UI_DATE_FORMAT_REQUEST = "MM/dd/yyyy";

	public static void validateStartDateAndEndDate(String startDate, String endDate) {
		notNullCheckStartAndEndDate(startDate, endDate);
		Date starDateObj = convertStringToDate(startDate);
		Date endDateObj = convertStringToDate(endDate);
		validateStartDateAndEndDate(starDateObj, endDateObj);
	}

	public static void validateStartDateAndEndDate(Date startDate, Date endDate) {
		if (startDate.compareTo(endDate) > 0) {
			throw new CustomerBadRequestException(INVALID_DATE_RANGE);
		}
	}

	public static void notNullCheckStartAndEndDate(String startDate, String endDate) {
		if (Objects.isNull(startDate))
			throw new CustomerBadRequestException(FROM_DATE_IS_REQUIRED);
		if (Objects.isNull(endDate))
			throw new CustomerBadRequestException(TO_DATE_IS_REQUIRED);
	}

	public static void validateDate(String date) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(UI_DATE_FORMAT_REQUEST);
			LocalDate.parse(date, formatter);
		} catch (DateTimeParseException e) {
			throw new CustomerProfileException(INVALID_DATE_FORMAT, e);
		}

	}

	/**
	 * This method used to convert String to return Date. Ex : String date
	 * format MM/dd/yyyy
	 * 
	 * @param date
	 * @return
	 */
	public static Date convertStringToDate(String date) {
		try {
			validateDate(date);
			DateFormat formatter = new SimpleDateFormat(UI_DATE_FORMAT_REQUEST);
			Date aLD;
			formatter.setLenient(false);
			aLD = formatter.parse(date);
			return aLD;
		} catch (Exception e) {
			throw new CustomerProfileException(INVALID_DATE_FORMAT, e);
		}
	}

	public static LocalDate convertStringToISODate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(UI_DATE_FORMAT_REQUEST);
		return LocalDate.parse(date, formatter);
	}

	public static String parseWordDateFromString(Date dateAsString) {
		DateFormat df = new SimpleDateFormat("MMM d, yyyy");
		String convertDateString = null;
		if (dateAsString != null) {
			convertDateString = df.format(dateAsString);
		}
		return convertDateString;
	}

	public static String parseWordFromDateToString(Date dateAsString) {
		DateFormat df = new SimpleDateFormat(UI_DATE_FORMAT_REQUEST);
		String convertDateString = null;
		if (dateAsString != null) {
			convertDateString = df.format(dateAsString);
		}
		return convertDateString;
	}

	public static Date checkconvertStringToISODate(String date) {
		DateFormat df = new SimpleDateFormat(UI_DATE_FORMAT_REQUEST);
		Date convertUtilDate = null;
		if (StringUtils.isNotBlank(date)) {
			try {
				convertUtilDate = df.parse(date);
			} catch (ParseException e) {
				throw new CustomerProfileException(INVALID_DATE_FORMAT);
			}
		}
		return convertUtilDate;
	}

	public static Date checkconvertStringToDate(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return df.parse(date);
		} catch (ParseException e) {
			throw new CustomerProfileException(INVALID_DATE_FORMAT);
		}
	}
	
	public static String parseDateWithTime(Date dateAsString) {
		DateFormat df = new SimpleDateFormat("MMM d, yyyy hh:mm:ss aaa");
		String convertDateString = null;
		if (dateAsString != null) {
			convertDateString = df.format(dateAsString);
		}
		return convertDateString;
	}

}
