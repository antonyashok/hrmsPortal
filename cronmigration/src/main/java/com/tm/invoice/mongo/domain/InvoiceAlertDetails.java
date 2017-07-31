/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.invoice.mongo.domain.InvoiceAlertDetails.java
 * Author        : Thenmozhi P
 * Date Created  : May 4th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.invoice.mongo.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Id;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tm.commonapi.web.core.data.IEntity;
import com.tm.invoice.domain.AuditFields;

@Document(collection = "invoiceAlertDetails")
public class InvoiceAlertDetails implements IEntity<ObjectId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1313646660760087286L;

	@Id
	private ObjectId id;
	private Date alertDate;
	private String billToClient;
	private String endClient;
	private String billingSpecialist;
	private UUID alertTypeId;
	private String alertsType;
	private String indicator;
	private String alertsMessage;
	private String linkTo;
	private AuditFields created;
	private AuditFields updated;
	private String engmtName;
	private UUID engmtId;
	private UUID poId;
	private UUID invoiceSetupId;
	private UUID invoiceId;
	private String purchaseOrderNumber;
	private Long customerId;

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

	public UUID getAlertTypeId() {
		return alertTypeId;
	}

	public void setAlertTypeId(UUID alertTypeId) {
		this.alertTypeId = alertTypeId;
	}

	public String getAlertsType() {
		return alertsType;
	}

	public void setAlertsType(String alertsType) {
		this.alertsType = alertsType;
	}

	public String getAlertsMessage() {
		return alertsMessage;
	}

	public void setAlertsMessage(String alertsMessage) {
		this.alertsMessage = alertsMessage;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getLinkTo() {
		return linkTo;
	}

	public void setLinkTo(String linkTo) {
		this.linkTo = linkTo;
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

	@Override
	public ObjectId getId() {
		return id;
	}

	@Override
	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getEngmtName() {
		return engmtName;
	}

	public void setEngmtName(String engmtName) {
		this.engmtName = engmtName;
	}

	public UUID getEngmtId() {
		return engmtId;
	}

	public void setEngmtId(UUID engmtId) {
		this.engmtId = engmtId;
	}

	public UUID getPoId() {
		return poId;
	}

	public void setPoId(UUID poId) {
		this.poId = poId;
	}

	public UUID getInvoiceSetupId() {
		return invoiceSetupId;
	}

	public void setInvoiceSetupId(UUID invoiceSetupId) {
		this.invoiceSetupId = invoiceSetupId;
	}

	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
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

	public UUID getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(UUID invoiceId) {
		this.invoiceId = invoiceId;
	}
	
}