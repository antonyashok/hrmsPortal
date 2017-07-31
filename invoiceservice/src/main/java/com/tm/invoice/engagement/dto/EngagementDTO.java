package com.tm.invoice.engagement.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

public class EngagementDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -3996522176189454337L;

	private static final String ENGAGEMENT_NAME_IS_REQUIRED = "Engagement Name is Required";
	private static final String CUSTOMER_NAME_IS_REQUIRED = "Customer Name is Required";
	private static final String ENGMT_START_DATE_IS_REQUIRED = "Engagement Start Date is Required";
	private static final String ENGMT_END_DATE_IS_REQUIRED = "Engagement End Date is Required";
	private static final String BILL_MGR_IS_REQUIRED = "Client Account Manager is Required";
	private static final String BILL_MGR_EMAIL_IS_REQUIRED = "Client Account Manager Email is Required";
	private static final String PROJECT_MGR_IS_REQUIRED = "Project Manager Name is Required";
	private static final String PROJECT_MGR_EMIAL_IS_REQUIRED = "Project Manager Email is Required";
	private static final String HIRING_MGR_IS_REQUIRED = "Hiring Manager is Required";
	private static final String HIRING_MGR_EMAIL_IS_REQUIRED = "Hiring Manager Email is Required";
	private static final String CURRENCY_IS_REQUIRED = "Currency is Required";
	private static final String ADDRESS_NAME_IS_REQUIRED = "Address is Required";
	private static final String POSTAL_IS_REQUIRED = "Address is Required";

	@Type(type = "uuid-char")
	private UUID engagementId;
	@NotBlank(message = ENGAGEMENT_NAME_IS_REQUIRED)
	private String engagementName;
	private Long engmtLeadEmployeeId;
	@NotNull(message = CUSTOMER_NAME_IS_REQUIRED)
	private Long customerOrganizationId;
	@NotBlank(message = ENGMT_START_DATE_IS_REQUIRED)
	private String engmtStartDate;
	@NotBlank(message = ENGMT_END_DATE_IS_REQUIRED)
	private String engmtEndDate;
	private String activeFlag;
	private Date createdDate;
	private Date updatedDate;
	private Long createdBy;
	private Long updatedBy;
	private String engmtDescription;
	private String skillSet;
	@NotBlank(message = BILL_MGR_EMAIL_IS_REQUIRED)
	private String billToMgrEmail;
	@NotBlank(message = BILL_MGR_IS_REQUIRED)
	private String billToMgrName;
	@NotBlank(message = PROJECT_MGR_IS_REQUIRED)
	private String projectMgrName;
	@NotBlank(message = PROJECT_MGR_EMIAL_IS_REQUIRED)
	private String projectMgrEmail;
	private Long financeMgrId;
	private Long financeRepId;
	@NotBlank(message = CURRENCY_IS_REQUIRED)
	private String currencyName;
	@NotBlank(message = HIRING_MGR_IS_REQUIRED)
	private String hiringMgrName;
	@NotBlank(message = HIRING_MGR_EMAIL_IS_REQUIRED)
	private String hiringMgrEmail;

	@NotBlank(message = ADDRESS_NAME_IS_REQUIRED)
	private String billAddress;
	private Long countryId;
	
	@NotBlank(message = POSTAL_IS_REQUIRED)
	private String postalCode;
	
	private Long stateId;
	private Long cityId;
	@Type(type = "uuid-char")
    private UUID lineOfBusinessId;
	@Type(type = "uuid-char")
    private UUID terms;
    @Type(type = "uuid-char")
    private UUID revenuePurchaseOrderId;
    private String revenuePoNumber;
    private BigDecimal initialRevenueAmount;
    private BigDecimal totalRevenueAmount;
    private BigDecimal balanceRevenueAmount;
    @Type(type = "uuid-char")
    private UUID expensePurchaseOrderId;
    private String expensePoNumber;
    private BigDecimal initialExpenseAmount;
    private BigDecimal totalExpenseAmount;
    private BigDecimal balanceExpenseAmount;
    private String revenuePoIssueDate;
    private String expensePoIssueDate;

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

	public String getEngmtStartDate() {
		return engmtStartDate;
	}

	public void setEngmtStartDate(String engmtStartDate) {
		this.engmtStartDate = engmtStartDate;
	}

	public String getEngmtEndDate() {
		return engmtEndDate;
	}

	public void setEngmtEndDate(String engmtEndDate) {
		this.engmtEndDate = engmtEndDate;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Date getCreatedDate() {
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
	}

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

    public String getRevenuePoIssueDate() {
        return revenuePoIssueDate;
    }

    public void setRevenuePoIssueDate(String revenuePoIssueDate) {
        this.revenuePoIssueDate = revenuePoIssueDate;
    }

    public String getExpensePoIssueDate() {
        return expensePoIssueDate;
    }

    public void setExpensePoIssueDate(String expensePoIssueDate) {
        this.expensePoIssueDate = expensePoIssueDate;
    }

}