package com.tm.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.commonapi.exception.ApplicationException;
import com.tm.commonapi.exception.BusinessException;
import com.tm.timesheetgeneration.domain.enums.StatusEnum;

public class TimeSheetCommonUtils {

	private static final Logger logger = LoggerFactory.getLogger(TimeSheetCommonUtils.class);

    /**
     * @param dateStr
     * @return
     */
    public static java.sql.Date getDates(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat(TimesheetConstants.YYYYMMDD_WITH_HYPEN);
        SimpleDateFormat df1 = new SimpleDateFormat(TimesheetConstants.YYYYMMDD_WITH_DOT);
        java.sql.Date returnDate = null;
        try {
            Date date = df.parse(dateStr);
            returnDate = new java.sql.Date(df1.parse(df1.format(date)).getTime());

        } catch (ParseException e) {
            logger.info("Key", e);
        }
        return returnDate;
    }

    /**
     * @param range
     * @param start
     * @param end
     * @return
     */
    public static List<Integer> getIntegerList(List<Integer> range, Integer start, Integer end) {
        range = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        return range;
    }

    /**
     * @param d
     * @return
     */
    public static String round(BigDecimal d) {
        return String.format("%.2f", d);
    }

    /**
     * @param val
     * @return
     */
    public static Float parseFloat(Integer val) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Float.valueOf(twoDForm.format(val));
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getFormattedDateRange(Date startDate, Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        return sdf.format(startDate) + " - " + sdf.format(endDate);
    }


    /**
     * @param status
     * @return
     */
    public static String getStatus(StatusEnum status) {
        if (status.name().equalsIgnoreCase("Approved")) {
            return "Approved";
        } else if (status.name().equalsIgnoreCase("Rejected")) {
            return "Rejected";
        } else if (status.name().equalsIgnoreCase("Overdue")) {
            return "Overdue";
        } else if (status.name().equalsIgnoreCase("NotSubmitted")) {
            return "Not Submitted";
        } else if (status.name().equalsIgnoreCase("AwaitingApproval")) {
            return "Awaiting Approval";
        }

        return null;
    }

