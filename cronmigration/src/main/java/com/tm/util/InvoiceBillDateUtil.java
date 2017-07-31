package com.tm.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class InvoiceBillDateUtil {

	public static final String START_ON_FIRST = "Start on First";
	public static final String START_ON_SECOND = "Start on Second";
	public static final String START_ON_THIRD = "Start on Third";
	public static final String START_ON_FOURTH = "Start on Fourth";
	public static final String START_ON_LAST = "Start on Last";

	public static final String SUN = "Sun";
	public static final String MON = "Mon";
	public static final String TUE = "Tue";
	public static final String WED = "Wed";
	public static final String THU = "Thu";
	public static final String FRI = "Fri";
	public static final String SAT = "Sat";

	/*public static void main(String[] args) {
	
		LocalDate today = LocalDate.of(2017, 2, 1);
		LocalDate firstMonthHalf=LocalDate.of(today.getYear(), today.getMonth(), 15);
		LocalDate bDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.TUESDAY));
		System.out.println("bDate : "+bDate);
		LocalDate billDate=null;
		
		if(firstMonthHalf.getDayOfMonth()>=today.getDayOfMonth()){
			billDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.TUESDAY));
		}else{
			billDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.TUESDAY));
			System.out.println("thisPast "+billDate);
		}
		
		LocalDate jd = LocalDate.of(2016, 2, 1);
		LocalDate halfMonth = LocalDate.of(jd.getYear(), jd.getMonth(), jd.lengthOfMonth() / 2);
		LocalDate monthEnd = LocalDate.of(jd.getYear(), jd.getMonth(), jd.lengthOfMonth());
		System.out.println("halfMonth  "+halfMonth); // 29
		System.out.println("today :"+today);
		System.out.println("monthEnd "+monthEnd);
		System.out.println("firstMonthHalf :"+firstMonthHalf);
		System.out.println("thisPast "+billDate);
		}
		
	public long getDaysCountBetweenDates(LocalDate dateBefore, LocalDate dateAfter) {
	    return ChronoUnit.DAYS.between(dateBefore, dateAfter);
	}*/
		
	public static LocalDate getRegularOnMonthly(String billCycle, String billCycleDay, LocalDate runDate) {
		LocalDate billDate = null;
		DayOfWeek dayOfWeek = null;
		if (StringUtils.isNotBlank(billCycle)) {
			dayOfWeek = convertDaytoDayOfWeek(billCycleDay);
			if (START_ON_FIRST.equalsIgnoreCase(billCycle)) {
				billDate = runDate.with(TemporalAdjusters.firstInMonth(dayOfWeek));
			} else if (START_ON_SECOND.equalsIgnoreCase(billCycle)) {
				billDate = runDate.with(TemporalAdjusters.dayOfWeekInMonth(2, dayOfWeek));
			} else if (START_ON_THIRD.equalsIgnoreCase(billCycle)) {
				billDate = runDate.with(TemporalAdjusters.dayOfWeekInMonth(3, dayOfWeek));
			} else if (START_ON_FOURTH.equalsIgnoreCase(billCycle)) {
				billDate = runDate.with(TemporalAdjusters.dayOfWeekInMonth(4, dayOfWeek));
			} else if (START_ON_LAST.equalsIgnoreCase(billCycle)) {
				billDate = runDate.with(TemporalAdjusters.lastInMonth(DayOfWeek.MONDAY));
			}
		}
		return billDate;
	}

	public static Set<LocalDate> getListRegularOnMonthly(String billCycle, String billCycleDay, LocalDate startDate,
			LocalDate endDate) {
		Set<LocalDate> allBillDate = new LinkedHashSet<>();
		List<LocalDate> allDate = new DateRange(startDate, endDate).toList();
		allDate.stream().forEach(date -> allBillDate.add(getRegularOnMonthly(billCycle, billCycleDay, date)));
		return allBillDate;
	}

	private static DayOfWeek convertDaytoDayOfWeek(String billCycleDay) {
		DayOfWeek dayOfWeekFormat = null;
		if (SUN.equalsIgnoreCase(billCycleDay)) {
			dayOfWeekFormat = DayOfWeek.SUNDAY;
		} else if (MON.equalsIgnoreCase(billCycleDay)) {
			dayOfWeekFormat = DayOfWeek.MONDAY;
		} else if (TUE.equalsIgnoreCase(billCycleDay)) {
			dayOfWeekFormat = DayOfWeek.TUESDAY;
		} else if (WED.equalsIgnoreCase(billCycleDay)) {
			dayOfWeekFormat = DayOfWeek.WEDNESDAY;
		} else if (THU.equalsIgnoreCase(billCycleDay)) {
			dayOfWeekFormat = DayOfWeek.THURSDAY;
		} else if (FRI.equalsIgnoreCase(billCycleDay)) {
			dayOfWeekFormat = DayOfWeek.FRIDAY;
		} else if (SAT.equalsIgnoreCase(billCycleDay)) {
			dayOfWeekFormat = DayOfWeek.SATURDAY;
		}
		return dayOfWeekFormat;
	}

	public static LocalDate getRegularOnWeekly(String billCycleDay, LocalDate runDate) {
		DayOfWeek dayOfWeek = convertDaytoDayOfWeek(billCycleDay);
		return runDate.with(TemporalAdjusters.previousOrSame(dayOfWeek));
	}

	public static Set<LocalDate> getListRegularOnWeekly(String billCycleDay, LocalDate startDate, LocalDate endDate) {
		Set<LocalDate> allBillDate = new LinkedHashSet<>();
		DayOfWeek dayOfWeek = convertDaytoDayOfWeek(billCycleDay);
		List<LocalDate> allDate = new DateRange(startDate, endDate).toList();
		allDate.stream().forEach(date -> allBillDate.add(date.with(TemporalAdjusters.previousOrSame(dayOfWeek))));
		return allBillDate;
	}

	public static LocalDate getRegularOnSemiMonthly(String billCycleDay, LocalDate runDate) {
		DayOfWeek dayOfWeek = convertDaytoDayOfWeek(billCycleDay);
		return runDate.with(TemporalAdjusters.previousOrSame(dayOfWeek));
	}

	public static Set<LocalDate> getListRegularOnSemiMonthly(String billCycleDay, LocalDate startDate,
			LocalDate endDate) {
		Set<LocalDate> allBillDate = new LinkedHashSet<>();
		DayOfWeek dayOfWeek = convertDaytoDayOfWeek(billCycleDay);
		List<LocalDate> allDate = new DateRange(startDate, endDate).toList();
		allDate.stream().forEach(date -> allBillDate.add(date.with(TemporalAdjusters.previousOrSame(dayOfWeek))));
		return allBillDate;
	}

}
