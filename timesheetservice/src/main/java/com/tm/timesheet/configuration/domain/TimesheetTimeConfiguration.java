/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.domain.TimesheetTimeConfiguration.java
 * Author        : Annamalai L
 * Date Created  : Jan 10, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.tm.timesheet.configuration.domain.util.TimeConverter;

@Entity
@Table(name = "timesheet_time_config")
public class TimesheetTimeConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1773903394813395383L;

	@Id
	@Column(name = "ts_config_time_id", length = 36)
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID timesheetTimeId;

	@OneToOne
	@JoinColumn(name = "ts_config_id")
	private TimesheetConfiguration timesheetConfiguration;

	@Column(name = "brk_strt_time", length = 45)
	@Convert(converter = TimeConverter.class)   // Note: The converter is using in time to string, string to time conversion. (Refer TimeConverter) 
	private String breakStartTime;

	@Column(name = "brk_end_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String breakEndTime;

	@Column(name = "sun_strt_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String sundayStartTime;

	@Column(name = "sun_end_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String sundayEndTime;

	@Column(name = "mon_strt_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String mondayStartTime;

	@Column(name = "mon_end_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String mondayEndTime;

	@Column(name = "tue_strt_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String tuesdayStartTime;

	@Column(name = "tue_end_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String tuesdayEndTime;

	@Column(name = "wed_strt_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String wednesdayStartTime;

	@Column(name = "wed_end_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String wednesdayEndTime;

	@Column(name = "thu_strt_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String thursdayStartTime;

	@Column(name = "thu_end_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String thursdayEndTime;

	@Column(name = "fri_strt_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String fridayStartTime;

	@Column(name = "fri_end_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String fridayEndTime;

	@Column(name = "sat_strt_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String saturdayStartTime;

	@Column(name = "sat_end_time", length = 45)
	@Convert(converter = TimeConverter.class)
	private String saturdayEndTime;

	public UUID getTimesheetTimeId() {
		return timesheetTimeId;
	}

	public void setTimesheetTimeId(UUID timesheetTimeId) {
		this.timesheetTimeId = timesheetTimeId;
	}

	public TimesheetConfiguration getTimesheetConfiguration() {
		return timesheetConfiguration;
	}

	public void setTimesheetConfiguration(
			TimesheetConfiguration timesheetConfiguration) {
		this.timesheetConfiguration = timesheetConfiguration;
	}

	public String getBreakStartTime() {
		return breakStartTime;
	}

	public void setBreakStartTime(String breakStartTime) {
		this.breakStartTime = breakStartTime;
	}

	public String getBreakEndTime() {
		return breakEndTime;
	}

	public void setBreakEndTime(String breakEndTime) {
		this.breakEndTime = breakEndTime;
	}

	public String getSundayStartTime() {
		return sundayStartTime;
	}

	public void setSundayStartTime(String sundayStartTime) {
		this.sundayStartTime = sundayStartTime;
	}

	public String getSundayEndTime() {
		return sundayEndTime;
	}

	public void setSundayEndTime(String sundayEndTime) {
		this.sundayEndTime = sundayEndTime;
	}

	public String getMondayStartTime() {
		return mondayStartTime;
	}

	public void setMondayStartTime(String mondayStartTime) {
		this.mondayStartTime = mondayStartTime;
	}

	public String getMondayEndTime() {
		return mondayEndTime;
	}

	public void setMondayEndTime(String mondayEndTime) {
		this.mondayEndTime = mondayEndTime;
	}

	public String getTuesdayStartTime() {
		return tuesdayStartTime;
	}

	public void setTuesdayStartTime(String tuesdayStartTime) {
		this.tuesdayStartTime = tuesdayStartTime;
	}

	public String getTuesdayEndTime() {
		return tuesdayEndTime;
	}

	public void setTuesdayEndTime(String tuesdayEndTime) {
		this.tuesdayEndTime = tuesdayEndTime;
	}

	public String getWednesdayStartTime() {
		return wednesdayStartTime;
	}

	public void setWednesdayStartTime(String wednesdayStartTime) {
		this.wednesdayStartTime = wednesdayStartTime;
	}

	public String getWednesdayEndTime() {
		return wednesdayEndTime;
	}

	public void setWednesdayEndTime(String wednesdayEndTime) {
		this.wednesdayEndTime = wednesdayEndTime;
	}

	public String getThursdayStartTime() {
		return thursdayStartTime;
	}

	public void setThursdayStartTime(String thursdayStartTime) {
		this.thursdayStartTime = thursdayStartTime;
	}

	public String getThursdayEndTime() {
		return thursdayEndTime;
	}

	public void setThursdayEndTime(String thursdayEndTime) {
		this.thursdayEndTime = thursdayEndTime;
	}

	public String getFridayStartTime() {
		return fridayStartTime;
	}

	public void setFridayStartTime(String fridayStartTime) {
		this.fridayStartTime = fridayStartTime;
	}

	public String getFridayEndTime() {
		return fridayEndTime;
	}

	public void setFridayEndTime(String fridayEndTime) {
		this.fridayEndTime = fridayEndTime;
	}

	public String getSaturdayStartTime() {
		return saturdayStartTime;
	}

	public void setSaturdayStartTime(String saturdayStartTime) {
		this.saturdayStartTime = saturdayStartTime;
	}

	public String getSaturdayEndTime() {
		return saturdayEndTime;
	}

	public void setSaturdayEndTime(String saturdayEndTime) {
		this.saturdayEndTime = saturdayEndTime;
	}

}
