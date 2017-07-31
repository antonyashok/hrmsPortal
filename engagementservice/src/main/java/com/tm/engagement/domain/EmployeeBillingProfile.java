package com.tm.engagement.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "empl_bill_profile")
public class EmployeeBillingProfile extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = -2280221831468150784L;

    @Id
    @Column(name = "empl_bill_profile_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID employeeBillingProfileId;

    @Column(name = "actv_flg")
    private String activeFlag;

    @Column(name = "ap_contact_id")
    private Long apContactId;

    @Column(name = "bill_to_cli_id")
    private Long billToClientId;

    @Column(name = "bill_type_lkp_id")
    private String billTypeLookupId;

    @Column(name = "bs_empl_id")
    private Long billingSpecialistEmployeeId;

    @Column(name = "capped_max_dt_hrs")
    private Double cappedMaxDTHrs;

    @Column(name = "capped_max_ot_hrs")
    private Double cappedMaxOTHrs;

    @Column(name = "capped_max_st_hrs")
    private Double cappedMaxSTHrs;

    @Column(name = "client_mgr_id")
    private Long clientManagerId;

    @Temporal(TemporalType.DATE)
    @Column(name = "contract_end_dt")
    private Date contractEndDate;

    @Column(name = "contract_id")
    private Long contractId;

    @Temporal(TemporalType.DATE)
    @Column(name = "contract_strt_dt")
    private Date contractStartDate;

    @Column(name = "cost_center")
    private String costCenter;

    @Column(name = "dept_no")
    private String departmentNumber;

    @Column(name = "empl_id")
    private Long emplId;

    @Column(name = "empl_profile_name")
    private String employeeProfileName;

    @Column(name = "end_cli_id")
    private Long endClientId;

    private int jobcategory;

    private String jobtitle;

    @Column(name = "po_exist_flg")
    private String poExistFlag;

    @Column(name = "proc_fee_rate_pct")
    private BigDecimal processFeeRatePct;

    @Column(name = "proc_referral_fee")
    private BigDecimal processReferralFee;

    @Column(name = "proc_special_fee")
    private BigDecimal processSpecialFee;

    @Temporal(TemporalType.DATE)
    @Column(name = "prof_actvdt_dt")
    private Date profileActivateDate;

    @Column(name = "rate_type_lkp_id")
    private String rateTypeLookupId;

    @Column(name = "time_rule_id")
    private String timeRuleId;

    @Column(name = "ts_entry_lkp_id")
    private String timesheetEntryLookupId;

    @Column(name = "ts_method_lkp_id")
    private String timesheetMethodLookupId;
    
	@Column(name = "po_id")
	@Type(type = "uuid-char")
	private UUID purchaseOrderId;
    
    @ManyToOne
    @JoinColumn(name = "empl_engmt_id")
    private EmployeeEngagement employeeEngagement;
    
    public UUID getEmployeeBillingProfileId() {
        return employeeBillingProfileId;
    }

    public void setEmployeeBillingProfileId(UUID employeeBillingProfileId) {
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

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Date getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
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

    public Date getProfileActivateDate() {
        return profileActivateDate;
    }

    public void setProfileActivateDate(Date profileActivateDate) {
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

    public EmployeeEngagement getEmployeeEngagement() {
        return employeeEngagement;
    }

    public void setEmployeeEngagement(EmployeeEngagement employeeEngagement) {
        this.employeeEngagement = employeeEngagement;
    }

	public UUID getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(UUID purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public void setCappedMaxDTHrs(Double cappedMaxDTHrs) {
		this.cappedMaxDTHrs = cappedMaxDTHrs;
	}

	public void setCappedMaxOTHrs(Double cappedMaxOTHrs) {
		this.cappedMaxOTHrs = cappedMaxOTHrs;
	}

	public void setCappedMaxSTHrs(Double cappedMaxSTHrs) {
		this.cappedMaxSTHrs = cappedMaxSTHrs;
	}
    
    

}
