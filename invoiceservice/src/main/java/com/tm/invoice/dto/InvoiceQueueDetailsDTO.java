package com.tm.invoice.dto;

import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceQueueDetailsDTO extends ResourceSupport {

	private UUID invoiceQueueDetailId;	
	private UUID invoiceQueueId;
	private UUID invoiceSetupId;	
	private Long contractorId;
	private String contractorName;
	private UUID poId;	
	private Double salesTaxRate;	
	private Double salesTaxAmount;	
	private Double cityTaxRate;	
	private Double cityTaxAmount;	
	private Double countryTaxRate;	
	private Double countryTaxAmount;	
	private Double stateTaxRate;	
	private Double stateTaxAmount;	
	private Double federalTaxRate;	
	private Double federalTaxAmount;	
	private Double totalTaxAmount;	
	private Double stHours;	
	private Double stAmount;	
	private Double otHours;	
	private Double otAmount;	
	private Double dtHours;	
	private Double dtAmount;	
	private Double totalNoOfHoursWorked;	
	private Double totalAmount;	
	private String startDate;	
	private String endDate;
	private String period;
	private String currencyType = "USD";
	
	public UUID getInvoiceQueueDetailId() {
		return invoiceQueueDetailId;
	}
	public void setInvoiceQueueDetailId(UUID invoiceQueueDetailId) {
		this.invoiceQueueDetailId = invoiceQueueDetailId;
	}
	public UUID getInvoiceQueueId() {
		return invoiceQueueId;
	}
	public void setInvoiceQueueId(UUID invoiceQueueId) {
		this.invoiceQueueId = invoiceQueueId;
	}
	public UUID getInvoiceSetupId() {
		return invoiceSetupId;
	}
	public void setInvoiceSetupId(UUID invoiceSetupId) {
		this.invoiceSetupId = invoiceSetupId;
	}
	public Long getContractorId() {
		return contractorId;
	}
	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}
	public String getContractorName() {
		return contractorName;
	}
	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}
	public UUID getPoId() {
		return poId;
	}
	public void setPoId(UUID poId) {
		this.poId = poId;
	}
	public Double getSalesTaxRate() {
		return salesTaxRate;
	}
	public void setSalesTaxRate(Double salesTaxRate) {
		this.salesTaxRate = salesTaxRate;
	}
	public Double getSalesTaxAmount() {
		return salesTaxAmount;
	}
	public void setSalesTaxAmount(Double salesTaxAmount) {
		this.salesTaxAmount = salesTaxAmount;
	}
	public Double getCityTaxRate() {
		return cityTaxRate;
	}
	public void setCityTaxRate(Double cityTaxRate) {
		this.cityTaxRate = cityTaxRate;
	}
	public Double getCityTaxAmount() {
		return cityTaxAmount;
	}
	public void setCityTaxAmount(Double cityTaxAmount) {
		this.cityTaxAmount = cityTaxAmount;
	}
	public Double getCountryTaxRate() {
		return countryTaxRate;
	}
	public void setCountryTaxRate(Double countryTaxRate) {
		this.countryTaxRate = countryTaxRate;
	}
	public Double getCountryTaxAmount() {
		return countryTaxAmount;
	}
	public void setCountryTaxAmount(Double countryTaxAmount) {
		this.countryTaxAmount = countryTaxAmount;
	}
	public Double getStateTaxRate() {
		return stateTaxRate;
	}
	public void setStateTaxRate(Double stateTaxRate) {
		this.stateTaxRate = stateTaxRate;
	}
	public Double getStateTaxAmount() {
		return stateTaxAmount;
	}
	public void setStateTaxAmount(Double stateTaxAmount) {
		this.stateTaxAmount = stateTaxAmount;
	}
	public Double getFederalTaxRate() {
		return federalTaxRate;
	}
	public void setFederalTaxRate(Double federalTaxRate) {
		this.federalTaxRate = federalTaxRate;
	}
	public Double getFederalTaxAmount() {
		return federalTaxAmount;
	}
	public void setFederalTaxAmount(Double federalTaxAmount) {
		this.federalTaxAmount = federalTaxAmount;
	}
	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}
	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}
	public Double getStHours() {
		return stHours;
	}
	public void setStHours(Double stHours) {
		this.stHours = stHours;
	}
	public Double getStAmount() {
		return stAmount;
	}
	public void setStAmount(Double stAmount) {
		this.stAmount = stAmount;
	}
	public Double getOtHours() {
		return otHours;
	}
	public void setOtHours(Double otHours) {
		this.otHours = otHours;
	}
	public Double getOtAmount() {
		return otAmount;
	}
	public void setOtAmount(Double otAmount) {
		this.otAmount = otAmount;
	}
	public Double getDtHours() {
		return dtHours;
	}
	public void setDtHours(Double dtHours) {
		this.dtHours = dtHours;
	}
	public Double getDtAmount() {
		return dtAmount;
	}
	public void setDtAmount(Double dtAmount) {
		this.dtAmount = dtAmount;
	}
	public Double getTotalNoOfHoursWorked() {
		return totalNoOfHoursWorked;
	}
	public void setTotalNoOfHoursWorked(Double totalNoOfHoursWorked) {
		this.totalNoOfHoursWorked = totalNoOfHoursWorked;
	}
	public Double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getPeriod() {
		if(period == null){
			period = startDate + " - " + endDate;
		}
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	

	
}