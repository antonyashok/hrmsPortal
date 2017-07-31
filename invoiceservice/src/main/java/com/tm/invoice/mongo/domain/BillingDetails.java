package com.tm.invoice.mongo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillingDetails {
	
	private String billingContact;
	private Boolean isTaxExemptState;
	private Boolean isTaxExemptCountry;
	private String invoiceTerm;
	private Double percentage;
	private String date;
	private String invoiceDeliveryMethod;
	private String invoiceDeliveryName;
	private String invoiceRecipients;
	private String billingAddress;
	private String worksiteAddress;
	public String getBillingContact() {
		return billingContact;
	}
	public void setBillingContact(String billingContact) {
		this.billingContact = billingContact;
	}
	public Boolean getIsTaxExemptState() {
		return isTaxExemptState;
	}
	public void setIsTaxExemptState(Boolean isTaxExemptState) {
		this.isTaxExemptState = isTaxExemptState;
	}
	public Boolean getIsTaxExemptCountry() {
		return isTaxExemptCountry;
	}
	public void setIsTaxExemptCountry(Boolean isTaxExemptCountry) {
		this.isTaxExemptCountry = isTaxExemptCountry;
	}
	public String getInvoiceTerm() {
		return invoiceTerm;
	}
	public void setInvoiceTerm(String invoiceTerm) {
		this.invoiceTerm = invoiceTerm;
	}
	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getInvoiceDeliveryMethod() {
		return invoiceDeliveryMethod;
	}
	public void setInvoiceDeliveryMethod(String invoiceDeliveryMethod) {
		this.invoiceDeliveryMethod = invoiceDeliveryMethod;
	}
	public String getInvoiceRecipients() {
		return invoiceRecipients;
	}
	public void setInvoiceRecipients(String invoiceRecipients) {
		this.invoiceRecipients = invoiceRecipients;
	}
	public String getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getWorksiteAddress() {
		return worksiteAddress;
	}
	public void setWorksiteAddress(String worksiteAddress) {
		this.worksiteAddress = worksiteAddress;
	}
	public String getInvoiceDeliveryName() {
		return invoiceDeliveryName;
	}
	public void setInvoiceDeliveryName(String invoiceDeliveryName) {
		this.invoiceDeliveryName = invoiceDeliveryName;
	}

	
}
