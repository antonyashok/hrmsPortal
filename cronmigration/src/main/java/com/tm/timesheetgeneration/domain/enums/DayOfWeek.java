package com.tm.timesheetgeneration.domain.enums;

public enum DayOfWeek {
	MONDAY("MONDAY"), TUESDAY("TUESDAY"), WEDNESDAY("WEDNESDAY"), THURSDAY(
			"THURSDAY"), FRIDAY("FRIDAY"), SATURDAY("SATURDAY"), SUNDAY(
			"SUNDAY");

	private String value;

	private DayOfWeek(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}

}