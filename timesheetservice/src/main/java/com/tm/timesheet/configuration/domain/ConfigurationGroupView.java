/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.domain.ConfigurationGroupView.java
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
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.tm.timesheet.configuration.enums.TimeCalculationEnum;
import com.tm.timesheet.configuration.enums.WorkInputEnum;

@Entity
@Table(name = "config_settings_view")

public class ConfigurationGroupView implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -1610425446933695807L;



    @Id
    @Column(name = "ts_config_grp_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID configurationGroupId;

    @Column(name = "ofc_name")
    private String officeName;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "week_start_day")
    private String weekStartDay;

    @Column(name = "week_end_day")
    private String weekEndDay;
    
    @Column(name = "actv_flg")
    private String activeFlag;

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

    @Column(name = "effctv_end_dt")
    private ZonedDateTime effectiveEndDate = ZonedDateTime.now();

    @Column(name = "create_dt")
    private ZonedDateTime createdDate = ZonedDateTime.now();

    @Column(name = "ntfy_flg")
    private String notifyFlag;

    @Column(name = "hldy_flg")
    private String holidayFlag;

    public UUID getConfigurationGroupId() {
        return configurationGroupId;
    }

    public void setConfigurationGroupId(UUID configurationGroupId) {
        this.configurationGroupId = configurationGroupId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
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

    public ZonedDateTime getEffectiveEndDate() {
        return effectiveEndDate;
    }

    public void setEffectiveEndDate(ZonedDateTime effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getNotifyFlag() {
        return notifyFlag;
    }

    public void setNotifyFlag(String notifyFlag) {
        this.notifyFlag = notifyFlag;
    }

    public String getHolidayFlag() {
        return holidayFlag;
    }

    public void setHolidayFlag(String holidayFlag) {
        this.holidayFlag = holidayFlag;
    }

}
