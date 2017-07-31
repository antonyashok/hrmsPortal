package com.tm.util;

import static java.time.temporal.TemporalAdjusters.previous;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tm.timesheetgeneration.exception.TimeoffBadRequestException;
import com.tm.timesheetgeneration.exception.TimeoffException;

public class DateUtil {

	private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

	private static final String INVALID_DATE = "Invalid date";
	private static final String INVALID_DATE_FORMAT = "Invalid date format";
	private static final String INVALID_DATE_RANGE = "Invalid Date Range";
	private static final String FROM_DATE_IS_REQUIRED = "From Date is Required";
	private static final String TO_DATE_IS_REQUIRED = "To Date is Required";

	private static final String UI_DATE_FORMAT_REQUEST = "MM/dd/yyyy";
//	private static SimpleDateFormat simpleMysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//	private static SimpleDateFormat simpleMysqlDateFormatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	private static SimpleDateFormat simpleMysqlLocalDateFormatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
    private static final ThreadLocal<SimpleDateFormat> simpleMysqlDateFormat = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    
    private static final ThreadLocal<SimpleDateFormat> simpleMysqlDateFormatTime = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    
    private static final ThreadLocal<SimpleDateFormat> simpleMysqlLocalDateFormatTime = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };    
    
//	private static String defaultTimeStamp = " 05:30:00";
    private static String defaultTimeStamp = " 00:00:00";

	public DateUtil() {
		
		simpleMysqlDateFormatTime.get().setTimeZone(TimeZone.getTimeZone("UTC"));
		simpleMysqlLocalDateFormatTime.get().setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	public enum day {
		Sun(1), Mon(2), Tue(3), Wed(4), Thu(5), Fri(6), Sat(7);
		private int value;

		private day(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public static void validateStartDateAndEndDate(String startDate, String endDate) {
		notNullCheckStartAndEndDate(startDate, endDate);
		Date starDateObj = convertStringToDate(startDate);
		Date endDateObj = convertStringToDate(endDate);
		validateStartDateAndEndDate(starDateObj, endDateObj);
	}

	public static Date parseUtilDateFormatWithDefaultTime(Date givenDate) throws ParseException {
		String convertDefaultTimeFormat = DateUtil.simpleMysqlDateFormat.get().format(givenDate) + defaultTimeStamp;
//		log.info("convertDefaultTimeFormat --->"+convertDefaultTimeFormat);
		return DateUtil.simpleMysqlDateFormatTime.get()
				.parse(convertDefaultTimeFormat);
	}

	public static Date parseLocalDateFormatWithDefaultTime(LocalDate givenDate) throws ParseException {
		 String convertDefaultTimeFormat = simpleMysqlDateFormat.get().format(DateConversionUtil.convertToDate(givenDate)) + defaultTimeStamp;
		return simpleMysqlLocalDateFormatTime.get()
				.parse(convertDefaultTimeFormat);
	}
	
	public static Date parseDateFormatWithDefaultTime(Date givenDate) throws ParseException {
		 String convertDefaultTimeFormat = simpleMysqlDateFormat.get().format(givenDate) + defaultTimeStamp;
		return simpleMysqlLocalDateFormatTime.get()
				.parse(convertDefaultTimeFormat);
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
		} catch (TimeoffException e) {
			throw new TimeoffException(INVALID_DATE_FORMAT, e);
		} catch (Exception e) {
			// throw new TimeoffException(INVALID_DATE_FORMAT, e);
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

	public static String parseWordDateFormatString(Date dateAsString) {
		DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
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
		// DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
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

	public static String convertDateFormatForScreenshots(Date date) {
		SimpleDateFormat dateformater = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa");
		return dateformater.format(date);
	}

	public static synchronized Week getStartAndEndDateByGivenDateAndWeekDay(Date givenDate, String startDay,
			String endDay) {
		Week week = new Week();
		Calendar cal = Calendar.getInstance();
		cal.setTime(givenDate);
		cal.set(Calendar.DAY_OF_WEEK, day.valueOf(startDay).getValue());
//		log.info("Start Date of the week " + simpleMysqlDateFormatTime.get().format(cal.getTime()));
		week.setStartDate(DateConversionUtil.convertToLocalDate(cal.getTime()));
		week.setStartUtilDate(cal.getTime());
		cal.set(Calendar.DAY_OF_WEEK, day.valueOf(endDay).getValue());
//		log.info(" End Date of the week :: " + simpleMysqlDateFormatTime.get().format(cal.getTime()));
		week.setEndUtilDate(cal.getTime());
		week.setEndDate(DateConversionUtil.convertToLocalDate(cal.getTime()));
		return week;
	}

	public static Week getStartAndEndDateByGivenDate(DayOfWeek dayofWeek) {
		Week week = new Week();
		// ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
		// LocalDate startDate =
		// utc.toLocalDate().with(previousOrSame(dayofWeek));
		LocalDate startDate = LocalDate.now().with(previousOrSame(dayofWeek));
		week.setStartDate(startDate);
		LocalDate endDate = startDate.plusDays(6);
		week.setEndDate(endDate);
		week.setWeekStartDay(startDate.getDayOfWeek().name());
		week.setWeekEndDay(endDate.getDayOfWeek().name());
		week.setStartDayOfWeek(startDate.getDayOfWeek());
//		 log.info("LocalDate : : "+new Date());
//		 log.info("getStartDate ---->"+week.getStartDate());
//		 log.info("getEndDate ---->"+week.getEndDate());
		return week;
	}

	public static Week getStartAndEndDateByGivenDateForRecruiter(DayOfWeek dayofWeek) {
		Week week = new Week();
		LocalDate startDate = LocalDate.now().with(previous(dayofWeek)).minusDays(6);
		week.setStartDate(startDate);
		LocalDate endDate = startDate.plusDays(6);
		week.setEndDate(endDate);
		week.setWeekStartDay(startDate.getDayOfWeek().name());
		week.setWeekEndDay(endDate.getDayOfWeek().name());
		week.setStartDayOfWeek(startDate.getDayOfWeek());
		return week;
	}

	public static synchronized boolean isWithinRange(Date testDate, Date startDate, Date endDate) {
		return !(testDate.before(startDate) || testDate.after(endDate) || testDate.equals(startDate) || testDate.equals(endDate));
	}
	
	public static Date addDaysToUtilDate(Date givenDate, int noOfDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(givenDate);
		cal.add(Calendar.DATE, noOfDays);
		return cal.getTime();
	}
	
	public static LocalDate addDaysToLocalDate(LocalDate givenDate, int noOfDays) {
		return givenDate.plusDays(noOfDays);
	}	
}
