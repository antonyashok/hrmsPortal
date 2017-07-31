package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "engagement_view")
public class EngagementView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4688877122490578837L;

	@Id
	@Column(name = "engmt_id")
	@Type(type = "uuid-char")
	private UUID engagementId;

	@Column(name = "engmt_nm")
	private String engagementName;

	@Column(name = "engmt_strt_dt")
	private String engmtStartDate;

	@Column(name = "engmt_end_dt")
	private String engmtEndDate;

	@Column(name = "actv_flg")
	private String activeFlag;

	@Column(name = "last_updt_dt", insertable = false)
	private String updatedDate;

	@Column(name = "project_mgr_name")
	private String projectMgrName;

	@Column(name = "customer_name")
	private String customerName;
	
	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "engmt_strt_date")
	private String startDate;
	
	@Column(name = "engmt_end_date")
	private String endDate;
	
	@Column(name = "ofc_nm")
	private String officeNames;
	
	@Column(name="ofc_id")
	private String officeIds;
	
	@Column(name="bill_to_mgr_email")
	private String billToManagerEmail;
	
	@Column(name="bill_to_mgr_name")
	private String billToMgrName;
	
	@Column(name="finance_rep_name")
	private String financeRepName;

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

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getProjectMgrName() {
		return projectMgrName;
	}

	public void setProjectMgrName(String projectMgrName) {
		this.projectMgrName = projectMgrName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getOfficeNames() {
		return officeNames;
	}

	public void setOfficeNames(String officeNames) {
		this.officeNames = officeNames;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

    public String getOfficeIds() {
        return officeIds;
    }

    public void setOfficeIds(String officeIds) {
        this.officeIds = officeIds;
    }

	public String getFinanceRepName() {
		return financeRepName;
	}

	public void setFinanceRepName(String financeRepName) {
		this.financeRepName = financeRepName;
	}

	public String getBillToManagerEmail() {
		return billToManagerEmail;
	}

	public void setBillToManagerEmail(String billToManagerEmail) {
		this.billToManagerEmail = billToManagerEmail;
	}

	public String getBillToMgrName() {
		return billToMgrName;
	}

	public void setBillToMgrName(String billToMgrName) {
		this.billToMgrName = billToMgrName;
	}
    
}