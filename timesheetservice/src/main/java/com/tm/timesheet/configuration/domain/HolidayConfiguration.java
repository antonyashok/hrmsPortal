/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.domain.HolidayConfiguration.java
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
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "timesheet_hldy_cldr")
public class HolidayConfiguration implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1620235139434815425L;

    @Id
    @Column(name = "ts_hldy_cldr_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID timeSheetHolidayId;

    @ManyToOne
    @JoinColumn(name = "ts_config_grp_id", nullable = false)
    private ConfigurationGroup configurationGroup;

    @Temporal(TemporalType.DATE)
    @Column(name = "hldy_date")
    private Date holidayDate;

    @Column(name = "hldy_desc")
    private String holidayDescription;
    
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    @JsonIgnore
    private Long createdBy;

    @CreatedDate
    @Column(name = "create_dt", nullable = false)
    @JsonIgnore
    private ZonedDateTime createdDate = ZonedDateTime.now();  
    
    @Transient
    private String holiday;

    public UUID getTimeSheetHolidayId() {
        return timeSheetHolidayId;
    }

    public void setTimeSheetHolidayId(UUID timeSheetHolidayId) {
        this.timeSheetHolidayId = timeSheetHolidayId;
    }

    public ConfigurationGroup getConfigurationGroup() {
        return configurationGroup;
    }

    public void setConfigurationGroup(ConfigurationGroup configurationGroup) {
        this.configurationGroup = configurationGroup;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getHolidayDescription() {
        return holidayDescription;
    }

    public void setHolidayDescription(String holidayDescription) {
        this.holidayDescription = holidayDescription;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }
    
}