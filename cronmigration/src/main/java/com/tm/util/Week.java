package com.tm.util;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.tm.commonapi.web.rest.util.ResourceUtil;

public class Week implements Serializable {

	private static final long serialVersionUID = -93840821644799972L;

	private LocalDate startDate;
	private LocalDate endDate;
	private Date startUtilDate;
	private Date endUtilDate;
	private String weekStartDay;
	private String weekEndDay;
	private Date applicationLiveDate;
	private String process;
	private DayOfWeek startDayOfWeek;

	public Date getStartUtilDate() {
		return startUtilDate;
	}

	public void setStartUtilDate(Date startUtilDate) {
		this.startUtilDate = startUtilDate;
	}

	public Date getEndUtilDate() {
		return endUtilDate;
	}

	public void setEndUtilDate(Date endUtilDate) {
		this.endUtilDate = endUtilDate;
	}

	public Week() {
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getWeekStartDay() {
		return weekStartDay;
	}

	public void setWeekStartDay(String weekStartDay) {
		if (StringUtils.isNotBlank(weekStartDay)) {
			weekStartDay = ResourceUtil.firstLetterUpperCaseOtherLowerCase(weekStartDay);
		}
		this.weekStartDay = weekStartDay;
	}

	public String getWeekEndDay() {
		return weekEndDay;
	}

	public void setWeekEndDay(String weekEndDay) {
		if (StringUtils.isNotBlank(weekEndDay)) {
			weekEndDay = ResourceUtil.firstLetterUpperCaseOtherLowerCase(weekEndDay);
		}
		this.weekEndDay = weekEndDay;
	}

	public Date getApplicationLiveDate() {
		return applicationLiveDate;
	}

	public void setApplicationLiveDate(Date applicationLiveDate) {
		this.applicationLiveDate = applicationLiveDate;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public DayOfWeek getStartDayOfWeek() {
		return startDayOfWeek;
	}

	public void setStartDayOfWeek(DayOfWeek startDayOfWeek) {
		this.startDayOfWeek = startDayOfWeek;
	}
}
