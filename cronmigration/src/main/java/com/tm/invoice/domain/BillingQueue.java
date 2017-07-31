package com.tm.invoice.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tm.invoice.domain.Task;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "billingQueue")
public class BillingQueue {

    public enum TaxExempt {
        Y, N
    }

    @Id
    private UUID id;
    private String billingProfile;
    private String engagementId;
    private Long employeeId;
    private String fileNumber;
    private Long billToClientId;
    private String billToClient;
    private String projectName;
    private String locationName;
    private String office;
    private Long officeId;
    private UUID purchaseOrderId;
    private String poNumber;
    private Long billingSpecialistId;
    private String billingSpecialist;
    private Date startDate;
    private Date profileActiveDate;
    private Date profileEndDate;
    private BigDecimal ptoAllottedHours;
    private BigDecimal ptoBalanceAccrual;
    private BigDecimal processingFeeRate;
    private BigDecimal billToClientST;
    private BigDecimal billToClientOT;
    private BigDecimal billToClientDT;
    private BigDecimal billClientrate;
    private List<Task> subTasksDetails;
    private BigDecimal specialFees;
    private BigDecimal referalsFees;
    private BigDecimal cityTax;
    private BigDecimal countyTax;
    private BigDecimal stateTax;
    private BigDecimal federalTax;
    private BigDecimal totalSalesTax;
    private TaxExempt cityTaxExempt;
    private TaxExempt countyTaxExempt;
    private TaxExempt stateTaxExempt;
    private TaxExempt federalTaxExempt;
    private BigDecimal cappedMaxSTHours;
    private BigDecimal cappedMaxOTHours;
    private BigDecimal cappedMaxDTHours;
    private String status;
    private Date lastUpdatedOn;
    
