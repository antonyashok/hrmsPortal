package com.tm.timesheet.service.dto;

import java.io.Serializable;

public class TimesheetDaysDTO implements Serializable {

	private static final long serialVersionUID = -7765670604010875803L;
	private String day;
	private String date;
	private String formatedDate;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFormatedDate() {
		return formatedDate;
	}

	public void setFormatedDate(String formatedDate) {
		this.formatedDate = formatedDate;
	}
	
	

}
