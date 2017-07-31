package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceQueueAttachmentDTO implements Serializable {

  private static final long serialVersionUID = 927571816358281528L;

  private List<InvoiceAttachmentsDTO> invoiceAttachments;
  private List<InvoiceAttachmentsDTO> expenseAttachments;
  private List<InvoiceAttachmentsDTO> timesheetAttachments;

  public List<InvoiceAttachmentsDTO> getInvoiceAttachments() {
    return invoiceAttachments;
  }

  public void setInvoiceAttachments(List<InvoiceAttachmentsDTO> invoiceAttachments) {
    this.invoiceAttachments = invoiceAttachments;
  }

  public List<InvoiceAttachmentsDTO> getExpenseAttachments() {
    return expenseAttachments;
  }

  public void setExpenseAttachments(List<InvoiceAttachmentsDTO> expenseAttachments) {
    this.expenseAttachments = expenseAttachments;
  }

  public List<InvoiceAttachmentsDTO> getTimesheetAttachments() {
    return timesheetAttachments;
  }

  public void setTimesheetAttachments(List<InvoiceAttachmentsDTO> timesheetAttachments) {
    this.timesheetAttachments = timesheetAttachments;
  }

}
