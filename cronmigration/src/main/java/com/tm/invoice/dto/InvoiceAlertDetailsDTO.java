package com.tm.invoice.dto;

import java.util.Date;
import java.util.UUID;

import com.tm.invoice.domain.AuditFields;

public class InvoiceAlertDetailsDTO {

	/* Invoice alert details related fields */
	private Date alertDate;
	private String billToClient;
	private String endClient;
	private String billingSpecialist;
	private String indicator;
	private String alertsMessage;
	private AuditFields created;
	private AuditFields updated;
	private String engagementName;
	private UUID engagementId;
	private UUID purchaseOrderId;
	private UUID invoiceSetupId;
	private String purchaseOrderNumber;
	private Long customerId;
	private String customerName;
	private UUID invoiceId;

	/* LookupView InvoiceAlerts related fields */
	private UUID entityId;
	private String entityName;
	private UUID entityAttributeMapId;
	private String entityAttributeMapValue;
	private UUID attributeId;
	private String attributeName;
	private String attributeValue;
	private Integer sequnceNumber;

	public Date getAlertDate() {
		return alertDate;
	}

	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}

	public String getBillToClient() {
		return billToClient;
	}

	public void setBillToClient(String billToClient) {
		this.billToClient = billToClient;
	}

	public String getEndClient() {
		return endClient;
	}

	public void setEndClient(String endClient) {
		this.endClient = endClient;
	}

	public String getBillingSpecialist() {
		return billingSpecialist;
	}

	public void setBillingSpecialist(String billingSpecialist) {
		this.billingSpecialist = billingSpecialist;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getAlertsMessage() {
		return alertsMessage;
	}

	public void setAlertsMessage(String alertsMessage) {
		this.alertsMessage = alertsMessage;
	}

	public AuditFields getCreated() {
		return created;
	}

	public void setCreated(AuditFields created) {
		this.created = created;
	}

	public AuditFields getUpdated() {
		return updated;
	}

	public void setUpdated(AuditFields updated) {
		this.updated = updated;
	}

	public String getEngagementName() {
		return engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
	}

	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}

	public UUID getEntityAttributeMapId() {
		return entityAttributeMapId;
	}

	public void setEntityAttributeMapId(UUID entityAttributeMapId) {
		this.entityAttributeMapId = entityAttributeMapId;
	}

	public UUID getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(UUID purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}

	public UUID getInvoiceSetupId() {
		return invoiceSetupId;
	}

	public void setInvoiceSetupId(UUID invoiceSetupId) {
		this.invoiceSetupId = invoiceSetupId;
	}

	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public UUID getEntityId() {
		return entityId;
	}

	public void setEntityId(UUID entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public UUID getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(UUID attributeId) {
		this.attributeId = attributeId;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public Integer getSequnceNumber() {
		return sequnceNumber;
	}

	public void setSequnceNumber(Integer sequnceNumber) {
		this.sequnceNumber = sequnceNumber;
	}

	public String getEntityAttributeMapValue() {
		return entityAttributeMapValue;
	}

	public void setEntityAttributeMapValue(String entityAttributeMapValue) {
		this.entityAttributeMapValue = entityAttributeMapValue;
	}

	public UUID getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(UUID invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}