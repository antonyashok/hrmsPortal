package com.tm.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class TimesheetCalculationUtil {

	private static final String DOUBLE_ZERO = "0.00";
	
	private TimesheetCalculationUtil() {}
	
    public static BigDecimal calcWorkHrs(String startTime, String endTime) {

        if (null != startTime && startTime.equals(DOUBLE_ZERO)
                || null != endTime && endTime.equals(DOUBLE_ZERO)) {
            return BigDecimal.ZERO;
        } else {
            if (null != startTime && null != endTime) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime time1 = LocalTime.parse(startTime, format);
                LocalTime time2 = LocalTime.parse(endTime, format);
                Duration duration = Duration.between(time1, time2);
                MathContext mc = new MathContext(3);
                return BigDecimal.valueOf(duration.toMinutes() / 60.00).round(mc);
            }
        }
        return BigDecimal.ZERO;
    }

      
    public static BigDecimal calcWorkHrs(BigDecimal totalHours, BigDecimal breakHours) {
        MathContext mc = new MathContext(3);
        return (totalHours.subtract(breakHours)).round(mc);
    }

    public static BigDecimal calcWorkHrs(String startTime, String endTime, String brkStartTime,
            String brkEndTime) {
        if (null != startTime && startTime.equals(DOUBLE_ZERO)
                || null != endTime && endTime.equals(DOUBLE_ZERO)) {
            return BigDecimal.ZERO;
        } else {
            if (null != startTime && null != endTime) {
                DateTimeFormatter format = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
                LocalTime time1 = LocalTime.parse(startTime, format);
                LocalTime time2 = LocalTime.parse(endTime, format);
                LocalTime time3 = LocalTime.parse(brkStartTime, format);
                LocalTime time4 = LocalTime.parse(brkEndTime, format);
                Duration duration1 = Duration.between(time1, time2);
                Duration duration2 = Duration.between(time3, time4);

                long totalWorkHours = duration1.toMinutes();
                long breakHours = duration2.toMinutes();
                MathContext mc = new MathContext(3);
                return BigDecimal.valueOf((totalWorkHours - breakHours) / 60.00).round(mc);                
            }
        }
        return BigDecimal.ZERO;
    }

    public static boolean isExceedMaxCapHours(BigDecimal maxCapHours, BigDecimal actualWorkHours) {
        if (maxCapHours.compareTo(BigDecimal.ZERO) != 0 && actualWorkHours.compareTo(maxCapHours) > 0) {
            return true;
        }
        return false;
    }
    
    public static boolean isNegative(BigDecimal b) {
       return b.signum() == -1;
    }
}
