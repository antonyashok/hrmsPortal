package com.tm.timesheet.timeoff.web.rest.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;

import com.tm.timesheet.timeoff.exception.TimeoffBadRequestException;
import com.tm.timesheet.timeoff.exception.TimeoffException;

public class DateUtil {

	private static final String INVALID_DATE = "Invalid date";
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
			throw new TimeoffBadRequestException(INVALID_DATE_RANGE);
		}
	}

	public static void notNullCheckStartAndEndDate(String startDate, String endDate) {
		if (Objects.isNull(startDate))
			throw new TimeoffBadRequestException(FROM_DATE_IS_REQUIRED);
		if (Objects.isNull(endDate))
			throw new TimeoffBadRequestException(TO_DATE_IS_REQUIRED);
	}

	public static void validateDate(String date) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(UI_DATE_FORMAT_REQUEST);
			LocalDate.parse(date, formatter);
		} catch (DateTimeParseException e) {
			throw new TimeoffException(INVALID_DATE_FORMAT, e);
		}

	}

	public static Date convertStringToDate(String date) {
		try {
			validateDate(date);
			DateFormat formatter = new SimpleDateFormat(UI_DATE_FORMAT_REQUEST);
			Date convertDate;
			formatter.setLenient(false);
			convertDate = formatter.parse(date);
			return convertDate;
		}catch(TimeoffException e){
			throw new TimeoffException(INVALID_DATE_FORMAT, e);
		}
		catch (Exception e) {
			//throw new TimeoffException(INVALID_DATE_FORMAT, e);
			throw new TimeoffException(INVALID_DATE, e);
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
				throw new TimeoffException(INVALID_DATE_FORMAT);
			}
		}
		return convertUtilDate;
	}

	public static Date checkconvertStringToDate(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return df.parse(date);
		} catch (ParseException e) {
			throw new TimeoffException(INVALID_DATE_FORMAT);
		}
	}
	
	public static Date checkconvertStringToDate1(String date) {
		DateFormat df = new SimpleDateFormat("yyyy-dd-MM");
		try {
			return df.parse(date);
		} catch (ParseException e) {
			throw new TimeoffException(INVALID_DATE_FORMAT);
		}
	}

	public static List<Date> getWeekListDate(String startDateparam, String endDateParam) {
		List<Date> weekDates = new ArrayList<>();
		//DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat outputformatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate;
		Date endDate;
		try {
			startDate = formatter.parse(startDateparam);
			endDate = formatter.parse(endDateParam);

			long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
			long endTime = endDate.getTime(); // create your endtime here,
												// possibly using Calendar or
												// Date
			long curTime = startDate.getTime();
			while (curTime <= endTime) {
				weekDates.add(new Date(curTime));
				curTime += interval;
			}
			for (int i = 0; i < weekDates.size(); i++) {
				Date lDate = weekDates.get(i);
				outputformatter.format(lDate);
			}
		} catch (ParseException e) {
			throw new TimeoffException(INVALID_DATE_FORMAT);
		}
		return weekDates;
	}
	
	
	public static String parseDateWithTime(Date dateAsString) {
		DateFormat df = new SimpleDateFormat("MMM d, yyyy hh:mm:ss aaa");
		String convertDateString = null;
		if (dateAsString != null) {
			convertDateString = df.format(dateAsString);
		}
		return convertDateString;
	}
	
	private static String UI_DATE_FORMAT_TIMESHEET="EEEE -MMM dd, yyyy";
	
	public static String parseDateTimesheetTimeoff(Date dateAsString) {
		DateFormat df = new SimpleDateFormat(UI_DATE_FORMAT_TIMESHEET);
		String convertDateString = null;
		if (dateAsString != null) {
			convertDateString = df.format(dateAsString);
		}
		return convertDateString;
	}
	
	public static Date asDate(LocalDate localDate) {
	    return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	  }

}
