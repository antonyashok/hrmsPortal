package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

public class TimesheetPDFDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 7798348751734603495L;


	private String projectName;
	
	private String invoicePeriod;
	
	private String poNumber;
	
	private String financeRepresentName;
	
	private String billingSpecialist;
	
	private String accountManager;
	
	private List<InvoiceContractorDTO> invoiceContractorDTOList;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getInvoicePeriod() {
		return invoicePeriod;
	}

	public void setInvoicePeriod(String invoicePeriod) {
		this.invoicePeriod = invoicePeriod;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getFinanceRepresentName() {
		return financeRepresentName;
	}

	public void setFinanceRepresentName(String financeRepresentName) {
		this.financeRepresentName = financeRepresentName;
	}

	public String getBillingSpecialist() {
		return billingSpecialist;
	}

	public void setBillingSpecialist(String billingSpecialist) {
		this.billingSpecialist = billingSpecialist;
	}

	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	public List<InvoiceContractorDTO> getInvoiceContractorDTOList() {
		return invoiceContractorDTOList;
	}

	public void setInvoiceContractorDTOList(List<InvoiceContractorDTO> invoiceContractorDTOList) {
		this.invoiceContractorDTOList = invoiceContractorDTOList;
	}


	
}
