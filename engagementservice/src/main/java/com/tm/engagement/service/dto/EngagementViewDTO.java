package com.tm.engagement.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class EngagementViewDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -6404521848959767161L;

	private UUID engagementId;
	private String engagementName;
	private String engmtStartDate;
	private String engmtEndDate;
	private String activeFlag;
	private String updatedDate;
	private String projectMgrName;
	private String customerName;
	private String officeNames;
	private String startDate;
	private String endDate;
	private Long customerId;
	private String officeIds;
	private String financeRepName;
	private String billToMgrName;
	private String billToManagerEmail;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

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

	public String getBillToMgrName() {
		return billToMgrName;
	}

	public void setBillToMgrName(String billToMgrName) {
		this.billToMgrName = billToMgrName;
	}

	public String getBillToManagerEmail() {
		return billToManagerEmail;
	}

	public void setBillToManagerEmail(String billToManagerEmail) {
		this.billToManagerEmail = billToManagerEmail;
	}

}
