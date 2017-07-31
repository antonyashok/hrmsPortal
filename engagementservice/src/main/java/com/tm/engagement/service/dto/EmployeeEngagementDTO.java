package com.tm.engagement.service.dto;

import java.util.List;

public class EmployeeEngagementDTO {

    private String employeeEngagementId;

    private String activeFlag;

    private Long accountManagerEmployeeId;

    private Long customerManagerContactId;

    private String effectiveEndDate;

    private String effectiveStartDate;

    private Long employeeId;

    private Long recruiterEmployeeId;

    private List<EmployeeBillingProfileDTO> employeeBillingProfiles;

    private List<EmployeeEngagementTaskMapDTO> employeeEngagementTaskMaps;

    private EngagementDTO engagement;

    private WeekPlanDTO weekPlan;

    public String getEmployeeEngagementId() {
        return employeeEngagementId;
    }

    public void setEmployeeEngagementId(String employeeEngagementId) {
        this.employeeEngagementId = employeeEngagementId;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Long getAccountManagerEmployeeId() {
        return accountManagerEmployeeId;
    }

    public void setAccountManagerEmployeeId(Long accountManagerEmployeeId) {
        this.accountManagerEmployeeId = accountManagerEmployeeId;
    }

    public Long getCustomerManagerContactId() {
        return customerManagerContactId;
    }

    public void setCustomerManagerContactId(Long customerManagerContactId) {
        this.customerManagerContactId = customerManagerContactId;
    }

    public String getEffectiveEndDate() {
        return effectiveEndDate;
    }

    public void setEffectiveEndDate(String effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    public String getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public void setEffectiveStartDate(String effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getRecruiterEmployeeId() {
        return recruiterEmployeeId;
    }

    public void setRecruiterEmployeeId(Long recruiterEmployeeId) {
        this.recruiterEmployeeId = recruiterEmployeeId;
    }

    public List<EmployeeBillingProfileDTO> getEmployeeBillingProfiles() {
        return employeeBillingProfiles;
    }

    public void setEmployeeBillingProfiles(
            List<EmployeeBillingProfileDTO> employeeBillingProfiles) {
        this.employeeBillingProfiles = employeeBillingProfiles;
    }

    public List<EmployeeEngagementTaskMapDTO> getEmployeeEngagementTaskMaps() {
        return employeeEngagementTaskMaps;
    }

    public void setEmployeeEngagementTaskMaps(
            List<EmployeeEngagementTaskMapDTO> employeeEngagementTaskMaps) {
        this.employeeEngagementTaskMaps = employeeEngagementTaskMaps;
    }

    public EngagementDTO getEngagement() {
        return engagement;
    }

    public void setEngagement(EngagementDTO engagement) {
        this.engagement = engagement;
    }

    public WeekPlanDTO getWeekPlan() {
        return weekPlan;
    }

    public void setWeekPlan(WeekPlanDTO weekPlan) {
        this.weekPlan = weekPlan;
    }

}
