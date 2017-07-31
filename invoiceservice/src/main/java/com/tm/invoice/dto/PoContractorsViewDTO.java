package com.tm.invoice.dto;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PoContractorsViewDTO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = -2616517673702156247L;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private String contractEndDate;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private String contractStartDate;

    private Long employeeId;

    private String employeeName;

    @Type(type = "uuid-char")
    private UUID invoiceSetupId;

    @Type(type = "uuid-char")
    private UUID purchaseOrderId;
    
    private String poNumber;
    
    @Type(type = "uuid-char")
    private UUID engagementId;

    public String getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(String contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public UUID getInvoiceSetupId() {
        return invoiceSetupId;
    }

    public void setInvoiceSetupId(UUID invoiceSetupId) {
        this.invoiceSetupId = invoiceSetupId;
    }

    public UUID getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(UUID purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public UUID getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(UUID engagementId) {
        this.engagementId = engagementId;
    }
    
        
}
