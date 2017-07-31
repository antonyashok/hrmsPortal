package com.tm.expense.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "expensereport_view")
public class ExpenseReportView {

	@Id
	@Type(type = "uuid-char")
	@Column(name = "expenseReportUUID")
	private UUID expenseReportUUID;

	@Column(name = "title")
	private String title;

	@Column(name = "dateFrom")
	private String dateFrom;

	@Column(name = "dateTo")
	private String dateTo;

	@Column(name = "submitterName")
	private String submitterName;

	@Column(name = "description")
	private String description;

	@Column(name = "totalAmount")
	private BigDecimal totalAmount;

	@Column(name = "status")
	private String status;

	@Column(name = "customerName")
	private String customerName;

	@Column(name = "projectName")
	private String projectName;

	@Column(name = "employeeType")
	private String employeeType;

	@Column(name = "createdOn")
	private String createdOn;

	@Column(name = "updatedOn")
	private String updatedOn;

	@Transient
	private String notes;

	@Transient
	private List<NotesView> notesList;

	@Transient
	private List<ExpenseView> expensesList;

	@Transient
	private List<ApprovalActivityVOView> activityList;

	public UUID getExpenseReportUUID() {
		return expenseReportUUID;
	}

	public void setExpenseReportUUID(UUID expenseReportUUID) {
		this.expenseReportUUID = expenseReportUUID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getSubmitterName() {
		return submitterName;
	}

	public void setSubmitterName(String submitterName) {
		this.submitterName = submitterName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<NotesView> getNotesList() {
		return notesList;
	}

	public void setNotesList(List<NotesView> notesList) {
		this.notesList = notesList;
	}

	public List<ExpenseView> getExpensesList() {
		return expensesList;
	}

	public void setExpensesList(List<ExpenseView> expensesList) {
		this.expensesList = expensesList;
	}

	public List<ApprovalActivityVOView> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<ApprovalActivityVOView> activityList) {
		this.activityList = activityList;
	}
	

}
