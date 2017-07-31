/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.domain.TimesheetHourConfiguration.java
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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "timesheet_hrs_config")
public class TimesheetHourConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4249741773361817767L;

	@Id
	@Column(name = "ts_hrs_config_id", length = 36)
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID timesheetHourId;

	@OneToOne
	@JoinColumn(name = "ts_config_id")
	private TimesheetConfiguration timesheetConfiguration;

	@Max(24)
	@Column(name = "sun_hrs")
	private Double sundayHours;

	@Max(24)
	@Column(name = "mon_hrs")
	private Double mondayHours;

	@Max(24)
	@Column(name = "tue_hrs")
	private Double tuesdayHours;

	@Max(24)
	@Column(name = "wed_hrs")
	private Double wednesdayHours;

	@Max(24)
	@Column(name = "thu_hrs")
	private Double thursdayHours;

	@Max(24)
	@Column(name = "fri_hrs")
	private Double fridayHours;

	@Max(24)
	@Column(name = "sat_hrs")
	private Double saturdayHours;

	public UUID getTimesheetHourId() {
		return timesheetHourId;
	}

	public void setTimesheetHourId(UUID timesheetHourId) {
		this.timesheetHourId = timesheetHourId;
	}

	public TimesheetConfiguration getTimesheetConfiguration() {
		return timesheetConfiguration;
	}

	public void setTimesheetConfiguration(
			TimesheetConfiguration timesheetConfiguration) {
		this.timesheetConfiguration = timesheetConfiguration;
	}

	public Double getSundayHours() {
		return sundayHours;
	}

	public void setSundayHours(Double sundayHours) {
		this.sundayHours = sundayHours;
	}

	public Double getMondayHours() {
		return mondayHours;
	}

	public void setMondayHours(Double mondayHours) {
		this.mondayHours = mondayHours;
	}

	public Double getTuesdayHours() {
		return tuesdayHours;
	}

	public void setTuesdayHours(Double tuesdayHours) {
		this.tuesdayHours = tuesdayHours;
	}

	public Double getWednesdayHours() {
		return wednesdayHours;
	}

	public void setWednesdayHours(Double wednesdayHours) {
		this.wednesdayHours = wednesdayHours;
	}

	public Double getThursdayHours() {
		return thursdayHours;
	}

	public void setThursdayHours(Double thursdayHours) {
		this.thursdayHours = thursdayHours;
	}

	public Double getFridayHours() {
		return fridayHours;
	}

	public void setFridayHours(Double fridayHours) {
		this.fridayHours = fridayHours;
	}

	public Double getSaturdayHours() {
		return saturdayHours;
	}

    public void setSaturdayHours(Double saturdayHours) {
		this.saturdayHours = saturdayHours;
	}

}
