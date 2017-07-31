package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalInvoiceSetupGridDTO implements Serializable {

    private static final long serialVersionUID = 4867158790232498523L;

    private UUID globalInvoiceSetupId;
    private String invoiceSetupName;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String createdDate;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String invoiceStartDate;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String invoiceEndDate;
    private String invoiceStatus;

    public UUID getGlobalInvoiceSetupId() {
        return globalInvoiceSetupId;
    }

    public void setGlobalInvoiceSetupId(UUID globalInvoiceSetupId) {
        this.globalInvoiceSetupId = globalInvoiceSetupId;
    }

    public String getInvoiceSetupName() {
        return invoiceSetupName;
    }

    public void setInvoiceSetupName(String invoiceSetupName) {
        this.invoiceSetupName = invoiceSetupName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getInvoiceStartDate() {
        return invoiceStartDate;
    }

    public void setInvoiceStartDate(String invoiceStartDate) {
        this.invoiceStartDate = invoiceStartDate;
    }

    public String getInvoiceEndDate() {
        return invoiceEndDate;
    }

    public void setInvoiceEndDate(String invoiceEndDate) {
        this.invoiceEndDate = invoiceEndDate;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

}
