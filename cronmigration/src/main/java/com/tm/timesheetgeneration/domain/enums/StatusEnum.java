package com.tm.timesheetgeneration.domain.enums;



public enum StatusEnum {
	
	Approved("Approved"),AwaitingApproval("AwaitingApproval"),NotSubmitted("NotSubmitted"),Overdue("Overdue"),Rejected("Rejected"),All("All");
	
	private String value;
	
	/**
	 * @param value
	 */
	private StatusEnum(String value) {
		this.value = value;
	}
	 
	/**
	 * @return value
	 */
	public String value() {
		return this.value;
	}

}
