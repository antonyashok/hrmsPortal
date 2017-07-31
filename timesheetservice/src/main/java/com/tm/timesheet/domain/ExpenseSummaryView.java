package com.tm.timesheet.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "expense_summary_view")
public class ExpenseSummaryView implements Serializable {

	private static final long serialVersionUID = -3800220860053334950L;

	@Id
	@Type(type = "uuid-char")
	@Column(name = "expense_report_id")
	private UUID expenseReportUUID;

	@Column(name = "report_name")
	private String expenseReportName;

	@Column(name = "date_from")
	private String dateFrom;

	@Column(name = "date_to")
	private String dateTo;

	@Column(name = "date_to_string")
	private String expenseDate;

	@Column(name = "empl_id")
	private String employeeId;

	@Column(name = "employee_name")
	private String employeeName;

	@Column(name = "emp_rl_nm")
	private String designation;

	@Column(name = "billable_expenses")
	private String billableExpenses;

	@Column(name = "non_billable_expenses")
	private String nonBillableExpenses;

	@Column(name = "customer_project_id")
	private String customerProjectId;

	@Column(name = "expense_amount")
	private String expenseAmount;

	@Column(name = "status")
	private String status;

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

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
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

	public String getCustomerProjectId() {
		return customerProjectId;
	}

	public void setCustomerProjectId(String customerProjectId) {
		this.customerProjectId = customerProjectId;
	}

	public String getExpenseAmount() {
		return expenseAmount;
	}

	public void setExpenseAmount(String expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
