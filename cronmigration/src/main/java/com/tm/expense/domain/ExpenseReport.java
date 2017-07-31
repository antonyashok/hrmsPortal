package com.tm.expense.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "expense_report")
public class ExpenseReport extends AbstractAuditingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 240256622073762897L;

	@Id
	@Column(name = "expense_report_id")
	@Type(type = "uuid-char")
	private UUID expenseReportUUID;

	@Column(name = "customer_office_location_id")
	private Long customerOfficeLocationId;

	@Column(name = "customer_project_id")
	@Type(type = "uuid-char")
	private UUID customerProjectId;

	@Column(name = "report_name")
	private String title;

	@Column(name = "proc_inst_id")
	private String processInstanceId;

	@Column(name = "date_from")
	private String dateFrom;

	@Column(name = "date_to")
	private String dateTo;

	@Column(name = "date_of_submission")
	private Timestamp dateOfSubmission = new Timestamp(System.currentTimeMillis());

	@Column(name = "status")
	private String status;

	@Column(name = "description")
	private String description;

	@Column(name = "totalAmount")
	private BigDecimal totalAmount;

	@Column(name = "deletedStatus")
	private Integer deletedStatus;

	@Column(name = "workflow_on")
	private Boolean workflowOn = false;

	@Column(name = "notes")
	private String notes;

	@Column(name = "billable")
	private Boolean billable = false;

	@Column(name = "issues")
	private Boolean issues = false;

	public UUID getExpenseReportUUID() {
		return expenseReportUUID;
	}

	public void setExpenseReportUUID(UUID expenseReportUUID) {
		this.expenseReportUUID = expenseReportUUID;
	}

	public Long getCustomerOfficeLocationId() {
		return customerOfficeLocationId;
	}

	public void setCustomerOfficeLocationId(Long customerOfficeLocationId) {
		this.customerOfficeLocationId = customerOfficeLocationId;
	}

	public UUID getCustomerProjectId() {
		return customerProjectId;
	}

	public void setCustomerProjectId(UUID customerProjectId) {
		this.customerProjectId = customerProjectId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
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

	public Timestamp getDateOfSubmission() {
		return dateOfSubmission;
	}

	public void setDateOfSubmission(Timestamp dateOfSubmission) {
		this.dateOfSubmission = dateOfSubmission;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Integer getDeletedStatus() {
		return deletedStatus;
	}

	public void setDeletedStatus(Integer deletedStatus) {
		this.deletedStatus = deletedStatus;
	}

	public Boolean getWorkflowOn() {
		return workflowOn;
	}

	public void setWorkflowOn(Boolean workflowOn) {
		this.workflowOn = workflowOn;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public Boolean getIssues() {
		return issues;
	}

	public void setIssues(Boolean issues) {
		this.issues = issues;
	}

}
