package com.tm.expense.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "expense_generate_view")
public class ExpenseView {

	@Id
	@Type(type = "uuid-char")
	@Column(name = "expensesUUID")
	private UUID expensesUUID;

	@Column(name = "approverId")
	private String approverId;

	@Column(name = "expenseTypeName")
	private String expenseTypeName;

	@Column(name = "expenseReportUUID")
	@Type(type = "uuid-char")
	private UUID expenseReportUUID;

	@Column(name = "vendorName")
	private String vendorName;

	@Column(name = "expenseDate")
	private String expenseDate;

	@Column(name = "expenseAmount")
	private BigDecimal expenseAmount;

	@Column(name = "billable")
	private String billable;

	@Column(name = "currencyId")
	private Long currencyId;

	@Column(name = "convertedAmount")
	private BigDecimal convertedAmount;

	@Column(name = "currencySymbol")
	private String currencySymbol;

	@Column(name = "issues")
	private String issues;

	@Column(name = "fileName")
	private String fileName;

	@Column(name = "createdBy")
	private Long createdBy;

	@Column(name = "customerName")
	private String customerName;

	@Column(name = "projectName")
	private String projectName;

	@Column(name = "date_from")
	private Date dateFrom;

	@Column(name = "date_to")
	private Date dateTo;

	@Column(name = "reportName")
	private String reportName;

	@Column(name = "customer_project_id")
	@Type(type = "uuid-char")
	private UUID customerProjectId;
	
	@Column(name = "submitter_name")
	private String submitterName;
	
	@Column(name = "status")
	private String status;
	
	@Transient
	private String vendorAmount;

	@Transient
	private String expenseComment;

	@Transient
	private List<ExpenseImage> expenseImageList;

	@Transient
	private String SUBREPORT_DIR;

	public UUID getExpensesUUID() {
		return expensesUUID;
	}

	public void setExpensesUUID(UUID expensesUUID) {
		this.expensesUUID = expensesUUID;
	}

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public String getExpenseTypeName() {
		return expenseTypeName;
	}

	public void setExpenseTypeName(String expenseTypeName) {
		this.expenseTypeName = expenseTypeName;
	}

	public UUID getExpenseReportUUID() {
		return expenseReportUUID;
	}

	public void setExpenseReportUUID(UUID expenseReportUUID) {
		this.expenseReportUUID = expenseReportUUID;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(String expenseDate) {
		this.expenseDate = expenseDate;
	}

	public BigDecimal getExpenseAmount() {
		return expenseAmount;
	}

	public void setExpenseAmount(BigDecimal expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public String getBillable() {
		return billable;
	}

	public void setBillable(String billable) {
		this.billable = billable;
	}

	public Long getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getConvertedAmount() {
		return convertedAmount;
	}

	public void setConvertedAmount(BigDecimal convertedAmount) {
		this.convertedAmount = convertedAmount;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getIssues() {
		return issues;
	}

	public void setIssues(String issues) {
		this.issues = issues;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
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

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getVendorAmount() {
		return vendorAmount;
	}

	public void setVendorAmount(String vendorAmount) {
		this.vendorAmount = vendorAmount;
	}

	public String getExpenseComment() {
		return expenseComment;
	}

	public void setExpenseComment(String expenseComment) {
		this.expenseComment = expenseComment;
	}

	public List<ExpenseImage> getExpenseImageList() {
		return expenseImageList;
	}

	public void setExpenseImageList(List<ExpenseImage> expenseImageList) {
		this.expenseImageList = expenseImageList;
	}

	public String getSUBREPORT_DIR() {
		return SUBREPORT_DIR;
	}

	public void setSUBREPORT_DIR(String sUBREPORT_DIR) {
		SUBREPORT_DIR = sUBREPORT_DIR;
	}

	public UUID getCustomerProjectId() {
		return customerProjectId;
	}

	public void setCustomerProjectId(UUID customerProjectId) {
		this.customerProjectId = customerProjectId;
	}

	public String getSubmitterName() {
		return submitterName;
	}

	public void setSubmitterName(String submitterName) {
		this.submitterName = submitterName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}