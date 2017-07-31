package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;

@Relation(value = "InvoiceSetupNotes", collectionRelation = "InvoiceSetupNotes")
public class InvoiceSetupNoteDTO extends ResourceSupport implements Serializable {


    private static final long serialVersionUID = 5269945633901576678L;
    
    private Long invoiceSetupNoteId;

    private Long employeeId;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private String invoiceNotesDate;

    private String notes;
  
    @Type(type = "uuid-char")
    private UUID invoiceSetupId;

    private String notesCreatedBy;

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

    public String getInvoiceNotesDate() {
        return invoiceNotesDate;
    }

    public void setInvoiceNotesDate(String invoiceNotesDate) {
        this.invoiceNotesDate = invoiceNotesDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public UUID getInvoiceSetupId() {
        return invoiceSetupId;
    }

    public void setInvoiceSetupId(UUID invoiceSetupId) {
        this.invoiceSetupId = invoiceSetupId;
    }

    public String getNotesCreatedBy() {
        return notesCreatedBy;
    }

    public void setNotesCreatedBy(String notesCreatedBy) {
        this.notesCreatedBy = notesCreatedBy;
    }
}
