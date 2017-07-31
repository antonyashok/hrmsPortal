/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.service.dto.ConfigurationGroupViewDTO.java
 * Author        : Mydeen Kasim 
 * Date Created  : Feb 08, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.service.dto;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "Configuration", collectionRelation = "Configurations")
public class ConfigurationGroupViewDTO extends ResourceSupport implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -1610425446933695807L;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private UUID configurationGroupId;
    private String officeName;
    private String jobTitle;
    private String weekStartDay;
    private String weekEndDay;
    private String activeFlag;
    private String timeCalculation;
    private String defaultInput;
    private String stMinHours;
    private String stMaxHours;
    private String otMinHours;
    private String otMaxHours;
    private String dtMinHours;
    private String dtMaxHours;
    private String notifyFlag;
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

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
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


    public String getStMinHours() {
      if(StringUtils.isNotEmpty(stMinHours)) {
        stMinHours = decimalFormat.format(Double.valueOf(stMinHours));
      }
      return stMinHours;
    }

    public void setStMinHours(String stMinHours) {
      this.stMinHours = stMinHours;
    }

    public String getStMaxHours() {
      if(StringUtils.isNotEmpty(stMaxHours)) {
        stMaxHours = decimalFormat.format(Double.valueOf(stMaxHours));
      }
      return stMaxHours;
    }

    public void setStMaxHours(String stMaxHours) {
      this.stMaxHours = stMaxHours;
    }

    public String getOtMinHours() {
      if(StringUtils.isNotEmpty(otMinHours)) {
        otMinHours = decimalFormat.format(Double.valueOf(otMinHours));
      }
      return otMinHours;
    }

    public void setOtMinHours(String otMinHours) {
      this.otMinHours = otMinHours;
    }

    public String getOtMaxHours() {
      if(StringUtils.isNotEmpty(otMaxHours)) {
        otMaxHours = decimalFormat.format(Double.valueOf(otMaxHours));
      }
      return otMaxHours;
    }

    public void setOtMaxHours(String otMaxHours) {
      this.otMaxHours = otMaxHours;
    }

    public String getDtMinHours() {
      if(StringUtils.isNotEmpty(dtMinHours)) {
        dtMinHours = decimalFormat.format(Double.valueOf(dtMinHours));
      }
      return dtMinHours;
    }

    public void setDtMinHours(String dtMinHours) {
      this.dtMinHours = dtMinHours;
    }

    public String getDtMaxHours() {
      if(StringUtils.isNotEmpty(dtMaxHours)) {
        dtMaxHours = decimalFormat.format(Double.valueOf(dtMaxHours));
      }
      return dtMaxHours;
    }

    public void setDtMaxHours(String dtMaxHours) {
      this.dtMaxHours = dtMaxHours;
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

    public String getWeekEndDay() {
        return weekEndDay;
    }

    public void setWeekEndDay(String weekEndDay) {
        this.weekEndDay = weekEndDay;
    }



}
