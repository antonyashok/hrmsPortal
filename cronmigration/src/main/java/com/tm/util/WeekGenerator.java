package com.tm.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;

public class WeekGenerator {

	private WeekGenerator() {
	}

	/**
	 * Note: This method is used for generating the weeks in between the start
	 * date and end date.
	 * 
	 * @param startDay
	 *            (For employee,It should be Sunday. For contractor, It should
	 *            be week plan's start day). It should be fully capital letters.
	 * @param endDay
	 *            (For employee, It should be Saturday. For contractor, It
	 *            should be week plan's end day). It should be fully capital
	 *            letters.
	 * @param fromDate
	 *            (fromDate will be Application Start Date if the employee is an
	 *            old employee. fromDate will be Joining date if the employee is
	 *            joined after our application rolled out. fromDate will be
	 *            (latest entry's end date + 1) if both employee or contractor
	 *            had the entry in the timesheet already. fromDate will be
	 *            Application Start Date if the contractor is an old employee.
	 *            fromDate will be employee engagement start date if the
	 *            employee is joined after our application rolled out. toDate
	 *            will be null for employees and contractor. If the contractor
	 *            is having employee engagement end date, employee engagement
	 *            end date will be passed as toDate
	 */
	public static LinkedHashMap<Integer, Week> generateWeeks(Integer startDay, Integer endDay, LocalDate fromDate,
			LocalDate toDate) {
		DayOfWeek weekStartDay = DayOfWeek.of(startDay);
		DayOfWeek weekEndDay = DayOfWeek.of(endDay);
		if (null == toDate) {
//			ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
//			toDate = utc.toLocalDate();
			toDate = LocalDate.now();
		}
		toDate = toDate.with(TemporalAdjusters.nextOrSame(weekEndDay));
//		toDate = toDate.with(TemporalAdjusters.previousOrSame(weekEndDay));
		fromDate = fromDate.with(TemporalAdjusters.previousOrSame(weekStartDay));
		LinkedHashMap<Integer, Week> weekMap = new LinkedHashMap<>();
		Integer weekCount = 1;
		while (fromDate.isBefore(toDate)) {
			Week week = new Week();
			week.setStartDate(fromDate);
			week.setEndDate(fromDate.plusDays(6));
			weekMap.put(weekCount, week);
			weekCount += 1;
			fromDate = fromDate.plusDays(7);
		}
		return weekMap;
	}
}
