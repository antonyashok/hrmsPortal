package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class InvoiceSetupDTO implements Serializable {

	private static final long serialVersionUID = -1093113301831461644L;

	private InvoiceTemplateDTO invoiceTemplate;
	private UUID invoiceSetupId;
	private String invoiceSetupName;
	private String invoiceType;
	private List<BillCycleDTO> billCycleDTOList;
	private InvoiceSetupBillCycleDTO invoiceSetupBillCycleDTO;
	private String paymentTerms;

	public InvoiceTemplateDTO getInvoiceTemplate() {
		return invoiceTemplate;
	}

	public void setInvoiceTemplate(InvoiceTemplateDTO invoiceTemplate) {
		this.invoiceTemplate = invoiceTemplate;
	}

	public UUID getInvoiceSetupId() {
		return invoiceSetupId;
	}

	public void setInvoiceSetupId(UUID invoiceSetupId) {
		this.invoiceSetupId = invoiceSetupId;
	}

	public String getInvoiceSetupName() {
		return invoiceSetupName;
	}

	public void setInvoiceSetupName(String invoiceSetupName) {
		this.invoiceSetupName = invoiceSetupName;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public List<BillCycleDTO> getBillCycleDTOList() {
		return billCycleDTOList;
	}

	public void setBillCycleDTOList(List<BillCycleDTO> billCycleDTOList) {
		this.billCycleDTOList = billCycleDTOList;
	}

	public InvoiceSetupBillCycleDTO getInvoiceSetupBillCycleDTO() {
		return invoiceSetupBillCycleDTO;
	}

	public void setInvoiceSetupBillCycleDTO(InvoiceSetupBillCycleDTO invoiceSetupBillCycleDTO) {
		this.invoiceSetupBillCycleDTO = invoiceSetupBillCycleDTO;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

}
