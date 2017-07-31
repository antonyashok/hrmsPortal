package com.tm.timesheet.configuration.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

/**
 * A DTO for the ConfigurationGroup entity.
 */
@Relation(value = "Configuration", collectionRelation = "Configurations")
public class ConfigurationGroupDTO extends ResourceSupport implements Serializable {
    
  /**
     * 
     */
    private static final long serialVersionUID = -3996522176189454337L;

    // --ConfigurationGroup setup
    private UUID configurationGroupId;
    private String configurationGroupName;
    private String effectiveFlag;
    private String activeFlag;

    // --TimesheetConfiguration setup
    private UUID configurationId;

    @NotBlank(message = "{timetrack.creation.workstart.notnull}")
    private String weekStartDay;

    @NotBlank(message = "{timetrack.creation.workend.notnull}")
    private String weekEndDay;

    private Double minHours;
    private Double maxHours;
    private String timeCalculation;
    private String defaultInput;
    private Double stMinHours;

    @Digits(integer = 3, fraction = 2)
    @NotNull
    private Double stMaxHours;
    private Double otMinHours;

    @Digits(integer = 3, fraction = 2)
    private Double otMaxHours;
    private Double dtMinHours;

    @Digits(integer = 3, fraction = 2)
    private Double dtMaxHours;

    // --TimesheetHourConfiguration settings
    private UUID timesheetHourId;
    private Double sundayHours = Double.valueOf(0);
    private Double mondayHours = Double.valueOf(0);
    private Double tuesdayHours = Double.valueOf(0);
    private Double wednesdayHours = Double.valueOf(0);
    private Double thursdayHours = Double.valueOf(0);
    private Double fridayHours = Double.valueOf(0);
    private Double saturdayHours = Double.valueOf(0);

    // --TimesheetTimeConfiguration settings
    private UUID timesheetTimeId;
    private String breakStartTime;
    private String breakEndTime;
    private String sundayStartTime;
    private String sundayEndTime;
    private String mondayStartTime;
    private String mondayEndTime;
    private String tuesdayStartTime;
    private String tuesdayEndTime;
    private String wednesdayStartTime;
    private String wednesdayEndTime;
    private String thursdayStartTime;
    private String thursdayEndTime;
    private String fridayStartTime;
    private String fridayEndTime;
    private String saturdayStartTime;
    private String saturdayEndTime;
    private String holidayFlag;
    private String notificationFlag;

    // --ConfigurationGroupLocation setup
    @NotNull
    @NotEmpty
    private List<ConfigurationGroupLocationDTO> configurationGroupLocation;

    // --ConfigurationGroupUserJobTitle setup
    @NotNull
    @NotEmpty
    private List<ConfigurationGroupUserJobTitleDTO> configurationGroupUserJobTitle;

    // -- HolidayConfiguration setup
    private List<HolidayConfigurationDTO> holidayConfiguration;

    // -- NotificationConfiguration setup
    private List<NotificationConfigurationDTO> notificationConfiguration;

    // -- NotificationAttribute setup
    @Valid
    private List<NotificationAttributeDTO> notificationAttribute;

    // --Actual input
    private String straightTime;
    private String overTime;
    private String doubleTime;

    private List<OfficeLocationDTO> officeLocationsDTO;

    private String userGroupCategory;

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

    public String getEffectiveFlag() {
        return effectiveFlag;
    }

    public void setEffectiveFlag(String effectiveFlag) {
        this.effectiveFlag = effectiveFlag;
    }



    public String getActiveFlag() {
        return activeFlag;
    }



    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }


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


    public String getTimeCalculation() {
        return timeCalculation;
    }

    public void setTimeCalculation(String timeCalculation) {
        this.timeCalculation = timeCalculation;
    }

    public String getDefaultInput() {
        return defaultInput;
    }

    public void setDefaultInput(String defaultInput) {
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

    public UUID getTimesheetHourId() {
        return timesheetHourId;
    }

    public void setTimesheetHourId(UUID timesheetHourId) {
        this.timesheetHourId = timesheetHourId;
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

    public UUID getTimesheetTimeId() {
        return timesheetTimeId;
    }

    public void setTimesheetTimeId(UUID timesheetTimeId) {
        this.timesheetTimeId = timesheetTimeId;
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



    public String getHolidayFlag() {
        return holidayFlag;
    }



    public void setHolidayFlag(String holidayFlag) {
        this.holidayFlag = holidayFlag;
    }



    public String getNotificationFlag() {
        return notificationFlag;
    }



    public void setNotificationFlag(String notificationFlag) {
        this.notificationFlag = notificationFlag;
    }



    public List<ConfigurationGroupLocationDTO> getConfigurationGroupLocation() {
        return configurationGroupLocation;
    }



    public void setConfigurationGroupLocation(
            List<ConfigurationGroupLocationDTO> configurationGroupLocation) {
        this.configurationGroupLocation = configurationGroupLocation;
    }



    public List<ConfigurationGroupUserJobTitleDTO> getConfigurationGroupUserJobTitle() {
        return configurationGroupUserJobTitle;
    }



    public void setConfigurationGroupUserJobTitle(
            List<ConfigurationGroupUserJobTitleDTO> configurationGroupUserJobTitle) {
        this.configurationGroupUserJobTitle = configurationGroupUserJobTitle;
    }



    public List<HolidayConfigurationDTO> getHolidayConfiguration() {
        return holidayConfiguration;
    }



    public void setHolidayConfiguration(List<HolidayConfigurationDTO> holidayConfiguration) {
        this.holidayConfiguration = holidayConfiguration;
    }



    public List<NotificationConfigurationDTO> getNotificationConfiguration() {
        return notificationConfiguration;
    }



    public void setNotificationConfiguration(
            List<NotificationConfigurationDTO> notificationConfiguration) {
        this.notificationConfiguration = notificationConfiguration;
    }



    public String getStraightTime() {
        return straightTime;
    }



    public void setStraightTime(String straightTime) {
        this.straightTime = straightTime;
    }



    public String getOverTime() {
        return overTime;
    }



    public void setOverTime(String overTime) {
        this.overTime = overTime;
    }



    public String getDoubleTime() {
        return doubleTime;
    }



    public void setDoubleTime(String doubleTime) {
        this.doubleTime = doubleTime;
    }



    public List<OfficeLocationDTO> getOfficeLocationsDTO() {
        return officeLocationsDTO;
    }



    public void setOfficeLocationsDTO(List<OfficeLocationDTO> officeLocationsDTO) {
        this.officeLocationsDTO = officeLocationsDTO;
    }



    public List<NotificationAttributeDTO> getNotificationAttribute() {
        return notificationAttribute;
    }



    public void setNotificationAttribute(List<NotificationAttributeDTO> notificationAttribute) {
        this.notificationAttribute = notificationAttribute;
    }

    public String getUserGroupCategory() {
        return userGroupCategory;
    }

    public void setUserGroupCategory(String userGroupCategory) {
        this.userGroupCategory = userGroupCategory;
    }

}
