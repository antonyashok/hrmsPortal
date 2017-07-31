package com.tm.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateConversionUtil {

	public static final String MM_DD_YYYY = "MM/dd/yyyy";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	private DateConversionUtil() {

	}

	public static Date convertToDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(MM_DD_YYYY);
		return sdf.parse(date);
	}

	public static String convertToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(MM_DD_YYYY);
		return sdf.format(date);
	}

	public static Date convertToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate convertToLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static String convertToUtilDate(String date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD);
		Date tempDate = simpleDateFormat.parse(date);
		SimpleDateFormat outputDateFormat = new SimpleDateFormat(MM_DD_YYYY);
		return outputDateFormat.format(tempDate);
	}

	public static String parseWordDateFromString(Date dateAsString) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String convertDateString = null;
		if (dateAsString != null) {
			convertDateString = df.format(dateAsString);
		}
		return convertDateString;
	}
}
