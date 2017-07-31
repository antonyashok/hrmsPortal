package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.UUID;

public class InvoiceBillWatchDTO implements Serializable{

	private static final long serialVersionUID = 3807009133525222080L;
	
	private UUID id;
	private String invoiceNumber;
	private String userName;
	private Long userId;
	private Double originalHours;
	private Double actualHours;
	private String originalExpenseCurrencyType;
	private Double originalExpenseAmount;
	private String actualExpenseCurrencyType;
	private Double actualExpenseAmount;
	private String action;
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	

}
