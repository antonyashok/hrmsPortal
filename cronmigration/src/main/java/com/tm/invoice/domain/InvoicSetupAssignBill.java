package com.tm.invoice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.commonapi.core.audit.mysql.BaseAuditableEntity;

@Entity
@Table(name = "invoice_setup_assign_bill")
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties({"auditDetails"})
public class InvoicSetupAssignBill extends BaseAuditableEntity {

    @Id
    @Column(name = "inv_manager_id")
    private Long invoiceManagerId;

    @Column(name = "inv_setup_id")
    private Long invoiceSetupId;

    @Column(name = "mgr_name")
    private String billToManager;

    @Column(name = "address")
    private String billToManagerAddress;

    @Column(name = "assigned_flg")
    private String assignedFlag;

    public Long getInvoiceManagerId() {
        return invoiceManagerId;
    }

    public void setInvoiceManagerId(Long invoiceManagerId) {
        this.invoiceManagerId = invoiceManagerId;
    }

    public Long getInvoiceSetupId() {
        return invoiceSetupId;
    }

    public void setInvoiceSetupId(Long invoiceSetupId) {
        this.invoiceSetupId = invoiceSetupId;
    }

    public String getBillToManager() {
        return billToManager;
    }

    public void setBillToManager(String billToManager) {
        this.billToManager = billToManager;
    }

    public String getBillToManagerAddress() {
        return billToManagerAddress;
    }

    public void setBillToManagerAddress(String billToManagerAddress) {
        this.billToManagerAddress = billToManagerAddress;
    }

    public String getAssignedFlag() {
        return assignedFlag;
    }

    public void setAssignedFlag(String assignedFlag) {
        this.assignedFlag = assignedFlag;
    }

}

