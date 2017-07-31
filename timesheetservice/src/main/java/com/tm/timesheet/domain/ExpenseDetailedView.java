package com.tm.timesheet.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "expense_detailed_view")
public class ExpenseDetailedView implements Serializable {

	private static final long serialVersionUID = -2292884249097939624L;

	@Id
	@Type(type = "uuid-char")
	@Column(name = "expense_id")
	private UUID expenseUUID;

	@Type(type = "uuid-char")
	@Column(name = "expense_report_id")
	private UUID expenseReportUUID;

	@Column(name = "report_name")
	private String expenseReportName;

	@Column(name = "vendor_name")
	private String vendorName;

	@Column(name = "ig_expense_type_name")
	private String expenseType;

	@Column(name = "amount")
	private String expenseAmount;

	@Column(name = "receipt_date")
	private String expenseDate;

	@Column(name = "billable_expenses")
	private String billableExpenses;

	@Column(name = "non_billable_expenses")
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
