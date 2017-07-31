package com.tm.engagement.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "engagement")
public class Engagement implements Serializable {

	private static final long serialVersionUID = 3133662292804564519L;

	public enum ActiveFlagEnum {
		Y, N
	}

	@Id
	@Column(name = "engmt_id")
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID engagementId;

	@Column(name = "engmt_nm")
	private String engagementName;

	@Column(name = "engmt_lead_am_empl_id")
	private Long engmtLeadEmployeeId;

	@Column(name = "cust_overall_org_id")
	private Long customerOrganizationId;

	@Column(name = "engmt_strt_dt")
	private Date engmtStartDate;

	@Column(name = "engmt_end_dt")
	private Date engmtEndDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private ActiveFlagEnum activeFlag;

	@Column(name = "hldy_hrs")
	private Double holidayHours;

	/*@Column(name = "create_dt")
	private Date createdDate;

	@Column(name = "last_updt_dt", insertable = false)
	private Date updatedDate;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "updated_by")
	private Long updatedBy;*/

	@Column(name = "engmt_description")
	private String engmtDescription;

	@Column(name = "skill_set")
	private String skillSet;

	@Column(name = "bill_to_mgr_name")
	private String billToMgrName;

	@Column(name = "bill_to_mgr_email")
	private String billToMgrEmail;

	@Column(name = "project_mgr_name")
	private String projectMgrName;

	@Column(name = "project_mgr_email")
	private String projectMgrEmail;

	@Column(name = "finance_mgr_id")
	private Long financeMgrId;

	@Column(name = "finance_rep_id")
	private Long financeRepId;

	@Column(name = "currency_name")
	private String currencyName;

	@Column(name = "hiring_mgr_name")
	private String hiringMgrName;

	@Column(name = "hiring_mgr_email")
	private String hiringMgrEmail;

	@Column(name = "bill_address")
	private String billAddress;

	@Column(name = "country_id")
	private Long countryId;

	@Column(name = "postal_code")
	private String postalCode;
	
	@Column(name = "state_id")
	private Long stateId;
	
	@Column(name = "city_id")
	private Long cityId;
	
	@Column(name="line_of_business")
	@Type(type = "uuid-char")
	private UUID lineOfBusinessId;
	
	@Column(name="terms")
	@Type(type = "uuid-char")
	private UUID terms;
	
    @Column(name = "revenue_po_id")
    @Type(type = "uuid-char")
    private UUID revenuePurchaseOrderId;

    @Column(name = "revenue_po_number")
    private String revenuePoNumber;

    @Column(name = "initial_revenue_amount")
    private BigDecimal initialRevenueAmount;

    @Column(name = "total_revenue_amount")
    private BigDecimal totalRevenueAmount;

    @Column(name = "balance_revenue_amount")
    private BigDecimal balanceRevenueAmount;

    @Column(name = "expense_po_id")
    @Type(type = "uuid-char")
    private UUID expensePurchaseOrderId;

    @Column(name = "expense_po_number")
    private String expensePoNumber;

    @Column(name = "initial_expense_amount")
    private BigDecimal initialExpenseAmount;

    @Column(name = "total_expense_amount")
    private BigDecimal totalExpenseAmount;

    @Column(name = "balance_expense_amount")
    private BigDecimal balanceExpenseAmount;

    @Column(name = "revenue_po_issue_dt")
    private Date revenuePoIssueDate;

