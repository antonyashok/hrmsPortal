package com.tm.invoice.mongo.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "dpQueue")
public class DpQueue implements Serializable {

	private static final long serialVersionUID = -7101861973016638613L;

	@Id
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID id;
	private Long billToClientId;
	private String billToClientName;
	private Long contractorId;
	private String contractorName;
	private Long locationId;
	private String locationName;
	private String startDate;
	private String requestDate;
	private String description;
	private Long accountManagerId;
	private String accountManagerName;
	private Long recruiterId;
	private String recruiterName;
	private UUID poId;
	private String poName;
	private Double annualSalary;
	private Double feePercent;
	private Double fee;
	private Double processingFee;
	private Double totalFee;
	private Long invoiceTemplateId;
	private String invoiceTemplateName;
	private String lineOfBusinessName;
	private Long lineOfBusinessId;
	private String notes;
	private String status;
	private AuditFields created;
	private AuditFields updated;
	private BillingDetails billingDetails;
	private String email;
	private String currencyType;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public String getContractorName() {
		return contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getAccountManagerId() {
		return accountManagerId;
	}

	public void setAccountManagerId(Long accountManagerId) {
		this.accountManagerId = accountManagerId;
	}

	public String getAccountManagerName() {
		return accountManagerName;
	}

	public void setAccountManagerName(String accountManagerName) {
		this.accountManagerName = accountManagerName;
	}

	public Long getRecruiterId() {
		return recruiterId;
	}

	public void setRecruiterId(Long recruiterId) {
		this.recruiterId = recruiterId;
	}

	public String getRecruiterName() {
		return recruiterName;
	}

	public void setRecruiterName(String recruiterName) {
		this.recruiterName = recruiterName;
	}

	public UUID getPoId() {
		return poId;
	}

	public void setPoId(UUID poId) {
		this.poId = poId;
	}

	public String getPoName() {
		return poName;
	}

	public void setPoName(String poName) {
		this.poName = poName;
	}

	public Double getAnnualSalary() {
		return annualSalary;
	}

	public void setAnnualSalary(Double annualSalary) {
		this.annualSalary = annualSalary;
	}

	public Double getFeePercent() {
		return feePercent;
	}

	public void setFeePercent(Double feePercent) {
		this.feePercent = feePercent;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Double getProcessingFee() {
		return processingFee;
	}

	public void setProcessingFee(Double processingFee) {
		this.processingFee = processingFee;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public Long getInvoiceTemplateId() {
		return invoiceTemplateId;
	}

	public void setInvoiceTemplateId(Long invoiceTemplateId) {
		this.invoiceTemplateId = invoiceTemplateId;
	}

	public String getInvoiceTemplateName() {
		return invoiceTemplateName;
	}

	public void setInvoiceTemplateName(String invoiceTemplateName) {
		this.invoiceTemplateName = invoiceTemplateName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public BillingDetails getBillingDetails() {
		return billingDetails;
	}

	public void setBillingDetails(BillingDetails billingDetails) {
		this.billingDetails = billingDetails;
	}

	public String getLineOfBusinessName() {
		return lineOfBusinessName;
	}

	public void setLineOfBusinessName(String lineOfBusinessName) {
		this.lineOfBusinessName = lineOfBusinessName;
	}

	public Long getLineOfBusinessId() {
		return lineOfBusinessId;
	}

	public void setLineOfBusinessId(Long lineOfBusinessId) {
		this.lineOfBusinessId = lineOfBusinessId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

}
