package com.tm.engagement.service.dto;

import java.util.List;

import javax.persistence.OneToMany;

public class WeekPlanDTO {

    private String weekPlanId;

    private String activeFlag;

    private String createdDate;

    private Long createdBy;

    private String endDay;

    private String endTime;

    private String lastUpdatedDate;

    private double noHours;

    private String startDay;

    private String startTime;

    private Long updatedBy;

    private String weekPlanDescription;

    private String weekPlanName;

    @OneToMany(mappedBy = "weekPlan")
    private List<EmployeeEngagementDTO> employeeEngagements;

    public String getWeekPlanId() {
        return weekPlanId;
    }

    public void setWeekPlanId(String weekPlanId) {
        this.weekPlanId = weekPlanId;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public double getNoHours() {
        return noHours;
    }

    public void setNoHours(double noHours) {
        this.noHours = noHours;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getWeekPlanDescription() {
        return weekPlanDescription;
    }

    public void setWeekPlanDescription(String weekPlanDescription) {
        this.weekPlanDescription = weekPlanDescription;
    }

    public String getWeekPlanName() {
        return weekPlanName;
    }

    public void setWeekPlanName(String weekPlanName) {
        this.weekPlanName = weekPlanName;
    }

    public List<EmployeeEngagementDTO> getEmployeeEngagements() {
        return employeeEngagements;
    }

    public void setEmployeeEngagements(List<EmployeeEngagementDTO> employeeEngagements) {
        this.employeeEngagements = employeeEngagements;
    }

}
