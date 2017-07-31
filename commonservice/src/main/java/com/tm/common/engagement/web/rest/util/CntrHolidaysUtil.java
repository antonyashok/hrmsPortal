package com.tm.common.engagement.web.rest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tm.common.engagement.domain.CntrHolidays;

public class CntrHolidaysUtil {

	private static final Logger log = LoggerFactory.getLogger(CntrHolidaysUtil.class);
	
	public List<CntrHolidays> getHolidayList(List<CntrHolidays> holidaysList, String startDate, String endDate) {
		List<CntrHolidays> holidayList = new ArrayList<>();
		for (CntrHolidays holidays : holidaysList) {
			log.info(""+holidays.getHolidayDate());
			Boolean isexists = compareDates(startDate, endDate, holidays.getHolidayDate());
			if (isexists) {
				holidayList.add(holidays);
			}
		}
		return holidaysList;
	}

	public static Boolean compareDates(String weekStartDate, String weekEndDate, Date holidayDate) {
		boolean dateTrue = false;
		java.util.Date startDate = null;
		java.util.Date endDate = null;
		if (null != weekStartDate && null != weekEndDate) {
			try {
				startDate = convertToDate(weekStartDate);
				endDate = convertToDate(weekEndDate);
			} catch (ParseException e) {
			}
		}
		if (null != holidayDate && (holidayDate.before(startDate))
				|| (holidayDate.after(startDate) && holidayDate.before(endDate))) {
			return !dateTrue;
		}
		return dateTrue;
	}

	public static Date convertToDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		return sdf.parse(date);
	}

	public static String convertToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		return sdf.format(date);
	}

	public static Date convertToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate convertToLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static String convertToUtilDate(String date) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date tempDate = simpleDateFormat.parse(date);
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		return outputDateFormat.format(tempDate);
	}
}
