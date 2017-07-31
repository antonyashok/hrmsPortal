package com.tm.engagement.service.dto;

import java.math.BigDecimal;
import java.util.List;

public class EmployeeBillingProfileDTO {

	private String employeeBillingProfileId;

	private String activeFlag;

	private Long apContactId;

	private Long billToClientId;

	private String billTypeLookupId;

	private Long billingSpecialistEmployeeId;

	private double cappedMaxDTHrs;

	private double cappedMaxOTHrs;

	private double cappedMaxSTHrs;

	private Long clientManagerId;

	private String contractEndDate;

	private Long contractId;

	private String contractStartDate;

	private String costCenter;

	private String createDate;

	private Long createdBy;

	private String departmentNumber;

	private Long emplId;

	private String employeeProfileName;

	private Long endClientId;

	private int jobcategory;

	private String jobtitle;

	private String lastUpdatedDate;

	private String poExistFlag;

	private BigDecimal processFeeRatePct;

	private BigDecimal processReferralFee;

	private BigDecimal processSpecialFee;

	private String profileActivateDate;

	private String rateTypeLookupId;

	private String timeRuleId;

	private String timesheetEntryLookupId;

	private String timesheetMethodLookupId;

	private Long updatedBy;

	private List<BillingProfileTimeBasedTaskRateDTO> billingProfileTimeBasedTaskRate;

	private List<BillingProfileUnitsBasedTaskRateDTO> billingProfileUnitsBasedTaskRate;

	private EmployeeEngagementDTO employeeEngagement;

	public String getEmployeeBillingProfileId() {
		return employeeBillingProfileId;
	}

	public void setEmployeeBillingProfileId(String employeeBillingProfileId) {
		this.employeeBillingProfileId = employeeBillingProfileId;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Long getApContactId() {
		return apContactId;
	}

	public void setApContactId(Long apContactId) {
		this.apContactId = apContactId;
	}

	public Long getBillToClientId() {
		return billToClientId;
	}

	public void setBillToClientId(Long billToClientId) {
		this.billToClientId = billToClientId;
	}

	public String getBillTypeLookupId() {
		return billTypeLookupId;
	}

	public void setBillTypeLookupId(String billTypeLookupId) {
		this.billTypeLookupId = billTypeLookupId;
	}

	public Long getBillingSpecialistEmployeeId() {
		return billingSpecialistEmployeeId;
	}

	public void setBillingSpecialistEmployeeId(Long billingSpecialistEmployeeId) {
		this.billingSpecialistEmployeeId = billingSpecialistEmployeeId;
	}

	public double getCappedMaxDTHrs() {
		return cappedMaxDTHrs;
	}

	public void setCappedMaxDTHrs(double cappedMaxDTHrs) {
		this.cappedMaxDTHrs = cappedMaxDTHrs;
	}

	public double getCappedMaxOTHrs() {
		return cappedMaxOTHrs;
	}

	public void setCappedMaxOTHrs(double cappedMaxOTHrs) {
		this.cappedMaxOTHrs = cappedMaxOTHrs;
	}

	public double getCappedMaxSTHrs() {
		return cappedMaxSTHrs;
	}

	public void setCappedMaxSTHrs(double cappedMaxSTHrs) {
		this.cappedMaxSTHrs = cappedMaxSTHrs;
	}

	public Long getClientManagerId() {
		return clientManagerId;
	}

	public void setClientManagerId(Long clientManagerId) {
		this.clientManagerId = clientManagerId;
	}

	public String getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public String getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(String contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
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

	public String getDepartmentNumber() {
		return departmentNumber;
	}

	public void setDepartmentNumber(String departmentNumber) {
		this.departmentNumber = departmentNumber;
	}

	public Long getEmplId() {
		return emplId;
	}

	public void setEmplId(Long emplId) {
		this.emplId = emplId;
	}

	public String getEmployeeProfileName() {
		return employeeProfileName;
	}

	public void setEmployeeProfileName(String employeeProfileName) {
		this.employeeProfileName = employeeProfileName;
	}

	public Long getEndClientId() {
		return endClientId;
	}

	public void setEndClientId(Long endClientId) {
		this.endClientId = endClientId;
	}

	public int getJobcategory() {
		return jobcategory;
	}

	public void setJobcategory(int jobcategory) {
		this.jobcategory = jobcategory;
	}

	public String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getPoExistFlag() {
		return poExistFlag;
	}

	public void setPoExistFlag(String poExistFlag) {
		this.poExistFlag = poExistFlag;
	}

	public BigDecimal getProcessFeeRatePct() {
		return processFeeRatePct;
	}

	public void setProcessFeeRatePct(BigDecimal processFeeRatePct) {
		this.processFeeRatePct = processFeeRatePct;
	}

	public BigDecimal getProcessReferralFee() {
		return processReferralFee;
	}

	public void setProcessReferralFee(BigDecimal processReferralFee) {
		this.processReferralFee = processReferralFee;
	}

	public BigDecimal getProcessSpecialFee() {
		return processSpecialFee;
	}

	public void setProcessSpecialFee(BigDecimal processSpecialFee) {
		this.processSpecialFee = processSpecialFee;
	}

	public String getProfileActivateDate() {
		return profileActivateDate;
	}

	public void setProfileActivateDate(String profileActivateDate) {
		this.profileActivateDate = profileActivateDate;
	}

	public String getRateTypeLookupId() {
		return rateTypeLookupId;
	}

	public void setRateTypeLookupId(String rateTypeLookupId) {
		this.rateTypeLookupId = rateTypeLookupId;
	}

	public String getTimeRuleId() {
		return timeRuleId;
	}

	public void setTimeRuleId(String timeRuleId) {
		this.timeRuleId = timeRuleId;
	}

	public String getTimesheetEntryLookupId() {
		return timesheetEntryLookupId;
	}

	public void setTimesheetEntryLookupId(String timesheetEntryLookupId) {
		this.timesheetEntryLookupId = timesheetEntryLookupId;
	}

	public String getTimesheetMethodLookupId() {
		return timesheetMethodLookupId;
	}

	public void setTimesheetMethodLookupId(String timesheetMethodLookupId) {
		this.timesheetMethodLookupId = timesheetMethodLookupId;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<BillingProfileTimeBasedTaskRateDTO> getBillingProfileTimeBasedTaskRate() {
		return billingProfileTimeBasedTaskRate;
	}

	public void setBillingProfileTimeBasedTaskRate(
			List<BillingProfileTimeBasedTaskRateDTO> billingProfileTimeBasedTaskRate) {
		this.billingProfileTimeBasedTaskRate = billingProfileTimeBasedTaskRate;
	}

	public List<BillingProfileUnitsBasedTaskRateDTO> getBillingProfileUnitsBasedTaskRate() {
		return billingProfileUnitsBasedTaskRate;
	}

	public void setBillingProfileUnitsBasedTaskRate(
			List<BillingProfileUnitsBasedTaskRateDTO> billingProfileUnitsBasedTaskRate) {
		this.billingProfileUnitsBasedTaskRate = billingProfileUnitsBasedTaskRate;
	}

	public EmployeeEngagementDTO getEmployeeEngagement() {
		return employeeEngagement;
	}

	public void setEmployeeEngagement(EmployeeEngagementDTO employeeEngagement) {
		this.employeeEngagement = employeeEngagement;
	}

}
