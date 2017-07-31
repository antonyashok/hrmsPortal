package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "invoice_setup_option")
public class InvoiceSetupOption implements Serializable {

    private static final long serialVersionUID = -443611172195422777L;

    public enum InvoiceFlag {
        Y, N;
    }

    @Id
    @Column(name = "inv_setup_optn_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long invoiceSetupOptionid;

    @Column(name = "option_lookup_id")
    @Type(type = "uuid-char")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "option_value")
    private InvoiceFlag value;

    @ManyToOne
    @JoinColumn(name = "inv_setup_id")
    private InvoiceSetup invoiceSetup;

    public Long getInvoiceSetupOptionid() {
        return invoiceSetupOptionid;
    }

    public void setInvoiceSetupOptionid(Long invoiceSetupOptionid) {
        this.invoiceSetupOptionid = invoiceSetupOptionid;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public InvoiceFlag getValue() {
        return value;
    }

    public void setValue(InvoiceFlag value) {
        this.value = value;
    }

    public InvoiceSetup getInvoiceSetup() {
        return invoiceSetup;
    }

    public void setInvoiceSetup(InvoiceSetup invoiceSetup) {
        this.invoiceSetup = invoiceSetup;
    }

}
