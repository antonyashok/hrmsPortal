package com.tm.expense.domain;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "expense")
@JsonInclude(Include.NON_NULL)
public class Expense extends AbstractAuditingEntity implements java.io.Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6108426695105856780L;

	@Id
	@Column(name = "expense_id")
	@Type(type = "uuid-char")
	private UUID expensesUUID;

	@Column(name = "expense_type_id")
	@Type(type = "uuid-char")
	private UUID expenseTypeUUID;

	@Column(name = "expense_report_id")
	@Type(type = "uuid-char")
	private UUID expenseReportUUID;

	@Column(name = "attached")
	private Boolean attached;

	@Column(name = "receipts_id")
	@Type(type = "uuid-char")
	private UUID receiptsUUID;

	@Column(name = "description")
	private String description;

	@Column(name = "billable")
	private Boolean billable;

	@Column(name = "status")
	private String status;

	@Column(name = "limitAmount")
	private BigDecimal limitAmount;

	@Column(name = "limitExceed")
	private Boolean limitExceed = false;

	@Column(name = "approver_id")
	private String approverId;

	@Column(name = "isRejected")
	private Boolean isRejected = false;

	@Column(name = "receipt_verified")
	private Boolean receiptVerified = false;

	@Column(name = "approval_needed")
	private Boolean approvalNeeded = false;

	@Column(name = "approval_received")
	private Boolean approvalReceived = false;

	@Column(name = "automatic")
	private Boolean automatic = false;

	@Column(name = "is_taxable")
	private String isTaxable = "N";

	public UUID getExpensesUUID() {
		return expensesUUID;
	}

	public void setExpensesUUID(UUID expensesUUID) {
		this.expensesUUID = expensesUUID;
	}

	public UUID getExpenseTypeUUID() {
		return expenseTypeUUID;
	}

	public void setExpenseTypeUUID(UUID expenseTypeUUID) {
		this.expenseTypeUUID = expenseTypeUUID;
	}

	public UUID getExpenseReportUUID() {
		return expenseReportUUID;
	}

	public void setExpenseReportUUID(UUID expenseReportUUID) {
		this.expenseReportUUID = expenseReportUUID;
	}

	public Boolean getAttached() {
		return attached;
	}

	public void setAttached(Boolean attached) {
		this.attached = attached;
	}

	public UUID getReceiptsUUID() {
		return receiptsUUID;
	}

	public void setReceiptsUUID(UUID receiptsUUID) {
		this.receiptsUUID = receiptsUUID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(BigDecimal limitAmount) {
		this.limitAmount = limitAmount;
	}

	public Boolean getLimitExceed() {
		return limitExceed;
	}

	public void setLimitExceed(Boolean limitExceed) {
		this.limitExceed = limitExceed;
	}

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public Boolean getIsRejected() {
		return isRejected;
	}

	public void setIsRejected(Boolean isRejected) {
		this.isRejected = isRejected;
	}

	public Boolean getReceiptVerified() {
		return receiptVerified;
	}

	public void setReceiptVerified(Boolean receiptVerified) {
		this.receiptVerified = receiptVerified;
	}

	public Boolean getApprovalNeeded() {
		return approvalNeeded;
	}

	public void setApprovalNeeded(Boolean approvalNeeded) {
		this.approvalNeeded = approvalNeeded;
	}

	public Boolean getApprovalReceived() {
		return approvalReceived;
	}

	public void setApprovalReceived(Boolean approvalReceived) {
		this.approvalReceived = approvalReceived;
	}

	public Boolean getAutomatic() {
		return automatic;
	}

	public void setAutomatic(Boolean automatic) {
		this.automatic = automatic;
	}

	public String getIsTaxable() {
		return isTaxable;
	}

	public void setIsTaxable(String isTaxable) {
		this.isTaxable = isTaxable;
	}

}
