package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "all_inv_setup_view")
public class InvoiceSetupView implements Serializable {

    private static final long serialVersionUID = -5597527342284426773L;

    @Id
    @Type(type = "uuid-char")
    @Column(name = "inv_setup_id")
    private UUID invoiceSetupId;

    @Column(name = "inv_setup_nm")
    private String invoiceSetupName;

    @Column(name = "bill_to_org_id")
    private Long customerId;

    @Column(name = "inv_typ")
    private String invoiceType;

    public UUID getInvoiceSetupId() {
        return invoiceSetupId;
    }

    public void setInvoiceSetupId(UUID invoiceSetupId) {
        this.invoiceSetupId = invoiceSetupId;
    }

    public String getInvoiceSetupName() {
        return invoiceSetupName;
    }

    public void setInvoiceSetupName(String invoiceSetupName) {
        this.invoiceSetupName = invoiceSetupName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }


}
