package com.tm.timesheetgeneration.domain.enums;

public enum DayOfWeekEnum {
	
    Monday("Monday"), Tuesday("Tuesday"), Wednesday("Wednesday"), Thursday("Thursday"), Friday("Friday"), Saturday("Saturday"), Sunday("Sunday");
	
	private String value;
	
	/**
	 * @param value
	 */
	private DayOfWeekEnum(String value) {
		this.value = value;
	}
	 
	/**
	 * @return value
	 */
	public String value() {
		return this.value;
	}

}