    private Long accountManagerId;
    private String effectiveStartDate;
    private String effectiveEndDate;
    private String weekPlanId;
    private String timesheetTypeLookUpId;
    private String timesheetMethodLookUpId;
    private String billTypeLookupId;
    private String timesheetRuleLookUpId;
    private String employeeEngagementId;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getBillingProfile() {
		return billingProfile;
	}
	public void setBillingProfile(String billingProfile) {
		this.billingProfile = billingProfile;
	}
	public String getEngagementId() {
		return engagementId;
	}
	public void setEngagementId(String engagementId) {
		this.engagementId = engagementId;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getFileNumber() {
		return fileNumber;
	}
	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}
	public Long getBillToClientId() {
		return billToClientId;
	}
	public void setBillToClientId(Long billToClientId) {
		this.billToClientId = billToClientId;
	}
	public String getBillToClient() {
		return billToClient;
	}
	public void setBillToClient(String billToClient) {
		this.billToClient = billToClient;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public Long getOfficeId() {
		return officeId;
	}
	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}
	public UUID getPurchaseOrderId() {
		return purchaseOrderId;
	}
	public void setPurchaseOrderId(UUID purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public Long getBillingSpecialistId() {
		return billingSpecialistId;
	}
	public void setBillingSpecialistId(Long billingSpecialistId) {
		this.billingSpecialistId = billingSpecialistId;
	}
	public String getBillingSpecialist() {
		return billingSpecialist;
	}
	public void setBillingSpecialist(String billingSpecialist) {
		this.billingSpecialist = billingSpecialist;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getProfileActiveDate() {
		return profileActiveDate;
	}
	public void setProfileActiveDate(Date profileActiveDate) {
		this.profileActiveDate = profileActiveDate;
	}
	public Date getProfileEndDate() {
		return profileEndDate;
	}
	public void setProfileEndDate(Date profileEndDate) {
		this.profileEndDate = profileEndDate;
	}
	public BigDecimal getPtoAllottedHours() {
		return ptoAllottedHours;
	}
	public void setPtoAllottedHours(BigDecimal ptoAllottedHours) {
		this.ptoAllottedHours = ptoAllottedHours;
	}
	public BigDecimal getPtoBalanceAccrual() {
		return ptoBalanceAccrual;
	}
	public void setPtoBalanceAccrual(BigDecimal ptoBalanceAccrual) {
		this.ptoBalanceAccrual = ptoBalanceAccrual;
	}
	public BigDecimal getProcessingFeeRate() {
		return processingFeeRate;
	}
	public void setProcessingFeeRate(BigDecimal processingFeeRate) {
		this.processingFeeRate = processingFeeRate;
	}
	public List<Task> getSubTasksDetails() {
		return subTasksDetails;
	}
	public void setSubTasksDetails(List<Task> subTasksDetails) {
		this.subTasksDetails = subTasksDetails;
	}
	public BigDecimal getSpecialFees() {
		return specialFees;
	}
	public void setSpecialFees(BigDecimal specialFees) {
		this.specialFees = specialFees;
	}
	public BigDecimal getReferalsFees() {
		return referalsFees;
	}
	public void setReferalsFees(BigDecimal referalsFees) {
		this.referalsFees = referalsFees;
	}
	public BigDecimal getCityTax() {
		return cityTax;
	}
	public void setCityTax(BigDecimal cityTax) {
		this.cityTax = cityTax;
	}
	public BigDecimal getCountyTax() {
		return countyTax;
	}
	public void setCountyTax(BigDecimal countyTax) {
		this.countyTax = countyTax;
	}
	public BigDecimal getStateTax() {
		return stateTax;
	}
	public void setStateTax(BigDecimal stateTax) {
		this.stateTax = stateTax;
	}
	public BigDecimal getFederalTax() {
		return federalTax;
	}
	public void setFederalTax(BigDecimal federalTax) {
		this.federalTax = federalTax;
	}
	public BigDecimal getTotalSalesTax() {
		return totalSalesTax;
	}
	public void setTotalSalesTax(BigDecimal totalSalesTax) {
		this.totalSalesTax = totalSalesTax;
	}
	public TaxExempt getCityTaxExempt() {
		return cityTaxExempt;
	}
	public void setCityTaxExempt(TaxExempt cityTaxExempt) {
		this.cityTaxExempt = cityTaxExempt;
	}
	public TaxExempt getCountyTaxExempt() {
		return countyTaxExempt;
	}
	public void setCountyTaxExempt(TaxExempt countyTaxExempt) {
		this.countyTaxExempt = countyTaxExempt;
	}
	public TaxExempt getStateTaxExempt() {
		return stateTaxExempt;
	}
	public void setStateTaxExempt(TaxExempt stateTaxExempt) {
		this.stateTaxExempt = stateTaxExempt;
	}
	public TaxExempt getFederalTaxExempt() {
		return federalTaxExempt;
	}
	public void setFederalTaxExempt(TaxExempt federalTaxExempt) {
		this.federalTaxExempt = federalTaxExempt;
	}
	public BigDecimal getCappedMaxSTHours() {
		return cappedMaxSTHours;
	}
	public void setCappedMaxSTHours(BigDecimal cappedMaxSTHours) {
		this.cappedMaxSTHours = cappedMaxSTHours;
	}
	public BigDecimal getCappedMaxOTHours() {
		return cappedMaxOTHours;
	}
	public void setCappedMaxOTHours(BigDecimal cappedMaxOTHours) {
		this.cappedMaxOTHours = cappedMaxOTHours;
	}
	public BigDecimal getCappedMaxDTHours() {
		return cappedMaxDTHours;
	}
	public void setCappedMaxDTHours(BigDecimal cappedMaxDTHours) {
		this.cappedMaxDTHours = cappedMaxDTHours;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	public Long getAccountManagerId() {
		return accountManagerId;
	}
	public void setAccountManagerId(Long accountManagerId) {
		this.accountManagerId = accountManagerId;
	}
	public String getEffectiveStartDate() {
		return effectiveStartDate;
	}
	public void setEffectiveStartDate(String effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}
	public String getEffectiveEndDate() {
		return effectiveEndDate;
	}
	public void setEffectiveEndDate(String effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}
	public String getWeekPlanId() {
		return weekPlanId;
	}
	public void setWeekPlanId(String weekPlanId) {
		this.weekPlanId = weekPlanId;
	}
	public String getTimesheetTypeLookUpId() {
		return timesheetTypeLookUpId;
	}
	public void setTimesheetTypeLookUpId(String timesheetTypeLookUpId) {
		this.timesheetTypeLookUpId = timesheetTypeLookUpId;
	}
	public String getTimesheetMethodLookUpId() {
		return timesheetMethodLookUpId;
	}
	public void setTimesheetMethodLookUpId(String timesheetMethodLookUpId) {
		this.timesheetMethodLookUpId = timesheetMethodLookUpId;
	}
	public String getBillTypeLookupId() {
		return billTypeLookupId;
	}
	public void setBillTypeLookupId(String billTypeLookupId) {
		this.billTypeLookupId = billTypeLookupId;
	}
	public String getTimesheetRuleLookUpId() {
		return timesheetRuleLookUpId;
	}
	public void setTimesheetRuleLookUpId(String timesheetRuleLookUpId) {
		this.timesheetRuleLookUpId = timesheetRuleLookUpId;
	}
	public String getEmployeeEngagementId() {
		return employeeEngagementId;
	}
	public void setEmployeeEngagementId(String employeeEngagementId) {
		this.employeeEngagementId = employeeEngagementId;
	}
	public BigDecimal getBillToClientST() {
		return billToClientST;
	}
	public void setBillToClientST(BigDecimal billToClientST) {
		this.billToClientST = billToClientST;
	}
	public BigDecimal getBillToClientOT() {
		return billToClientOT;
	}
	public void setBillToClientOT(BigDecimal billToClientOT) {
		this.billToClientOT = billToClientOT;
	}
	public BigDecimal getBillToClientDT() {
		return billToClientDT;
	}
	public void setBillToClientDT(BigDecimal billToClientDT) {
		this.billToClientDT = billToClientDT;
	}
	public BigDecimal getBillClientrate() {
		return billClientrate;
	}
	public void setBillClientrate(BigDecimal billClientrate) {
		this.billClientrate = billClientrate;
	}
	
}