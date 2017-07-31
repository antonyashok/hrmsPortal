package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tm.invoice.mongo.domain.AuditFields;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceReturnDTO implements Serializable {

	private static final long serialVersionUID = 3113910786073345726L;
	
	private static final String COMMENTS_ALLOWED_300_CHARACTERS = "Comments allowed 300 characters only";
	
	private UUID id;
	private UUID invoiceQueueId;
    private String invoiceNumber;
    private Long billToClientId;
    @Type(type = "uuid-char")
    private UUID invoiceSetupId;
    private String billToClientName;
    private Long endClientId;
    private String endClientName;
    private Long billingSpecialistId;
    private double amount;
    private String contractor;
    private String currencyType;
    private String status;
    @Size(max = 300, message = COMMENTS_ALLOWED_300_CHARACTERS)
    private String returnComments;
    @Size(max = 300, message = COMMENTS_ALLOWED_300_CHARACTERS)
    private String approvalComments;
    private AuditFields created;
	private AuditFields updated;
	private List<InvoiceAttachmentsDTO> attachments;
	private boolean statusFlag=false;
    
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
	public UUID getInvoiceSetupId() {
		return invoiceSetupId;
	}
	public void setInvoiceSetupId(UUID invoiceSetupId) {
		this.invoiceSetupId = invoiceSetupId;
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getContractor() {
		return contractor;
	}
	public void setContractor(String contractor) {
		this.contractor = contractor;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
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
	public String getApprovalComments() {
		return approvalComments;
	}
	public void setApprovalComments(String approvalComments) {
		this.approvalComments = approvalComments;
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
	public List<InvoiceAttachmentsDTO> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<InvoiceAttachmentsDTO> attachments) {
		this.attachments = attachments;
	}
	public boolean isStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(boolean statusFlag) {
		this.statusFlag = statusFlag;
	}
	public UUID getInvoiceQueueId() {
		return invoiceQueueId;
	}
	public void setInvoiceQueueId(UUID invoiceQueueId) {
		this.invoiceQueueId = invoiceQueueId;
	}
	
	
    
    
}
