/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.service.resources.TimeruleResource.java
 * Author        : Hemanth Kumar
 * Date Created  : Feb 28, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.service.resources;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "TimeruleConfiguration", collectionRelation = "TimeruleConfigurations")

public class TimeruleResource extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -2338938446756812377L;

	private UUID timeRuleId;
	
	private String timeRuleName;
	
	private Double otForDayHrsExceeds;

	private Double otForWeekHrsExceeds;

	private Double dtForDayHrsExceeds;

	private String rateFor7thDayAllHrs;

	private String rateFor7thDayFirst8hrs;

	private String rateFor7thDayAfter8hrs;

	private String rateForHolidays;

	private String rateForSundays;

	private String deduct8hrsForHolidayFlag;

	private String activeIndFlag;

	private Double maxPto;

	private String strrateFor7thDayAllHrs;

	private String strrateFor7thDayFirst8hrs;

	private String strrateFor7thDayAfter8hrs;

	private String strrateForHolidays;

	private String strrateForSundays;

	public String getStrrateFor7thDayAllHrs() {
		return strrateFor7thDayAllHrs;
	}

	public void setStrrateFor7thDayAllHrs(String strrateFor7thDayAllHrs) {
		this.strrateFor7thDayAllHrs = strrateFor7thDayAllHrs;
	}

	public String getStrrateFor7thDayFirst8hrs() {
		return strrateFor7thDayFirst8hrs;
	}

	public void setStrrateFor7thDayFirst8hrs(String strrateFor7thDayFirst8hrs) {
		this.strrateFor7thDayFirst8hrs = strrateFor7thDayFirst8hrs;
	}

	public String getStrrateFor7thDayAfter8hrs() {
		return strrateFor7thDayAfter8hrs;
	}

	public void setStrrateFor7thDayAfter8hrs(String strrateFor7thDayAfter8hrs) {
		this.strrateFor7thDayAfter8hrs = strrateFor7thDayAfter8hrs;
	}

	public String getStrrateForHolidays() {
		return strrateForHolidays;
	}

	public void setStrrateForHolidays(String strrateForHolidays) {
		this.strrateForHolidays = strrateForHolidays;
	}

	public String getStrrateForSundays() {
		return strrateForSundays;
	}

	public void setStrrateForSundays(String strrateForSundays) {
		this.strrateForSundays = strrateForSundays;
	}

	public UUID getTimeRuleId() {
		return timeRuleId;
	}

	public void setTimeRuleId(UUID timeRuleId) {
		this.timeRuleId = timeRuleId;
	}

	public String getTimeRuleName() {
		return timeRuleName;
	}

	public void setTimeRuleName(String timeRuleName) {
		this.timeRuleName = timeRuleName;
	}

	public Double getOtForDayHrsExceeds() {
		return otForDayHrsExceeds;
	}

	public void setOtForDayHrsExceeds(Double otForDayHrsExceeds) {
		this.otForDayHrsExceeds = otForDayHrsExceeds;
	}

	public Double getOtForWeekHrsExceeds() {
		return otForWeekHrsExceeds;
	}

	public void setOtForWeekHrsExceeds(Double otForWeekHrsExceeds) {
		this.otForWeekHrsExceeds = otForWeekHrsExceeds;
	}

	public Double getDtForDayHrsExceeds() {
		return dtForDayHrsExceeds;
	}

	public void setDtForDayHrsExceeds(Double dtForDayHrsExceeds) {
		this.dtForDayHrsExceeds = dtForDayHrsExceeds;
	}

	public String getRateFor7thDayAllHrs() {
		return rateFor7thDayAllHrs;
	}

	public void setRateFor7thDayAllHrs(String rateFor7thDayAllHrs) {
		this.rateFor7thDayAllHrs = rateFor7thDayAllHrs;
	}

	public String getRateFor7thDayFirst8hrs() {
		return rateFor7thDayFirst8hrs;
	}

	public void setRateFor7thDayFirst8hrs(String rateFor7thDayFirst8hrs) {
		this.rateFor7thDayFirst8hrs = rateFor7thDayFirst8hrs;
	}

	public String getRateFor7thDayAfter8hrs() {
		return rateFor7thDayAfter8hrs;
	}

	public void setRateFor7thDayAfter8hrs(String rateFor7thDayAfter8hrs) {
		this.rateFor7thDayAfter8hrs = rateFor7thDayAfter8hrs;
	}

	public String getRateForHolidays() {
		return rateForHolidays;
	}

	public void setRateForHolidays(String rateForHolidays) {
		this.rateForHolidays = rateForHolidays;
	}

	public String getRateForSundays() {
		return rateForSundays;
	}

	public void setRateForSundays(String rateForSundays) {
		this.rateForSundays = rateForSundays;
	}

	public String getDeduct8hrsForHolidayFlag() {
		return deduct8hrsForHolidayFlag;
	}

	public void setDeduct8hrsForHolidayFlag(String deduct8hrsForHolidayFlag) {
		this.deduct8hrsForHolidayFlag = deduct8hrsForHolidayFlag;
	}

	public String getActiveIndFlag() {
		return activeIndFlag;
	}

	public void setActiveIndFlag(String activeIndFlag) {
		this.activeIndFlag = activeIndFlag;
	}

	public Double getMaxPto() {
		return maxPto;
	}

	public void setMaxPto(Double maxPto) {
		this.maxPto = maxPto;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
