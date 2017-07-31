/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.invoice.mongo.domain.InvoiceAlertDetails.java
 * Author        : Annamalai L
 * Date Created  : Apr 11th, 2017
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

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tm.commonapi.web.core.data.IEntity;

@Document(collection = "invoiceAlertDetails")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceAlertDetails implements IEntity<ObjectId> {

	private static final long serialVersionUID = 7677626347120607148L;

	@Id
	@Column(name = "_id")
	@GeneratedValue(generator = "id")
	private ObjectId id;

	@Column
	private Date alertDate;

	@Column
	private String billToClient;

	@Column
	private String endClient;

	@Column
	private String billingSpecialist;

	@Column
	private UUID alertTypeId;

	@Column
	private String alertsType;

	@Column
	private String indicator;

	@Column
	private String alertsMessage;

	@Column
	private String linkTo;

	private AuditFields created;

	private AuditFields updated;

	@Column
	private String engmtName;
	
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
		return null;
	}

	@Override
	public void setId(ObjectId id) {
	}

	public String getEngmtName() {
		return engmtName;
	}

	public void setEngmtName(String engmtName) {
		this.engmtName = engmtName;
	}

}
