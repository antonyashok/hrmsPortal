/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.domain.util.DateConverter.java
 * Author        : Mydeen Kasim 
 * Date Created  : Feb 15, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/

package com.tm.timesheet.configuration.domain.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateConverter {

    private static final Logger log = LoggerFactory.getLogger(DateConverter.class);

    private DateConverter() {

    }

    public static String convertDateToString(Date dbData) {
        String strDate = null;
        if (Objects.nonNull(dbData)) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            strDate = dateFormat.format(dbData);
        }
        return strDate;
    }

    public static double timeDifference(String startTime, String endTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Date startDate; 
        Date endDate;
        long diff; 
        long hours; 
        long mins;
        
        try {
            startDate = simpleDateFormat.parse(startTime);
            endDate = simpleDateFormat.parse(endTime);
            diff = endDate.getTime() - startDate.getTime();
            hours = diff / (1000 * 60 * 60);
            mins = diff % (1000 * 60 * 60);
            return Double
                    .parseDouble(new StringBuilder().append(hours).append(".").append(mins).toString());
        } catch (ParseException e) {
            return 0.0;
        }
    }


    public static Date convertStringToDate(String startDateString) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = null;
        try {
            startDate = df.parse(startDateString);
        } catch (ParseException e) {
            log.error("Parse Exception", e);
        }
        return startDate;
    }
    
}