    @Column(name = "expense_po_issue_dt")
    private Date expensePoIssueDate;
    
	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}

	public String getEngagementName() {
		return engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
	}

	public Long getEngmtLeadEmployeeId() {
		return engmtLeadEmployeeId;
	}

	public void setEngmtLeadEmployeeId(Long engmtLeadEmployeeId) {
		this.engmtLeadEmployeeId = engmtLeadEmployeeId;
	}

	public Long getCustomerOrganizationId() {
		return customerOrganizationId;
	}

	public void setCustomerOrganizationId(Long customerOrganizationId) {
		this.customerOrganizationId = customerOrganizationId;
	}

	public Date getEngmtStartDate() {
		return engmtStartDate;
	}

	public void setEngmtStartDate(Date engmtStartDate) {
		this.engmtStartDate = engmtStartDate;
	}

	public Date getEngmtEndDate() {
		return engmtEndDate;
	}

	public void setEngmtEndDate(Date engmtEndDate) {
		this.engmtEndDate = engmtEndDate;
	}

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Double getHolidayHours() {
		return holidayHours;
	}

	public void setHolidayHours(Double holidayHours) {
		this.holidayHours = holidayHours;
	}

	/*public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}*/

	public String getEngmtDescription() {
		return engmtDescription;
	}

	public void setEngmtDescription(String engmtDescription) {
		this.engmtDescription = engmtDescription;
	}

	public String getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(String skillSet) {
		this.skillSet = skillSet;
	}

	public String getBillToMgrEmail() {
		return billToMgrEmail;
	}

	public void setBillToMgrEmail(String billToMgrEmail) {
		this.billToMgrEmail = billToMgrEmail;
	}

	public String getBillToMgrName() {
		return billToMgrName;
	}

	public void setBillToMgrName(String billToMgrName) {
		this.billToMgrName = billToMgrName;
	}

	public String getProjectMgrName() {
		return projectMgrName;
	}

	public void setProjectMgrName(String projectMgrName) {
		this.projectMgrName = projectMgrName;
	}

	public String getProjectMgrEmail() {
		return projectMgrEmail;
	}

	public void setProjectMgrEmail(String projectMgrEmail) {
		this.projectMgrEmail = projectMgrEmail;
	}

	public Long getFinanceMgrId() {
		return financeMgrId;
	}

	public void setFinanceMgrId(Long financeMgrId) {
		this.financeMgrId = financeMgrId;
	}

	public Long getFinanceRepId() {
		return financeRepId;
	}

	public void setFinanceRepId(Long financeRepId) {
		this.financeRepId = financeRepId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getHiringMgrName() {
		return hiringMgrName;
	}

	public void setHiringMgrName(String hiringMgrName) {
		this.hiringMgrName = hiringMgrName;
	}

	public String getHiringMgrEmail() {
		return hiringMgrEmail;
	}

	public void setHiringMgrEmail(String hiringMgrEmail) {
		this.hiringMgrEmail = hiringMgrEmail;
	}

	public String getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(String billAddress) {
		this.billAddress = billAddress;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

    public UUID getLineOfBusinessId() {
        return lineOfBusinessId;
    }

    public void setLineOfBusinessId(UUID lineOfBusinessId) {
        this.lineOfBusinessId = lineOfBusinessId;
    }

    public UUID getTerms() {
        return terms;
    }

    public void setTerms(UUID terms) {
        this.terms = terms;
    }

    public UUID getRevenuePurchaseOrderId() {
        return revenuePurchaseOrderId;
    }

    public void setRevenuePurchaseOrderId(UUID revenuePurchaseOrderId) {
        this.revenuePurchaseOrderId = revenuePurchaseOrderId;
    }

    public String getRevenuePoNumber() {
        return revenuePoNumber;
    }

    public void setRevenuePoNumber(String revenuePoNumber) {
        this.revenuePoNumber = revenuePoNumber;
    }

    public BigDecimal getInitialRevenueAmount() {
        return initialRevenueAmount;
    }

    public void setInitialRevenueAmount(BigDecimal initialRevenueAmount) {
        this.initialRevenueAmount = initialRevenueAmount;
    }

    public BigDecimal getTotalRevenueAmount() {
        return totalRevenueAmount;
    }

    public void setTotalRevenueAmount(BigDecimal totalRevenueAmount) {
        this.totalRevenueAmount = totalRevenueAmount;
    }

    public BigDecimal getBalanceRevenueAmount() {
        return balanceRevenueAmount;
    }

    public void setBalanceRevenueAmount(BigDecimal balanceRevenueAmount) {
        this.balanceRevenueAmount = balanceRevenueAmount;
    }

    public UUID getExpensePurchaseOrderId() {
        return expensePurchaseOrderId;
    }

    public void setExpensePurchaseOrderId(UUID expensePurchaseOrderId) {
        this.expensePurchaseOrderId = expensePurchaseOrderId;
    }

    public String getExpensePoNumber() {
        return expensePoNumber;
    }

    public void setExpensePoNumber(String expensePoNumber) {
        this.expensePoNumber = expensePoNumber;
    }

    public BigDecimal getInitialExpenseAmount() {
        return initialExpenseAmount;
    }

    public void setInitialExpenseAmount(BigDecimal initialExpenseAmount) {
        this.initialExpenseAmount = initialExpenseAmount;
    }

    public BigDecimal getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public void setTotalExpenseAmount(BigDecimal totalExpenseAmount) {
        this.totalExpenseAmount = totalExpenseAmount;
    }

    public BigDecimal getBalanceExpenseAmount() {
        return balanceExpenseAmount;
    }

    public void setBalanceExpenseAmount(BigDecimal balanceExpenseAmount) {
        this.balanceExpenseAmount = balanceExpenseAmount;
    }

	public Date getRevenuePoIssueDate() {
		return revenuePoIssueDate;
	}

	public void setRevenuePoIssueDate(Date revenuePoIssueDate) {
		this.revenuePoIssueDate = revenuePoIssueDate;
	}

	public Date getExpensePoIssueDate() {
		return expensePoIssueDate;
	}

	public void setExpensePoIssueDate(Date expensePoIssueDate) {
		this.expensePoIssueDate = expensePoIssueDate;
	}
    
}