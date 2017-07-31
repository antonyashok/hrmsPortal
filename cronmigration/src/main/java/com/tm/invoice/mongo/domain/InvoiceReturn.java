package com.tm.invoice.mongo.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "invoicereturn")
public class InvoiceReturn {

    @Id
    private UUID id;
    private String invoiceNumber;
    private Long billToClientId;
    @Type(type = "uuid-char")
    private UUID invoiceSetupId;
    private String billToClientName;
    private Long endClientId;
    private String endClientName;
    private Long billingSpecialistId;
    private String billingSpecialistName;
    private Long reportingManagerId;
    /*private String invoiceType;
    private String timesheetType;
    private String billCycle;
    private String location;	
    private String country;
    private String delivery;*/
    private String currencyType;
    private double amount;
    private String status;
    private String returnComments;
    private String approvalComments;
    private String contractor;
    private AuditFields created;
	private AuditFields updated;
	private Date createdDate;

    
    
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public Long getBillToClientId() {
		return billToClientId;
	}
	public void setBillToClientId(Long billToClientId) {
		this.billToClientId = billToClientId;
	}
	public String getBillToClientName() {
		return billToClientName;
	}
	public void setBillToClientName(String billToClientName) {
		this.billToClientName = billToClientName;
	}
	public Long getEndClientId() {
		return endClientId;
	}
	public void setEndClientId(Long endClientId) {
		this.endClientId = endClientId;
	}
	public String getEndClientName() {
		return endClientName;
	}
	public void setEndClientName(String endClientName) {
		this.endClientName = endClientName;
	}
	public Long getBillingSpecialistId() {
		return billingSpecialistId;
	}
	public void setBillingSpecialistId(Long billingSpecialistId) {
		this.billingSpecialistId = billingSpecialistId;
	}
	public String getBillingSpecialistName() {
		return billingSpecialistName;
	}
	public void setBillingSpecialistName(String billingSpecialistName) {
		this.billingSpecialistName = billingSpecialistName;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public UUID getInvoiceSetupId() {
		return invoiceSetupId;
	}
	public void setInvoiceSetupId(UUID invoiceSetupId) {
		this.invoiceSetupId = invoiceSetupId;
	}
	public String getReturnComments() {
		return returnComments;
	}
	public void setReturnComments(String returnComments) {
		this.returnComments = returnComments;
	}
	public String getApprovalComments() {
		return approvalComments;
	}
	public void setApprovalComments(String approvalComments) {
		this.approvalComments = approvalComments;
	}
	public Long getReportingManagerId() {
		return reportingManagerId;
	}
	public void setReportingManagerId(Long reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}
	public String getContractor() {
		return contractor;
	}
	public void setContractor(String contractor) {
		this.contractor = contractor;
	}
	public AuditFields getCreated() {
		return created;
	}
	public void setCreated(AuditFields created) {
		this.created = created;
	}
	public AuditFields getUpdated() {
		return updated;
	}
	public void setUpdated(AuditFields updated) {
		this.updated = updated;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
