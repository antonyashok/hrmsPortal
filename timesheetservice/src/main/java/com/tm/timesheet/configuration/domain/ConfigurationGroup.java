/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.domain.ConfigurationGroup.java
 * Author        : Annamalai L
 * Date Created  : Mar 20, 2017
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
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "timesheet_config_grp")

public class ConfigurationGroup extends AbstractAuditingEntity implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = -1610425446933695807L;


    public enum UserGroupCategoryEnum {
        RCTR, EMPL, CNCTR
    }
    
    public enum EffectiveFlagEnum {
        Y, N
    }

    public enum ActiveFlagEnum {
        Y, N
    }

    @Id
    @Column(name = "ts_config_grp_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID configurationGroupId;

    @Column(name = "ts_config_grp_name")
    private String configurationGroupName;

    @Enumerated(EnumType.STRING)
    @Column(name = "actv_flg")
    private ActiveFlagEnum activeFlag;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "effctv_flg")
    private EffectiveFlagEnum effectiveFlag;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "config_grp_category", nullable = false)
    private UserGroupCategoryEnum userGroupCategory;
    
    @Column(name = "effctv_end_dt")
    private ZonedDateTime effectiveEndDate = ZonedDateTime.now();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "configurationGroup", cascade = CascadeType.ALL)
    private TimesheetConfiguration timesheetConfiguration;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "configurationGroup", cascade = CascadeType.ALL)
    private List<ConfigurationGroupLocation> configurationGroupLocation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "configurationGroup", cascade = CascadeType.ALL)
    private List<ConfigurationGroupUserJobTitle> configurationGroupUserJobTitle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "configurationGroup", cascade = CascadeType.ALL)
    private List<HolidayConfiguration> holidayConfiguration;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "configurationGroup", cascade = CascadeType.ALL)
    private List<NotificationConfiguration> notificationConfiguration;

    public UUID getConfigurationGroupId() {
        return configurationGroupId;
    }

    public void setConfigurationGroupId(UUID configurationGroupId) {
        this.configurationGroupId = configurationGroupId;
    }

    public String getConfigurationGroupName() {
        return configurationGroupName;
    }

    public void setConfigurationGroupName(String configurationGroupName) {
        this.configurationGroupName = configurationGroupName;
    }

    public ActiveFlagEnum getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(ActiveFlagEnum activeFlag) {
        this.activeFlag = activeFlag;
    }

    public EffectiveFlagEnum getEffectiveFlag() {
        return effectiveFlag;
    }

    public void setEffectiveFlag(EffectiveFlagEnum effectiveFlag) {
        this.effectiveFlag = effectiveFlag;
    }

    public UserGroupCategoryEnum getUserGroupCategory() {
        return userGroupCategory;
    }

    public void setUserGroupCategory(UserGroupCategoryEnum userGroupCategory) {
        this.userGroupCategory = userGroupCategory;
    }

    public ZonedDateTime getEffectiveEndDate() {
        return effectiveEndDate;
    }

    public void setEffectiveEndDate(ZonedDateTime effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }
    
    public TimesheetConfiguration getTimesheetConfiguration() {
        return timesheetConfiguration;
    }

    public void setTimesheetConfiguration(TimesheetConfiguration timesheetConfiguration) {
        this.timesheetConfiguration = timesheetConfiguration;
    }

    public List<ConfigurationGroupLocation> getConfigurationGroupLocation() {
        return configurationGroupLocation;
    }

    public void setConfigurationGroupLocation(
            List<ConfigurationGroupLocation> configurationGroupLocation) {
        this.configurationGroupLocation = configurationGroupLocation;
    }

    public List<ConfigurationGroupUserJobTitle> getConfigurationGroupUserJobTitle() {
        return configurationGroupUserJobTitle;
    }

    public void setConfigurationGroupUserJobTitle(
            List<ConfigurationGroupUserJobTitle> configurationGroupUserJobTitle) {
        this.configurationGroupUserJobTitle = configurationGroupUserJobTitle;
    }

    public List<HolidayConfiguration> getHolidayConfiguration() {
        return holidayConfiguration;
    }

    public void setHolidayConfiguration(List<HolidayConfiguration> holidayConfiguration) {
        this.holidayConfiguration = holidayConfiguration;
    }

    public List<NotificationConfiguration> getNotificationConfiguration() {
        return notificationConfiguration;
    }

    public void setNotificationConfiguration(List<NotificationConfiguration> notificationConfiguration) {
        this.notificationConfiguration = notificationConfiguration;
    }
    
}
