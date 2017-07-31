package com.tm.invoice.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "invoice_setup_notes")
@JsonInclude(value = Include.NON_NULL)
public class InvoiceSetupNote extends AbstractAuditingEntity {

    private static final long serialVersionUID = -3669260161363779471L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inv_setup_note_id")
    private Long invoiceSetupNoteId;

    @Column(name = "empl_id")
    private Long employeeId;

    @Column(name = "invoice_notes_date")
    private Date invoiceNotesDate;

    @Column
    private String notes;
    
    @Column(name="notes_created_by")
    private String notesCreatedBy;

    @ManyToOne
    @JoinColumn(name = "inv_setup_id", nullable = false)
    private InvoiceSetup invoiceSetup;

    public Long getInvoiceSetupNoteId() {
        return invoiceSetupNoteId;
    }

    public void setInvoiceSetupNoteId(Long invoiceSetupNoteId) {
        this.invoiceSetupNoteId = invoiceSetupNoteId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Date getInvoiceNotesDate() {
        return invoiceNotesDate;
    }

    public void setInvoiceNotesDate(Date invoiceNotesDate) {
        this.invoiceNotesDate = invoiceNotesDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public InvoiceSetup getInvoiceSetup() {
        return invoiceSetup;
    }

    public void setInvoiceSetup(InvoiceSetup invoiceSetup) {
        this.invoiceSetup = invoiceSetup;
    }

    public String getNotesCreatedBy() {
        return notesCreatedBy;
    }

    public void setNotesCreatedBy(String notesCreatedBy) {
        this.notesCreatedBy = notesCreatedBy;
    }
}
