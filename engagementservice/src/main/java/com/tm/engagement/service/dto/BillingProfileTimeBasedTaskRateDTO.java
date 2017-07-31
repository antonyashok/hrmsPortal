package com.tm.engagement.service.dto;

import java.math.BigDecimal;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class BillingProfileTimeBasedTaskRateDTO {

    private String billingProfileTimeTaskDetailId;

    private BigDecimal billToClientDTRate;

    private String billToClientEffectiveDate;

    private BigDecimal billToClientOTRate;

    private BigDecimal billToClientSTRate;

    private Double createDate;

    private Long createdBy;

    private BigDecimal endClientDTRate;

    private BigDecimal endClientOTRate;

    private BigDecimal endClientSTRate;

    private String lastUpdatedDate;

    private String poAttachedFlag;

    private Long updatedBy;

    @ManyToOne
    @JoinColumn(name = "empl_engmt_task_map_id")
    private EmployeeEngagementTaskMapDTO employeeEngagementtTaskMap;

    @ManyToOne
    @JoinColumn(name = "empl_bill_profile_id")
    private EmployeeBillingProfileDTO employeeBillingProfile;

    public String getBillingProfileTimeTaskDetailId() {
        return billingProfileTimeTaskDetailId;
    }

    public void setBillingProfileTimeTaskDetailId(String billingProfileTimeTaskDetailId) {
        this.billingProfileTimeTaskDetailId = billingProfileTimeTaskDetailId;
    }

    public BigDecimal getBillToClientDTRate() {
        return billToClientDTRate;
    }

    public void setBillToClientDTRate(BigDecimal billToClientDTRate) {
        this.billToClientDTRate = billToClientDTRate;
    }

    public String getBillToClientEffectiveDate() {
        return billToClientEffectiveDate;
    }

    public void setBillToClientEffectiveDate(String billToClientEffectiveDate) {
        this.billToClientEffectiveDate = billToClientEffectiveDate;
    }

    public BigDecimal getBillToClientOTRate() {
        return billToClientOTRate;
    }

    public void setBillToClientOTRate(BigDecimal billToClientOTRate) {
        this.billToClientOTRate = billToClientOTRate;
    }

    public BigDecimal getBillToClientSTRate() {
        return billToClientSTRate;
    }

    public void setBillToClientSTRate(BigDecimal billToClientSTRate) {
        this.billToClientSTRate = billToClientSTRate;
    }

    public Double getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Double createDate) {
        this.createDate = createDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public BigDecimal getEndClientDTRate() {
        return endClientDTRate;
    }

    public void setEndClientDTRate(BigDecimal endClientDTRate) {
        this.endClientDTRate = endClientDTRate;
    }

    public BigDecimal getEndClientOTRate() {
        return endClientOTRate;
    }

    public void setEndClientOTRate(BigDecimal endClientOTRate) {
        this.endClientOTRate = endClientOTRate;
    }

    public BigDecimal getEndClientSTRate() {
        return endClientSTRate;
    }

    public void setEndClientSTRate(BigDecimal endClientSTRate) {
        this.endClientSTRate = endClientSTRate;
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

    public EmployeeEngagementTaskMapDTO getEmployeeEngagementtTaskMap() {
        return employeeEngagementtTaskMap;
    }

    public void setEmployeeEngagementtTaskMap(
            EmployeeEngagementTaskMapDTO employeeEngagementtTaskMap) {
        this.employeeEngagementtTaskMap = employeeEngagementtTaskMap;
    }

    public EmployeeBillingProfileDTO getEmployeeBillingProfile() {
        return employeeBillingProfile;
    }

    public void setEmployeeBillingProfile(EmployeeBillingProfileDTO employeeBillingProfile) {
        this.employeeBillingProfile = employeeBillingProfile;
    }

}
