package com.tm.invoice.dto;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class InvoicePreBillViewDTO extends ResourceSupport {

	private String billToClient;
	private String projectName;
	private String invoiceSetupName;
	private String description;
	private BigDecimal amount;
	private BigDecimal amountRemaining;
	private BigDecimal amountEarned;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private String lastDateReport;

	@Type(type = "uuid-char")
	private UUID invoiceSetupId;

	@Type(type = "uuid-char")
	private UUID engagementId;

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

	public String getInvoiceSetupName() {
		return invoiceSetupName;
	}

	public void setInvoiceSetupName(String invoiceSetupName) {
		this.invoiceSetupName = invoiceSetupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmountRemaining() {
		return amountRemaining;
	}

	public void setAmountRemaining(BigDecimal amountRemaining) {
		this.amountRemaining = amountRemaining;
	}

	public BigDecimal getAmountEarned() {
		return amountEarned;
	}

	public void setAmountEarned(BigDecimal amountEarned) {
		this.amountEarned = amountEarned;
	}

	public String getLastDateReport() {
		return lastDateReport;
	}

	public void setLastDateReport(String lastDateReport) {
		this.lastDateReport = lastDateReport;
	}

	public UUID getInvoiceSetupId() {
		return invoiceSetupId;
	}

	public void setInvoiceSetupId(UUID invoiceSetupId) {
		this.invoiceSetupId = invoiceSetupId;
	}

	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}

}
