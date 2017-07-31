package com.tm.timesheet.configuration.service.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Relation(value = "holidayConfiguration", collectionRelation = "holidayConfiguration")
public class HolidayConfigurationDTO extends ResourceSupport implements Serializable {

    /**
     * Auto generatedId
     */
    private static final long serialVersionUID = -5542767881337677200L;

    private UUID timeSheetHolidayId;
    private String holidayDescription;

    @JsonIgnore
    private Date holidayDate;

    private UUID configurationGroupId;
    private String isActive = "N";
    private String holiday;

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public UUID getTimeSheetHolidayId() {
        return timeSheetHolidayId;
    }

    public void setTimeSheetHolidayId(UUID timeSheetHolidayId) {
        this.timeSheetHolidayId = timeSheetHolidayId;
    }

    public String getHolidayDescription() {
        return holidayDescription;
    }

    public void setHolidayDescription(String holidayDescription) {
        this.holidayDescription = holidayDescription;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }

    public UUID getConfigurationGroupId() {
        return configurationGroupId;
    }

    public void setConfigurationGroupId(UUID configurationGroupId) {
        this.configurationGroupId = configurationGroupId;
    }

    public String getHoliday() {
        return holiday;
    }

    public void setHoliday(String holiday) {
        this.holiday = holiday;
    }

}
