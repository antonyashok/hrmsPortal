/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.domain.TimesheetConfiguration.java
 * Author        : Annamalai L
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tm.timesheet.configuration.enums.TimeCalculationEnum;
import com.tm.timesheet.configuration.enums.WorkInputEnum;

@Entity
@Table(name = "timesheet_config")
public class TimesheetConfiguration implements Serializable {

    private static final long serialVersionUID = -3481700213887059631L;

    @Id
    @Column(name = "ts_config_id", length = 36)
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID configurationId;

    @OneToOne
    @JoinColumn(name = "ts_config_grp_id", nullable = false)
    private ConfigurationGroup configurationGroup;

    @Column(name = "week_start_day", length = 45)
    private String weekStartDay;

    @Column(name = "week_end_day", length = 45)
    private String weekEndDay;

    @Column(name = "min_hrs")
    private Double minHours;

    @Column(name = "max_hrs")
    private Double maxHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "dflt_time_calc")
    private TimeCalculationEnum timeCalculation;

    @Enumerated(EnumType.STRING)
    @Column(name = "dflt_input")
    private WorkInputEnum defaultInput;

    @Column(name = "st_min_hrs")
    private Double stMinHours;

    @Column(name = "st_max_hrs")
    private Double stMaxHours;

    @Column(name = "ot_min_hrs")
    private Double otMinHours;

    @Column(name = "ot_max_hrs")
    private Double otMaxHours;

    @Column(name = "dt_min_hrs")
    private Double dtMinHours;

    @Column(name = "dt_max_hrs")
    private Double dtMaxHours;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "timesheetConfiguration",
            cascade = CascadeType.ALL)
	private TimesheetHourConfiguration timesheetHourConfiguration;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "timesheetConfiguration",
            cascade = CascadeType.ALL)
	private TimesheetTimeConfiguration timesheetTimeConfiguration;

    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    @JsonIgnore
    private Long createdBy;

    @CreatedDate
    @Column(name = "create_dt", nullable = false)
    @JsonIgnore
    @Temporal(value=TemporalType.DATE)
    private Date createdDate= new Date();
    
    public UUID getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(UUID configurationId) {
        this.configurationId = configurationId;
    }

    public String getWeekStartDay() {
        return weekStartDay;
    }

    public void setWeekStartDay(String weekStartDay) {
        this.weekStartDay = weekStartDay;
    }

    public String getWeekEndDay() {
        return weekEndDay;
    }

    public void setWeekEndDay(String weekEndDay) {
        this.weekEndDay = weekEndDay;
    }

    public Double getMinHours() {
        return minHours;
    }

    public void setMinHours(Double minHours) {
        this.minHours = minHours;
    }

    public Double getMaxHours() {
        return maxHours;
    }

    public void setMaxHours(Double maxHours) {
        this.maxHours = maxHours;
    }

    public TimeCalculationEnum getTimeCalculation() {
        return timeCalculation;
    }

    public void setTimeCalculation(TimeCalculationEnum timeCalculation) {
        this.timeCalculation = timeCalculation;
    }

    public WorkInputEnum getDefaultInput() {
        return defaultInput;
    }

    public void setDefaultInput(WorkInputEnum defaultInput) {
        this.defaultInput = defaultInput;
    }
    
    public Double getStMinHours() {
        return stMinHours;
    }

    public void setStMinHours(Double stMinHours) {
        this.stMinHours = stMinHours;
    }

    public Double getStMaxHours() {
        return stMaxHours;
    }

    public void setStMaxHours(Double stMaxHours) {
        this.stMaxHours = stMaxHours;
    }

    public Double getOtMinHours() {
        return otMinHours;
    }

    public void setOtMinHours(Double otMinHours) {
        this.otMinHours = otMinHours;
    }

    public Double getOtMaxHours() {
        return otMaxHours;
    }

    public void setOtMaxHours(Double otMaxHours) {
        this.otMaxHours = otMaxHours;
    }

    public Double getDtMinHours() {
        return dtMinHours;
    }

    public void setDtMinHours(Double dtMinHours) {
        this.dtMinHours = dtMinHours;
    }

    public Double getDtMaxHours() {
        return dtMaxHours;
    }

    public void setDtMaxHours(Double dtMaxHours) {
        this.dtMaxHours = dtMaxHours;
    }

	public ConfigurationGroup getConfigurationGroup() {
		return configurationGroup;
	}

	public void setConfigurationGroup(ConfigurationGroup configurationGroup) {
		this.configurationGroup = configurationGroup;
	}

	public TimesheetHourConfiguration getTimesheetHourConfiguration() {
		return timesheetHourConfiguration;
	}

	public void setTimesheetHourConfiguration(
			TimesheetHourConfiguration timesheetHourConfiguration) {
		this.timesheetHourConfiguration = timesheetHourConfiguration;
	}

	public TimesheetTimeConfiguration getTimesheetTimeConfiguration() {
		return timesheetTimeConfiguration;
	}

	public void setTimesheetTimeConfiguration(
			TimesheetTimeConfiguration timesheetTimeConfiguration) {
		this.timesheetTimeConfiguration = timesheetTimeConfiguration;
	}

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
