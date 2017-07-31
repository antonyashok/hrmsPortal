package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PurchaseOrderDTO implements Serializable {

  private static final long serialVersionUID = -7978370181081748193L;

    private UUID purchaseOrderId;
	private Date poEndDate;
	private String poNumber;
	private Date poStartDate;
	private String poStatus;

	private InvoiceSetupDTO invoiceSetup;
	private List<BillingProfileDTO> billingProfiles;
	public UUID getPurchaseOrderId() {
		return purchaseOrderId;
	}
	public void setPurchaseOrderId(UUID purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}
	public Date getPoEndDate() {
		return poEndDate;
	}
	public void setPoEndDate(Date poEndDate) {
		this.poEndDate = poEndDate;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public Date getPoStartDate() {
		return poStartDate;
	}
	public void setPoStartDate(Date poStartDate) {
		this.poStartDate = poStartDate;
	}
	public String getPoStatus() {
		return poStatus;
	}
	public void setPoStatus(String poStatus) {
		this.poStatus = poStatus;
	}
	public InvoiceSetupDTO getInvoiceSetup() {
		return invoiceSetup;
	}
	public void setInvoiceSetup(InvoiceSetupDTO invoiceSetup) {
		this.invoiceSetup = invoiceSetup;
	}
	public List<BillingProfileDTO> getBillingProfiles() {
		return billingProfiles;
	}
	public void setBillingProfiles(List<BillingProfileDTO> billingProfiles) {
		this.billingProfiles = billingProfiles;
	}

	
}
