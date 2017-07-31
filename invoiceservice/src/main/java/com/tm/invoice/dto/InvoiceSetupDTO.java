package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tm.invoice.domain.InvoiceSetup.ActiveFlag;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "InvoiceSetups", collectionRelation = "InvoiceSetups")
public class InvoiceSetupDTO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = -4729316417571035107L;

    @Type(type = "uuid-char")
    private UUID invoiceSetupId;

    @Enumerated(EnumType.STRING)
    private ActiveFlag activeFlag;

    private Long invoiceManagerId;

    private Long billToOrganizationId;

    private String deliveryModeLookUpName;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private String endDate;

    private String invoiceDescription;

    private String invoiceSetupName;

    private Long invoiceSpecialistEmployeeId;

    private Long invoiceTemplateId;

    private String invoiceTypeName;

    private String notes;

    private String receipientEmail;

    private String terms;

    private String status;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private String startDate;

    private String billToOrganizationName;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private String createdDate;

    @Transient
    private String action;

    @Transient
    private String updatedNotes;

    private String comments;
    
    private UUID engagementId;

    @OneToMany
    private List<InvoiceSetupNoteDTO> invoiceSetupNotes;

    @Transient
    private String allNotes;

    @Transient
    private List<UUID> invoiceSetupIds;

    @Transient
    private List<InvoiceAttachmentsDTO> invoiceSetupAttachements;

    @OneToMany(mappedBy = "invoiceSetup")
    private List<InvoiceSetupOptionDTO> invoiceSetupOptions;

    private String prefix;

    private Integer startingNumber;

    private String suffixType;

    private String suffixValue;

    private String separator;

    private String billCycleDay;

    private String billCycleName;

    private String billCycleStartEndDetail;
    
    @Transient
    private boolean isSetupPresent;

    public UUID getInvoiceSetupId() {
        return invoiceSetupId;
    }

    public void setInvoiceSetupId(UUID invoiceSetupId) {
        this.invoiceSetupId = invoiceSetupId;
    }

    public ActiveFlag getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(ActiveFlag activeFlag) {
        this.activeFlag = activeFlag;
    }

    public Long getInvoiceManagerId() {
        return invoiceManagerId;
    }

    public void setInvoiceManagerId(Long invoiceManagerId) {
        this.invoiceManagerId = invoiceManagerId;
    }

    public Long getBillToOrganizationId() {
        return billToOrganizationId;
    }

    public void setBillToOrganizationId(Long billToOrganizationId) {
        this.billToOrganizationId = billToOrganizationId;
    }

    public String getDeliveryModeLookUpName() {
        return deliveryModeLookUpName;
    }

    public void setDeliveryModeLookUpName(String deliveryModeLookUpName) {
        this.deliveryModeLookUpName = deliveryModeLookUpName;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

    public void setInvoiceDescription(String invoiceDescription) {
        this.invoiceDescription = invoiceDescription;
    }

    public String getInvoiceSetupName() {
        return invoiceSetupName;
    }

    public void setInvoiceSetupName(String invoiceSetupName) {
        this.invoiceSetupName = invoiceSetupName;
    }

    public Long getInvoiceSpecialistEmployeeId() {
        return invoiceSpecialistEmployeeId;
    }

    public void setInvoiceSpecialistEmployeeId(Long invoiceSpecialistEmployeeId) {
        this.invoiceSpecialistEmployeeId = invoiceSpecialistEmployeeId;
    }

    public Long getInvoiceTemplateId() {
        return invoiceTemplateId;
    }

    public void setInvoiceTemplateId(Long invoiceTemplateId) {
        this.invoiceTemplateId = invoiceTemplateId;
    }

    public String getInvoiceTypeName() {
        return invoiceTypeName;
    }

    public void setInvoiceTypeName(String invoiceTypeName) {
        this.invoiceTypeName = invoiceTypeName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getReceipientEmail() {
        return receipientEmail;
    }

    public void setReceipientEmail(String receipientEmail) {
        this.receipientEmail = receipientEmail;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getBillToOrganizationName() {
        return billToOrganizationName;
    }

    public void setBillToOrganizationName(String billToOrganizationName) {
        this.billToOrganizationName = billToOrganizationName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUpdatedNotes() {
        return updatedNotes;
    }

    public void setUpdatedNotes(String updatedNotes) {
        this.updatedNotes = updatedNotes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<InvoiceSetupNoteDTO> getInvoiceSetupNotes() {
        return invoiceSetupNotes;
    }

    public void setInvoiceSetupNotes(List<InvoiceSetupNoteDTO> invoiceSetupNotes) {
        this.invoiceSetupNotes = invoiceSetupNotes;
    }

    public String getAllNotes() {
        return allNotes;
    }

    public void setAllNotes(String allNotes) {
        this.allNotes = allNotes;
    }

    public List<UUID> getInvoiceSetupIds() {
        return invoiceSetupIds;
    }

    public void setInvoiceSetupIds(List<UUID> invoiceSetupIds) {
        this.invoiceSetupIds = invoiceSetupIds;
    }

    public List<InvoiceAttachmentsDTO> getInvoiceSetupAttachements() {
        return invoiceSetupAttachements;
    }

    public void setInvoiceSetupAttachements(List<InvoiceAttachmentsDTO> invoiceSetupAttachements) {
        this.invoiceSetupAttachements = invoiceSetupAttachements;
    }

    public List<InvoiceSetupOptionDTO> getInvoiceSetupOptions() {
        return invoiceSetupOptions;
    }

    public void setInvoiceSetupOptions(List<InvoiceSetupOptionDTO> invoiceSetupOptions) {
        this.invoiceSetupOptions = invoiceSetupOptions;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getStartingNumber() {
        return startingNumber;
    }

    public void setStartingNumber(Integer startingNumber) {
        this.startingNumber = startingNumber;
    }

    public String getSuffixType() {
        return suffixType;
    }

    public void setSuffixType(String suffixType) {
        this.suffixType = suffixType;
    }

    public String getSuffixValue() {
        return suffixValue;
    }

    public void setSuffixValue(String suffixValue) {
        this.suffixValue = suffixValue;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getBillCycleDay() {
        return billCycleDay;
    }

    public void setBillCycleDay(String billCycleDay) {
        this.billCycleDay = billCycleDay;
    }

    public String getBillCycleName() {
        return billCycleName;
    }

    public void setBillCycleName(String billCycleName) {
        this.billCycleName = billCycleName;
    }

    public String getBillCycleStartEndDetail() {
        return billCycleStartEndDetail;
    }

    public void setBillCycleStartEndDetail(String billCycleStartEndDetail) {
        this.billCycleStartEndDetail = billCycleStartEndDetail;
    }

    public UUID getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(UUID engagementId) {
        this.engagementId = engagementId;
    }

    public boolean isSetupPresent() {
        return isSetupPresent;
    }

    public void setSetupPresent(boolean isSetupPresent) {
        this.isSetupPresent = isSetupPresent;
    }
    
}
