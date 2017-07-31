package com.tm.util;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TimesheetRecruiterUtil {

    private static final Logger log = LoggerFactory.getLogger(TimesheetRecruiterUtil.class);

    private TimesheetRecruiterUtil() {}
    
    public static Date getStartDateOfWeek() {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.setTime(new java.util.Date());
        int today = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DAY_OF_WEEK, -today - 7 + Calendar.SUNDAY);
        java.util.Date utilDate = c.getTime();
        return new java.sql.Date(utilDate.getTime());
    }

    public static Date getEndDateOfWeek() {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.setTime(new java.util.Date());
        int today = c.get(Calendar.DAY_OF_WEEK);
        c.add(Calendar.DAY_OF_WEEK, -today - 1 + Calendar.SUNDAY);
        java.util.Date utilDate = c.getTime();
        return new java.sql.Date(utilDate.getTime());
    }

    public static Date getSubmissionDate() {
        Calendar c = Calendar.getInstance();
        java.util.Date utilDate = c.getTime();
        return new java.sql.Date(utilDate.getTime());
    }

    public static Date getNextDate(String strDate, int i) {
    	
    	DateFormat fm = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        java.util.Date parsedDate = null;
        Calendar c = Calendar.getInstance();
		try {
			parsedDate = fm.parse(strDate);
			c.setTime(parsedDate);
	        c.add(Calendar.DATE, i);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        java.util.Date addedDate = c.getTime();
        return new java.sql.Date(addedDate.getTime());
    }

    public static String objToJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error in objToJson", e);
        }
        return jsonInString;
    }

    public static Boolean dateBtwnTwoDates(String oeStartDateStr, String oeEndDateStr,
            Date hldyDate) {

        boolean dateTrue = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date startDate = null;;
        java.util.Date endDate = null;
        if (null != oeStartDateStr && null != oeEndDateStr && null != hldyDate) {
            try {

                startDate = sdf.parse(oeStartDateStr);
                endDate = sdf.parse(oeEndDateStr);
            } catch (ParseException e) {
                log.error("Error in dateBtwnTwoDates", e);
            }
            Date d = new Date(hldyDate.getTime());

            String hldyDt = sdf.format(d);
            if ((d.after(startDate) && (d.before(endDate))) || (hldyDt.equals(sdf.format(startDate))
                    || hldyDt.equals(sdf.format(endDate)))) {
                return dateTrue;
            } else {
                return !dateTrue;
            }
        } else {
            return !dateTrue;
        }
    }

    public static Time convertStringToTime(String time) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.sql.Time timeValue = null;
        try {
            timeValue = new java.sql.Time(format.parse(time).getTime());
        } catch (ParseException e) {
            log.error("Error in convertStringToTime", e);
        }
        return timeValue;

    }

    public static String splitFirstNameLastName(String employeeName) {

        String[] splitRecipientName;
        String recipientNameSecondname;
        String recipientNameFirstname;
        if (null != employeeName) {
            splitRecipientName = employeeName.split(",");
            recipientNameSecondname = splitRecipientName[0];
            recipientNameFirstname = splitRecipientName[1];
            return recipientNameFirstname + " " + recipientNameSecondname;
        }
        return employeeName;
    }

}