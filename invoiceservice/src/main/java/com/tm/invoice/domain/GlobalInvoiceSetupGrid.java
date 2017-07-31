package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "gis_grid_view")
public class GlobalInvoiceSetupGrid implements Serializable {

    private static final long serialVersionUID = -6744848038465709132L;

    @Id
    @Column(name = "global_inv_setup_id")
    @Type(type = "uuid-char")
    private UUID globalInvoiceSetupId;

    @Column(name = "inv_setup_name")
    private String invoiceSetupName;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "inv_start_dt")
    private Date invoiceStartDate;

    @Column(name = "inv_end_dt")
    private Date invoiceEndDate;

    @Column(name = "inv_status")
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getInvoiceStartDate() {
        return invoiceStartDate;
    }

    public void setInvoiceStartDate(Date invoiceStartDate) {
        this.invoiceStartDate = invoiceStartDate;
    }

    public Date getInvoiceEndDate() {
        return invoiceEndDate;
    }

    public void setInvoiceEndDate(Date invoiceEndDate) {
        this.invoiceEndDate = invoiceEndDate;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

}


