package com.tm.invoice.mongo.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceExceptionDetails implements Serializable{

	private static final long serialVersionUID = -2030155134919066333L;
	
	private String fileNumber;
	private String contractorName;
	private Date weekEndDate;
	private String st;
	private String ot;
	private String dt;
	private String amount;
	private String status;
	private String returnComments;
	private Double totalHours;
	private String returnApprovalComments;
	private String currencyType;
	
	private Double originalHours;
	private Double actualHours;
	private String originalExpenseCurrencyType;
	private Double originalExpenseAmount;
	private String actualExpenseCurrencyType;
	private Double actualExpenseAmount;
	
	
	public String getFileNumber() {
		return fileNumber;
	}
	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}
	public String getContractorName() {
		return contractorName;
	}
	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}
	public Date getWeekEndDate() {
		return weekEndDate;
	}
	public void setWeekEndDate(Date weekEndDate) {
		this.weekEndDate = weekEndDate;
	}
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	public String getOt() {
		return ot;
	}
	public void setOt(String ot) {
		this.ot = ot;
	}
	public String getDt() {
		return dt;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReturnComments() {
		return returnComments;
	}
	public void setReturnComments(String returnComments) {
		this.returnComments = returnComments;
	}
	public String getReturnApprovalComments() {
		return returnApprovalComments;
	}
	public void setReturnApprovalComments(String returnApprovalComments) {
		this.returnApprovalComments = returnApprovalComments;
	}
	public Double getTotalHours() {
		return totalHours;
	}
	public void setTotalHours(Double totalHours) {
		this.totalHours = totalHours;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public Double getOriginalHours() {
		return originalHours;
	}
	public void setOriginalHours(Double originalHours) {
		this.originalHours = originalHours;
	}
	public Double getActualHours() {
		return actualHours;
	}
	public void setActualHours(Double actualHours) {
		this.actualHours = actualHours;
	}
	public String getOriginalExpenseCurrencyType() {
		return originalExpenseCurrencyType;
	}
	public void setOriginalExpenseCurrencyType(String originalExpenseCurrencyType) {
		this.originalExpenseCurrencyType = originalExpenseCurrencyType;
	}
	public Double getOriginalExpenseAmount() {
		return originalExpenseAmount;
	}
	public void setOriginalExpenseAmount(Double originalExpenseAmount) {
		this.originalExpenseAmount = originalExpenseAmount;
	}
	public String getActualExpenseCurrencyType() {
		return actualExpenseCurrencyType;
	}
	public void setActualExpenseCurrencyType(String actualExpenseCurrencyType) {
		this.actualExpenseCurrencyType = actualExpenseCurrencyType;
	}
	public Double getActualExpenseAmount() {
		return actualExpenseAmount;
	}
	public void setActualExpenseAmount(Double actualExpenseAmount) {
		this.actualExpenseAmount = actualExpenseAmount;
	}
	
	
	
}
