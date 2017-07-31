package com.tm.invoice.mongo.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tm.invoice.dto.InvoiceAttachmentsDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "ManualInvoice", collectionRelation = "ManualInvoice")
public class ManualInvoiceDTO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 7647958023921868663L;

    private static final String BILL_TO_CLIENT_REQUIRED = "Bill To client is Required";

    private static final String LINE_OF_BUSINESS_REQUIRED = "Line of Business is Required ";

    private static final String BILL_TO_ADDRESS_REQUIRED = "Bill To address is Required";

    private static final String TOTAL_REQUIRED = "Total is Required";

    private static final String PROJECT_REQUIRED = "Project is Required";

    private static final String TEMPLATE_REQUIRED = "Template is Required";

    private static final String PO_NUMBER_REQUIRED = "Po number is Required ";

    private static final String DELIVERY_REQUIRED = "Delivery value is Required";

    private static final String RECIPIENT_EMAIL_REQUIRED = "Recipient Email id is Required";

    private static final String USD = "USD";

    private UUID invoiceId;
    @NotNull(message = BILL_TO_CLIENT_REQUIRED)
    private Long billToClientId;
    private Long officeId;
    @NotNull(message = LINE_OF_BUSINESS_REQUIRED)
    private String lineOfBusinessId;
    @NotBlank(message = BILL_TO_ADDRESS_REQUIRED)
    private String billToAddress;
    @NotNull(message = TOTAL_REQUIRED)
    private BigDecimal totalAmount;
    @NotNull(message = PROJECT_REQUIRED)
    private UUID projectId;
    @NotNull(message = TEMPLATE_REQUIRED)
    private Long templateId;
    @NotBlank(message = PO_NUMBER_REQUIRED)
    private String poNumber;
    @NotNull(message = DELIVERY_REQUIRED)
    private String deliveryId;
    @NotBlank(message = RECIPIENT_EMAIL_REQUIRED)
    private String recipientsEmailIds; 
    private String reviewComments;
    private String status;
    private String billToClient;
    
    private String invoiceType;
    private Long financeRepId;
    private String officeLocation;
    private String countryName;
    private Long countryId;

    @Transient
    private String action;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private String createdDate;
    
    private String createdBy;

    private List<ManualInvoiceContractorDetailDTO> manualInvoiceContractorDetails;
    
    @Transient
    private List<InvoiceAttachmentsDTO> manualInvoiceAttachements;
    
    @Transient
    private List<UUID> manualInvoiceIds;
    
    private String currencyType=USD;
    
    private String billToClientName;
    
    private String billingSpecialistName;
    
    private String delivery;    
    
    private String invoiceNumber;

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

    public String getBillToAddress() {
        return billToAddress;
    }

    public void setBillToAddress(String billToAddress) {
        this.billToAddress = billToAddress;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
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

    public List<ManualInvoiceContractorDetailDTO> getManualInvoiceContractorDetails() {
        return manualInvoiceContractorDetails;
    }

    public void setManualInvoiceContractorDetails(
            List<ManualInvoiceContractorDetailDTO> manualInvoiceContractorDetails) {
        this.manualInvoiceContractorDetails = manualInvoiceContractorDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
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

    public List<InvoiceAttachmentsDTO> getManualInvoiceAttachements() {
        return manualInvoiceAttachements;
    }

    public void setManualInvoiceAttachements(List<InvoiceAttachmentsDTO> manualInvoiceAttachements) {
        this.manualInvoiceAttachements = manualInvoiceAttachements;
    }

    public List<UUID> getManualInvoiceIds() {
        return manualInvoiceIds;
    }

    public void setManualInvoiceIds(List<UUID> manualInvoiceIds) {
        this.manualInvoiceIds = manualInvoiceIds;
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

    public Long getFinanceRepId() {
        return financeRepId;
    }

    public void setFinanceRepId(Long financeRepId) {
        this.financeRepId = financeRepId;
    }

    public String getBillToClientName() {
        return billToClientName;
    }

    public void setBillToClientName(String billToClientName) {
        this.billToClientName = billToClientName;
    }

    public String getBillingSpecialistName() {
        return billingSpecialistName;
    }

    public void setBillingSpecialistName(String billingSpecialistName) {
        this.billingSpecialistName = billingSpecialistName;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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