    /**
     * Purpose : to get current date
     * 
     * @param void
     * @return Date
     * @throws ParseException
     */
    public static java.sql.Date getCurrentDate() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            Date currentDate = dateFormat.parse(dateFormat.format(date));
            java.sql.Date currentSqlDate = new java.sql.Date(currentDate.getTime());
            return currentSqlDate;
        } catch (Exception e) {
            logger.info("Key", e);
            throw new BusinessException("Date Parse Exception");
        }
    }

    /**
     * to get converted date as string
     * 
     * @param date
     * @return
     */
    public static String getStringDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(TimesheetConstants.YYYYMMDD_WITH_HYPEN);
        String returnString = null;
        try {
            returnString = df.format(date);
        } catch (Exception e) {
            logger.info("Key", e);
        }
        return returnString;
    }


    /**
     * Purpose : to get current date
     * 
     * @param void
     * @return Date
     * @throws ParseException
     */
    public static Date getCreateDate(Date date) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(TimesheetConstants.YYYYMMDD_WITH_HYPEN);
            Date currentDate = dateFormat.parse(dateFormat.format(date));
            return currentDate;
        } catch (Exception e) {
            logger.info("Key", e);
            throw new BusinessException("Date Parse Exception");
        }
    }

    /**
     * @return
     * @throws ApplicationException
     */
    /*
     * public static String convertCurrentTimeToESTFormate() throws ApplicationException {
     * SimpleDateFormat dateFormat = new SimpleDateFormat(TimesheetConstants.MM_DD_YYY_HH_MM_SS_A);
     * Calendar now = Calendar.getInstance(); dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
     * return dateFormat.format(now.getTime()); }
     */
    public static String convertCurrentTimeToESTFormate() throws ApplicationException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy h:mm:ss a");
        return dateFormat.format(System.currentTimeMillis());
    }

    /**
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<java.sql.Date> getDates(String startDate, String endDate) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy.MM.dd");
        List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
        java.sql.Date fromDt = null, endDt = null;
        try {
            Date fromDate = df.parse(startDate);
            Date toDate = df.parse(endDate);
            Date startFromDate = DateUtils.addDays(fromDate, -6);
            Date endToDate = DateUtils.addDays(toDate, 6);
            fromDt = new java.sql.Date(df1.parse(df1.format(startFromDate)).getTime());
            endDt = new java.sql.Date(df1.parse(df1.format(endToDate)).getTime());
            dateList.add(fromDt);
            dateList.add(endDt);
        } catch (ParseException e) {
            throw new ApplicationException("Date parse exception" + e);
        }
        return dateList;
    }

    /**
     * @param dateStr
     * @return
     */
    public static java.sql.Date getStartDateFormate(String dateStr) {
        SimpleDateFormat dateFormatWithHyphen =
                new SimpleDateFormat(TimesheetConstants.YYYYMMDD_WITH_HYPEN);
        SimpleDateFormat dateFormatWithDot =
                new SimpleDateFormat(TimesheetConstants.YYYYMMDD_WITH_DOT);
        java.sql.Date returnDate = null;
        try {

            Date date = dateFormatWithHyphen.parse(dateStr);
            Date startFromDate = DateUtils.addDays(date, -6);
            returnDate = new java.sql.Date(
                    dateFormatWithDot.parse(dateFormatWithDot.format(startFromDate)).getTime());

        } catch (ParseException e) {
            logger.info("Key", e);
        }
        return returnDate;
    }


    /**
     * @param dateStr
     * @return
     */
    public static java.sql.Date getEndDateFormate(String dateStr) {
        SimpleDateFormat dateFormatWithHyphen =
                new SimpleDateFormat(TimesheetConstants.YYYYMMDD_WITH_HYPEN);
        SimpleDateFormat dateFormatWithDot =
                new SimpleDateFormat(TimesheetConstants.YYYYMMDD_WITH_DOT);
        java.sql.Date returnDate = null;
        try {
            Date date = dateFormatWithHyphen.parse(dateStr);
            Date endToDate = DateUtils.addDays(date, 6);
            returnDate = new java.sql.Date(
                    dateFormatWithDot.parse(dateFormatWithDot.format(endToDate)).getTime());

        } catch (ParseException e) {
            logger.info("Key", e);
        }
        return returnDate;
    }


    /**
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<java.sql.Date> getDatesStartAndEnd(String startDate, String endDate) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy.MM.dd");
        List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
        java.sql.Date fromDt = null, endDt = null;
        try {
            Date fromDate = df.parse(startDate);
            Date toDate = df.parse(endDate);
            fromDt = new java.sql.Date(df1.parse(df1.format(fromDate)).getTime());
            endDt = new java.sql.Date(df1.parse(df1.format(toDate)).getTime());
            dateList.add(fromDt);
            dateList.add(endDt);
        } catch (ParseException e) {
            throw new ApplicationException("Date parse exception" + e);
        }
        return dateList;
    }


    /**
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<java.sql.Date> getPreviousStartAndEndDate() {

        SimpleDateFormat df1 = new SimpleDateFormat("yyyy.MM.dd");
        List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
        java.sql.Date fromDt = null, endDt = null;
        try {
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            Date startFromDate = cal.getTime();
            cal.add(Calendar.DATE, -6);
            Date endToDate = cal.getTime();

            fromDt = new java.sql.Date(df1.parse(df1.format(startFromDate)).getTime());
            endDt = new java.sql.Date(df1.parse(df1.format(endToDate)).getTime());
            dateList.add(endDt);
            dateList.add(fromDt);
        } catch (ParseException e) {
            throw new ApplicationException("Date parse exception" + e);
        }
        return dateList;
    }



    /**
     * Start Previous DateOfWeek
     * 
     * @return startDate
     */
    public static Date getStartDateOfWeek() {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.setTime(new java.util.Date());
        int today = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DAY_OF_WEEK, -today - 7 + Calendar.SUNDAY);
        java.util.Date utilDate = c.getTime();
        return new java.sql.Date(utilDate.getTime());
    }

    /**
     * End Previous DateOfWeek
     * 
     * @return endDate
     */
    public static Date getEndDateOfWeek() {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.setTime(new java.util.Date());
        int today = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DAY_OF_WEEK, -today - 1 + Calendar.SUNDAY);
        java.util.Date utilDate = c.getTime();
        return new java.sql.Date(utilDate.getTime());
    }

    /**
     * Current date
     * 
     * @return
     */
    public static Date getSubmissionDate() {
        Calendar c = Calendar.getInstance();
        java.util.Date utilDate = c.getTime();
        return new java.sql.Date(utilDate.getTime());
    }

    /**
     * @param strDate
     * @param i
     * @return
     */
    public static Date getNextDate(String strDate, int i) {

        LocalDate parsedDate = LocalDate.parse(strDate);
        LocalDate addedDate = parsedDate.plusDays(i);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateNew = addedDate.toString();
        java.util.Date utilDate = new java.util.Date();

        try {
            utilDate = format.parse(dateNew);
        } catch (ParseException e) {
            logger.info("Key", e);
        }

        return new java.sql.Date(utilDate.getTime());

    }

    /**
     * Convert Object to Json Format
     * 
     * @param obj
     * @return
     */
    public static String objToJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.info("Key", e);
        }
        return jsonInString;
    }
    
    public static Time convertStringToTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        java.sql.Time timeValue = null;
        try {
            if (StringUtils.isNotBlank(time)) {
                timeValue = new java.sql.Time(format.parse(time).getTime());
            }
        } catch (ParseException e) {
        }
        return timeValue;
    }


    public static double getTotalHours(String strTime, String endTime) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        Date strDate = null;
        Date endDate = null;
        long difference = 0l;
        double diffInHours = 0;
        try {
            if (strTime != null && StringUtils.isNotBlank(strTime)) {
                strDate = format.parse(strTime);
            }
            if (endTime != null && StringUtils.isNotBlank(endTime)) {
                endDate = format.parse(endTime);
            }
            if(strDate !=null && endDate !=null){
            
            difference = endDate.getTime() - strDate.getTime();
            diffInHours = (difference / ((double) 1000 * 60 * 60)) % 60;
            }
        } catch (ParseException e) {
        	logger.error("parese exception ", e);
        }

        return diffInHours;
    }

    /**
     * to get converted date as string
     * 
     * @param date
     * @return
     */
    public static String convertCurrentTimeToESTFormate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(TimesheetConstants.MM_DD_YYY_HH_MM_SS_A);
        String returnString = null;
        try {
            if (date != null) {
                returnString = df.format(date);
            }
        } catch (Exception e) {
            logger.info("Key", e);
        }
        return returnString;
    }


    /**
     * to get converted date as string
     * 
     * @param date
     * @return
     */
    public static String convertDayOfWeekFormate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(TimesheetConstants.EEEE_MMM_DD_YYYY);
        String returnString = null;
        try {
            if (date != null) {
                returnString = df.format(date);
            }
        } catch (Exception e) {
            logger.info("Key", e);
        }
        return returnString;
    }


    public static String getDayFromDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("EEE");

        String day = null;
        try {
            if (date != null) {
                day = df.format(date);

            }
        } catch (Exception e) {
            logger.info("Key", e);
        }
        return day;

    }

    public static String getCurrentDateAndTimeForTimeZone(Date date, String timeZone,
            String dateFormat) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        Date currentDate = date;
        TimeZone obj = TimeZone.getTimeZone(timeZone);
        formatter.setTimeZone(obj);
        return formatter.format(currentDate);
    }

    public static Date getPreviousWeekStartDate() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 7);
        Date previousWeekStartDate = c.getTime();
        c.add(Calendar.DATE, 6);
        return previousWeekStartDate;
    }

    public static Date getPreviousWeekEndDate() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 7);
        c.add(Calendar.DATE, 6);
        Date previousWeekEndDate = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = sdf.format(previousWeekEndDate);
        Date stDate = null;
        try {
            stDate = sdf.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stDate;
    }
}
