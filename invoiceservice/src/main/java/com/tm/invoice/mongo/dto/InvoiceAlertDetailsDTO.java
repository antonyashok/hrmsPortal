/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.invoice.mongo.dto.InvoiceAlertDetailsDTO.java
 * Author        : Annamalai L
 * Date Created  : Apr 11th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.invoice.mongo.dto;

import java.util.UUID;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tm.invoice.mongo.domain.AuditFields;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceAlertDetailsDTO {

	private ObjectId id;
	private String alertDate;
	private String billToClient;
	private String endClient;
	private String billingSpecialist;
	private UUID alertTypeId;
	private String alertsType;
	private String indicator;
	private String linkTo;
	private AuditFields created;
	private AuditFields updated;
	private String engmtName;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getAlertDate() {
		return alertDate;
	}

	public void setAlertDate(String alertDate) {
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

	public String getEngmtName() {
		return engmtName;
	}

	public void setEngmtName(String engmtName) {
		this.engmtName = engmtName;
	}

}