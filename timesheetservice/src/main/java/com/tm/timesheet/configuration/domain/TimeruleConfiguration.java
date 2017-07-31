/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.domain.TimeruleConfiguration.java
 * Author        : Hemanth Kumar
 * Date Created  : Feb 28, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "time_rule")
public class TimeruleConfiguration extends AbstractAuditingEntity {

	 
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    public enum DeductHolidayFlag {
		Y, N
	}

	public enum ActiveFlag {
		Y, N
	}

	@Id
	@Column(name = "time_rule_id", length = 36)
	@GeneratedValue(generator = "system-uuid")
	@Type(type = "uuid-char")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	private UUID timeRuleId;

	@NotBlank(message = "{timerule.type.not.empty}")
	@Size(max = 45, message = "{timerule.name.exceeds.char}")
	@Column(name = "time_rule_name")
	private String timeRuleName;

	@Column(name = "ot_for_day_hrs_exceeds")
	private Double otForDayHrsExceeds;

	@Column(name = "ot_for_week_hrs_exceeds")
	private Double otForWeekHrsExceeds;

	@Column(name = "dt_for_day_hrs_exceeds")
	private Double dtForDayHrsExceeds;

	@Column(name = "rate_for_7th_day_all_hrs")
	private String rateFor7thDayAllHrs;

	@Column(name = "rate_for_7th_day_first_8hrs")
	private String rateFor7thDayFirst8hrs;

	@Column(name = "rate_for_7th_day_after_8hrs")
	private String rateFor7thDayAfter8hrs;

	@Column(name = "rate_for_holidays")
	private String rateForHolidays;

	@Column(name = "rate_for_sundays")
	private String rateForSundays;

	@Enumerated(EnumType.STRING)
	@Column(name = "deduct_8hrs_for_holiday_flg")
	private DeductHolidayFlag deduct8hrsForHolidayFlag = DeductHolidayFlag.Y;

	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private ActiveFlag activeIndFlag = ActiveFlag.Y;

	@Column(name = "max_pto_hrs")
	@NotNull(message = "{timerule.pto.hours.not.empty}")
	private Double maxPto;

	@Transient
	private String strrateFor7thDayAllHrs;

	@Transient
	private String strrateFor7thDayFirst8hrs;

	@Transient
	private String strrateFor7thDayAfter8hrs;

	@Transient
	private String strrateForHolidays;

	@Transient
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

	public DeductHolidayFlag getDeduct8hrsForHolidayFlag() {
		return deduct8hrsForHolidayFlag;
	}

	public void setDeduct8hrsForHolidayFlag(DeductHolidayFlag deduct8hrsForHolidayFlag) {
		this.deduct8hrsForHolidayFlag = deduct8hrsForHolidayFlag;
	}

	public ActiveFlag getActiveIndFlag() {
		return activeIndFlag;
	}

	public void setActiveIndFlag(ActiveFlag activeIndFlag) {
		this.activeIndFlag = activeIndFlag;
	}

	public Double getMaxPto() {
		return maxPto;
	}

	public void setMaxPto(Double maxPto) {
		this.maxPto = maxPto;
	}

}
