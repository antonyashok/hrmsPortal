package com.tm.invoice.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.commonapi.core.audit.mysql.BaseAuditableEntity;

@Entity
@Table(name = "invoice_setup_notes")
@JsonIgnoreProperties({"auditDetails"})
@JsonInclude(value = Include.NON_NULL)
public class InvoiceSetupNotes extends BaseAuditableEntity {

    @Id
    @Column(name = "inv_setup_note_id")
    @Type(type = "uuid-char")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID invoiceNotesId;

    @Transient
    private String employeeName;

    @Column(name = "inv_setup_id")
    private Long invoiceSetupId;

    @Column(name = "empl_id")
    private Long employeeId;

    @Column(name = "notes")
    private String invoiceSetupNotes;

    @Column(name = "invoice_notes_date")
    private String invoiceNotesDate;

    public UUID getInvoiceNotesId() {
        return invoiceNotesId;
    }

    public void setInvoiceNotesId(UUID invoiceNotesId) {
        this.invoiceNotesId = invoiceNotesId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Long getInvoiceSetupId() {
        return invoiceSetupId;
    }

    public void setInvoiceSetupId(Long invoiceSetupId) {
        this.invoiceSetupId = invoiceSetupId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getInvoiceSetupNotes() {
        return invoiceSetupNotes;
    }

    public void setInvoiceSetupNotes(String invoiceSetupNotes) {
        this.invoiceSetupNotes = invoiceSetupNotes;
    }

    public String getInvoiceNotesDate() {
        return invoiceNotesDate;
    }

    public void setInvoiceNotesDate(String invoiceNotesDate) {
        this.invoiceNotesDate = invoiceNotesDate;
    }


}
