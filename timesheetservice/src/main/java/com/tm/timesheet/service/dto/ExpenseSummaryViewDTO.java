package com.tm.timesheet.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;

public class ExpenseSummaryViewDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 2080713021272491798L;

	@Type(type = "uuid-char")
	private UUID expenseReportUUID;
	private String expenseReportName;
	private String dateTo;
	private String dateFrom;
	private String expenseDate;
	private String employeeId;
	private String employeeName;
	private String designation;
	private String billableExpenses;
	private String nonBillableExpenses;
	private String status;
	private String expenseAmount;
	private String customerProjectId;

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

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(String expenseDate) {
		this.expenseDate = expenseDate;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExpenseAmount() {
		return expenseAmount;
	}

	public void setExpenseAmount(String expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public String getCustomerProjectId() {
		return customerProjectId;
	}

	public void setCustomerProjectId(String customerProjectId) {
		this.customerProjectId = customerProjectId;
	}

}
