package com.tm.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class DateRange implements Iterable<LocalDate> {

	private final LocalDate startDate;
	private final LocalDate endDate;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	public DateRange(String startDate, String endDate) {
		DateUtil.validateStartDateAndEndDate(startDate, endDate);
		this.startDate = LocalDate.parse(startDate, formatter);
		this.endDate = LocalDate.parse(endDate, formatter);
	}

	public DateRange(Date startDate, Date endDate) {
		this.startDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		this.endDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public DateRange(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public Iterator<LocalDate> iterator() {
		return stream().iterator();
	}

	public Stream<LocalDate> stream() {
		return Stream.iterate(startDate, d -> d.plusDays(1)).limit(ChronoUnit.DAYS.between(startDate, endDate) + 1);
	}

	public List<LocalDate> toList() {
		List<LocalDate> dates = new ArrayList<>();
		for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
			dates.add(d);
		}
		return dates;
	}
}