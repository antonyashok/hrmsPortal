package com.tm.invoice.mongo.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Id;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "manualInvoice")
public class ManualInvoice implements Serializable {

    private static final long serialVersionUID = 810726460722612082L;

    private static final String USD = "USD";

    @Id
    private ObjectId _id;
    private UUID invoiceId;
    private Long billToClientId;
    private Long officeId;
    private String lineOfBusinessId;
    private String attention;
    private String billToAddress;
    private double totalAmount;
    private UUID projectId;
    private Long templateId;
    private String poNumber;
    private String deliveryId;
    private String recipientsEmailIds;
    private String status;
    private String invoiceType;
    private String billToClient;
    private String currencyType=USD;
    private Date createdDate;
    private String officeLocation;
    private String countryName;
    private Long countryId;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    private String reviewComments;
    private String createdBy;
    
    private List<ManualInvoiceContractorDetail> manualInvoiceContractorDetails;

    private AuditFields auditFields;
    
    public Long getBillToClientId() {
        return billToClientId;
    }

    public void setBillToClientId(Long billToClientId) {
        this.billToClientId = billToClientId;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public String getLineOfBusinessId() {
        return lineOfBusinessId;
    }

    public void setLineOfBusinessId(String lineOfBusinessId) {
        this.lineOfBusinessId = lineOfBusinessId;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getBillToAddress() {
        return billToAddress;
    }

    public void setBillToAddress(String billToAddress) {
        this.billToAddress = billToAddress;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getRecipientsEmailIds() {
        return recipientsEmailIds;
    }

    public void setRecipientsEmailIds(String recipientsEmailIds) {
        this.recipientsEmailIds = recipientsEmailIds;
    }

    public List<ManualInvoiceContractorDetail> getManualInvoiceContractorDetails() {
        return manualInvoiceContractorDetails;
    }

    public void setManualInvoiceContractorDetails(
            List<ManualInvoiceContractorDetail> manualInvoiceContractorDetails) {
        this.manualInvoiceContractorDetails = manualInvoiceContractorDetails;
    }

    public AuditFields getAuditFields() {
        return auditFields;
    }

    public void setAuditFields(AuditFields auditFields) {
        this.auditFields = auditFields;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(String reviewComments) {
        this.reviewComments = reviewComments;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getBillToClient() {
        return billToClient;
    }

    public void setBillToClient(String billToClient) {
        this.billToClient = billToClient;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
    
}
