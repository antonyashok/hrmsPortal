package com.tm.invoice.mongo.dto;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tm.invoice.mongo.domain.InvoiceAlertDetails;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceAlertsDTO {

	private ObjectId id;
	private String alertsType;
	private String totalCount;

	private List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTO;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getAlertsType() {
		return alertsType;
	}

	public void setAlertsType(String alertsType) {
		this.alertsType = alertsType;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public List<InvoiceAlertDetailsDTO> getInvoiceAlertDetailsDTO() {
		return invoiceAlertDetailsDTO;
	}

	public void setInvoiceAlertDetailsDTO(List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTO) {
		this.invoiceAlertDetailsDTO = invoiceAlertDetailsDTO;
	}

}