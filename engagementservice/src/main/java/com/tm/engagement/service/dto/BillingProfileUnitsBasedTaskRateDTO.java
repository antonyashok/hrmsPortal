package com.tm.engagement.service.dto;

import java.math.BigDecimal;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class BillingProfileUnitsBasedTaskRateDTO {

    private String billingProfileUnitsTaskRateDatailId;

    private String billToClientEffectiveDate;

    private BigDecimal billToClientRate;

    private String createDate;

    private Long createdBy;

    private BigDecimal endClientRate;

    private String lastUpdatedDate;

    private String poAttachedFlag;

    private Long updatedBy;

    @ManyToOne
    @JoinColumn(name = "empl_engmt_task_map_id")
    private EmployeeEngagementTaskMapDTO employeeEngagementTaskMap;

    @ManyToOne
    @JoinColumn(name = "empl_bill_profile_id")
    private EmployeeBillingProfileDTO employeeBillingProfile;

    public String getBillingProfileUnitsTaskRateDatailId() {
        return billingProfileUnitsTaskRateDatailId;
    }

    public void setBillingProfileUnitsTaskRateDatailId(String billingProfileUnitsTaskRateDatailId) {
        this.billingProfileUnitsTaskRateDatailId = billingProfileUnitsTaskRateDatailId;
    }

    public String getBillToClientEffectiveDate() {
        return billToClientEffectiveDate;
    }

    public void setBillToClientEffectiveDate(String billToClientEffectiveDate) {
        this.billToClientEffectiveDate = billToClientEffectiveDate;
    }

    public BigDecimal getBillToClientRate() {
        return billToClientRate;
    }

    public void setBillToClientRate(BigDecimal billToClientRate) {
        this.billToClientRate = billToClientRate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public BigDecimal getEndClientRate() {
        return endClientRate;
    }

    public void setEndClientRate(BigDecimal endClientRate) {
        this.endClientRate = endClientRate;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getPoAttachedFlag() {
        return poAttachedFlag;
    }

    public void setPoAttachedFlag(String poAttachedFlag) {
        this.poAttachedFlag = poAttachedFlag;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public EmployeeEngagementTaskMapDTO getEmployeeEngagementTaskMap() {
        return employeeEngagementTaskMap;
    }

    public void setEmployeeEngagementTaskMap(
            EmployeeEngagementTaskMapDTO employeeEngagementTaskMap) {
        this.employeeEngagementTaskMap = employeeEngagementTaskMap;
    }

    public EmployeeBillingProfileDTO getEmployeeBillingProfile() {
        return employeeBillingProfile;
    }

    public void setEmployeeBillingProfile(EmployeeBillingProfileDTO employeeBillingProfile) {
        this.employeeBillingProfile = employeeBillingProfile;
    }

}
