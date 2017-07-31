package com.tm.invoice.dto;

import java.io.Serializable;

import javax.persistence.ManyToOne;

import com.tm.invoice.domain.InvoiceSetupOption.InvoiceFlag;

public class InvoiceSetupOptionDTO implements Serializable {

    private static final long serialVersionUID = -918579067796038959L;

    private Long invoiceSetupOptionid;

    private String id;

    private InvoiceFlag value;

    @ManyToOne
    private InvoiceSetupDTO invoiceSetupDTO;

    public Long getInvoiceSetupOptionid() {
        return invoiceSetupOptionid;
    }

    public void setInvoiceSetupOptionid(Long invoiceSetupOptionid) {
        this.invoiceSetupOptionid = invoiceSetupOptionid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InvoiceFlag getValue() {
        return value;
    }

    public void setValue(InvoiceFlag value) {
        this.value = value;
    }

    public InvoiceSetupDTO getInvoiceSetupDTO() {
        return invoiceSetupDTO;
    }

    public void setInvoiceSetupDTO(InvoiceSetupDTO invoiceSetupDTO) {
        this.invoiceSetupDTO = invoiceSetupDTO;
    }

}
