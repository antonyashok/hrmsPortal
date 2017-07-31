package com.tm.engagement.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class TaskDefinitionDTO {

    public enum BillType {
        Hours, Unit
    }

    private UUID taskId;

    private String taskDescription;

    private String taskName;

    private UUID engagementId;

    private Long employeeId;

    private BigDecimal billToClientDTRate;

    private String billToClientEffectiveDate;

    private BigDecimal billToClientOTRate;

    private BigDecimal billToClientSTRate;

    @Enumerated(EnumType.STRING)
    private BillType billType;

    private UUID employeeBillingProfileId;

    private BigDecimal billToClientRate;

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public UUID getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(UUID engagementId) {
        this.engagementId = engagementId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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

    public BillType getBillType() {
        return billType;
    }

    public void setBillType(BillType billType) {
        this.billType = billType;
    }

    public UUID getEmployeeBillingProfileId() {
        return employeeBillingProfileId;
    }

    public void setEmployeeBillingProfileId(UUID employeeBillingProfileId) {
        this.employeeBillingProfileId = employeeBillingProfileId;
    }

    public BigDecimal getBillToClientRate() {
        return billToClientRate;
    }

    public void setBillToClientRate(BigDecimal billToClientRate) {
        this.billToClientRate = billToClientRate;
    }

}
