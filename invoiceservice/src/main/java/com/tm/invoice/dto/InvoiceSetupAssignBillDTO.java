package com.tm.invoice.dto;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.web.core.data.BaseDTO;

public class InvoiceSetupAssignBillDTO extends BaseDTO {

    @NotNull(message = InvoiceConstants.INVOICEMANAGERNAME_REQUIRED)
    @NotBlank(message = InvoiceConstants.INVOICEMANAGERNAME_BLANK_ERROR_MESSAGE)
    private String billToManager;

    @NotNull(message = InvoiceConstants.INVOICEMANAGERADDR_REQUIRED)
    @NotBlank(message = InvoiceConstants.INVOICEMANAGERADDR_BLANK_ERROR_MESSAGE)
    private String billToManagerAddress;

    private Long invoiceManagerId;

    @Type(type = "uuid-char")
    private UUID invoiceSetupId;

    private String assignedFlag;

    public String getBillToManager() {
        return billToManager;
    }

    public String getBillToManagerAddress() {
        return billToManagerAddress;
    }

    public Long getInvoiceManagerId() {
        return invoiceManagerId;
    }

    public UUID getInvoiceSetupId() {
        return invoiceSetupId;
    }

    public String getAssignedFlag() {
        return assignedFlag;
    }

    public void setBillToManager(String billToManager) {
        this.billToManager = billToManager;
    }

    public void setBillToManagerAddress(String billToManagerAddress) {
        this.billToManagerAddress = billToManagerAddress;
    }

    public void setInvoiceManagerId(Long invoiceManagerId) {
        this.invoiceManagerId = invoiceManagerId;
    }

    public void setInvoiceSetupId(UUID invoiceSetupId) {
        this.invoiceSetupId = invoiceSetupId;
    }

    public void setAssignedFlag(String assignedFlag) {
        this.assignedFlag = assignedFlag;
    }

}
