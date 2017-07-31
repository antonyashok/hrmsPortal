package com.tm.timesheet.timeoff.service.dto;

import java.io.Serializable;
import java.util.Date;


public class HolidayDTO implements Serializable {

    private static final long serialVersionUID = 2464954033909138167L;

    private String holidayStateProvinceId;
    private Long stateProvinceId;
    private String holidayCalendarDetailId;
    private Date holidayDate;
    private String description;
    private String settingName;
    private Long countryId;

    public HolidayDTO() {}

    public HolidayDTO(String holidayStateProvinceId, Long stateProvinceId,
            String holidayCalendarDetailId, Date holidayDate, String description,
            String settingName, Long countryId) {
        this.holidayStateProvinceId = holidayStateProvinceId;
        this.stateProvinceId = stateProvinceId;
        this.holidayCalendarDetailId = holidayCalendarDetailId;
        this.holidayDate = holidayDate;
        this.description = description;
        this.settingName = settingName;
        this.countryId = countryId;
    }

    public String getHolidayStateProvinceId() {
        return holidayStateProvinceId;
    }

    public void setHolidayStateProvinceId(String holidayStateProvinceId) {
        this.holidayStateProvinceId = holidayStateProvinceId;
    }

    public Long getStateProvinceId() {
        return stateProvinceId;
    }

    public void setStateProvinceId(Long stateProvinceId) {
        this.stateProvinceId = stateProvinceId;
    }

    public String getHolidayCalendarDetailId() {
        return holidayCalendarDetailId;
    }

    public void setHolidayCalendarDetailId(String holidayCalendarDetailId) {
        this.holidayCalendarDetailId = holidayCalendarDetailId;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }



}
