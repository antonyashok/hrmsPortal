package com.tm.timesheet.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;

public class ExpenseDetailedViewDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 2080713021272491798L;

	@Type(type = "uuid-char")
	private UUID expenseUUID;
	@Type(type = "uuid-char")
	private UUID expenseReportUUID;
	private String expenseReportName;
	private String vendorName;
	private String expenseType;
	private String expenseAmount;
	private String expenseDate;
	private String billableExpenses;
	private String nonBillableExpenses;

	public UUID getExpenseUUID() {
		return expenseUUID;
	}

	public void setExpenseUUID(UUID expenseUUID) {
		this.expenseUUID = expenseUUID;
	}

	public UUID getExpenseReportUUID() {
		return expenseReportUUID;
	}

	public void setExpenseReportUUID(UUID expenseReportUUID) {
		this.expenseReportUUID = expenseReportUUID;
	}

	public String getExpenseReportName() {
		return expenseReportName;
	}

	public void setExpenseReportName(String expenseReportName) {
		this.expenseReportName = expenseReportName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getExpenseType() {
		return expenseType;
	}

	public void setExpenseType(String expenseType) {
		this.expenseType = expenseType;
	}

	public String getExpenseAmount() {
		return expenseAmount;
	}

	public void setExpenseAmount(String expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public String getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(String expenseDate) {
		this.expenseDate = expenseDate;
	}

	public String getBillableExpenses() {
		return billableExpenses;
	}

	public void setBillableExpenses(String billableExpenses) {
		this.billableExpenses = billableExpenses;
	}

	public String getNonBillableExpenses() {
		return nonBillableExpenses;
	}

	public void setNonBillableExpenses(String nonBillableExpenses) {
		this.nonBillableExpenses = nonBillableExpenses;
	}

}
