package com.tm.invoice.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.invoice.domain.PurchaseOrder.Alert;
import com.tm.invoice.domain.PurchaseOrder.Potype;
import com.tm.invoice.enums.ActiveFlag;

@JsonInclude(value = Include.NON_NULL)
public class PurchaseOrderDTO extends ResourceSupport {
    
    private static final String PONUMBER_BLANK_ERROR_MESSAGE = "PO Number Field Cannot be Empty";
    private static final String PONUMBER_SIZE_ERROR_MESSAGE =
            "PO Number Exceeds more than 20 Characters";
    private static final String PONUMBER_SPECIAL_CHARECTER_ERROR_MESSAGE =
            "PO Number special character's not allowed";
    private static final String STARTDATE_BLANK_ERROR_MESSAGE =
            "Start String Field Cannot be Empty";
    private static final String ENDDATE_BLANK_ERROR_MESSAGE = "End String Field Cannot be Empty";
    private static final String AMOUNT_BLANK_ERROR_MESSAGE = "amount Field Cannot be Empty";

    @Type(type = "uuid-char")
    private UUID purchaseOrderId;
    @Enumerated(EnumType.STRING)
    private ActiveFlag active;
    @Enumerated(EnumType.STRING)
    private Alert alert;
    private String description;
    @NotBlank(message = ENDDATE_BLANK_ERROR_MESSAGE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String endDate;
    @Type(type = "uuid-char")
    private UUID engagementId;
    private String invoiceGeneratedDate;
    private String notes;
    @Type(type = "uuid-char")
    private UUID parentPurchaseOrderId;
    @NotNull(message = AMOUNT_BLANK_ERROR_MESSAGE)
    private BigDecimal purchaseOrderAmount;
    @NotBlank(message = PONUMBER_BLANK_ERROR_MESSAGE)
    @Size(min = 1, max = 20, message = PONUMBER_SIZE_ERROR_MESSAGE)
    @Pattern(regexp = "[ a-zA-Z0-9_.-]*", message = PONUMBER_SPECIAL_CHARECTER_ERROR_MESSAGE)
    private String poNumber;
    @Enumerated(EnumType.STRING)
    private Potype purchaseOrderType;
    @NotBlank(message = STARTDATE_BLANK_ERROR_MESSAGE)
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String startDate;
    private String unbilledPoReference;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String lastUpdatedOn;
    
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String engagementStartDate;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String engagementEndDate;
    @Type(type = "uuid-char")
    private UUID revenueParentPurchaseOrderId;
    @Type(type = "uuid-char")
    private UUID expenseParentPurchaseOrderId;
    
    private List<InvoiceAttachmentsDTO> purcheaseOrderAttachements;
    
    private String status;

    public UUID getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(UUID purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public ActiveFlag getActive() {
        return active;
    }

    public void setActive(ActiveFlag active) {
        this.active = active;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public UUID getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(UUID engagementId) {
        this.engagementId = engagementId;
    }

    public String getInvoiceGeneratedDate() {
        return invoiceGeneratedDate;
    }

    public void setInvoiceGeneratedDate(String invoiceGeneratedDate) {
        this.invoiceGeneratedDate = invoiceGeneratedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public UUID getParentPurchaseOrderId() {
        return parentPurchaseOrderId;
    }

    public void setParentPurchaseOrderId(UUID parentPurchaseOrderId) {
        this.parentPurchaseOrderId = parentPurchaseOrderId;
    }

    public BigDecimal getPurchaseOrderAmount() {
        return purchaseOrderAmount;
    }

    public void setPurchaseOrderAmount(BigDecimal purchaseOrderAmount) {
        this.purchaseOrderAmount = purchaseOrderAmount;
    }

    public Potype getPurchaseOrderType() {
        return purchaseOrderType;
    }

    public void setPurchaseOrderType(Potype purchaseOrderType) {
        this.purchaseOrderType = purchaseOrderType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getUnbilledPoReference() {
        return unbilledPoReference;
    }

    public void setUnbilledPoReference(String unbilledPoReference) {
        this.unbilledPoReference = unbilledPoReference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEngagementStartDate() {
        return engagementStartDate;
    }

    public void setEngagementStartDate(String engagementStartDate) {
        this.engagementStartDate = engagementStartDate;
    }

    public String getEngagementEndDate() {
        return engagementEndDate;
    }

    public void setEngagementEndDate(String engagementEndDate) {
        this.engagementEndDate = engagementEndDate;
    }

    public UUID getRevenueParentPurchaseOrderId() {
        return revenueParentPurchaseOrderId;
    }

    public void setRevenueParentPurchaseOrderId(UUID revenueParentPurchaseOrderId) {
        this.revenueParentPurchaseOrderId = revenueParentPurchaseOrderId;
    }

    public UUID getExpenseParentPurchaseOrderId() {
        return expenseParentPurchaseOrderId;
    }

    public void setExpenseParentPurchaseOrderId(UUID expenseParentPurchaseOrderId) {
        this.expenseParentPurchaseOrderId = expenseParentPurchaseOrderId;
    }

    public List<InvoiceAttachmentsDTO> getPurcheaseOrderAttachements() {
        return purcheaseOrderAttachements;
    }

    public void setPurcheaseOrderAttachements(List<InvoiceAttachmentsDTO> purcheaseOrderAttachements) {
        this.purcheaseOrderAttachements = purcheaseOrderAttachements;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    
}
