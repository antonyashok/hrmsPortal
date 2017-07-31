package com.tm.timesheetgeneration.domain.enums;

public enum PaidStatusEnum {
	N, Y;

	private String value;

	public String getValue() {
		return value;
	}

	void setValue(String value) {
		this.value = value;
	}
	
}
