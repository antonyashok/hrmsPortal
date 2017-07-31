package com.tm.engagement.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "InvoiceAttachments", collectionRelation = "InvoiceAttachments")
public class InvoiceAttachmentsDTO extends ResourceSupport implements Serializable {

  private static final long serialVersionUID = -655173240930691918L;

  private String sourceReferenceId;
  private String sourceReferenceName;
  private String fileName;
  private String uploadedDate;
  private byte[] content;
  private String contentType;
  private UUID invoiceAttachmentId;
  private String subType;

  public String getSourceReferenceId() {
    return sourceReferenceId;
  }

  public void setSourceReferenceId(String sourceReferenceId) {
    this.sourceReferenceId = sourceReferenceId;
  }

  public String getSourceReferenceName() {
    return sourceReferenceName;
  }

  public void setSourceReferenceName(String sourceReferenceName) {
    this.sourceReferenceName = sourceReferenceName;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getUploadedDate() {
    return uploadedDate;
  }

  public void setUploadedDate(String uploadedDate) {
    this.uploadedDate = uploadedDate;
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public UUID getInvoiceAttachmentId() {
    return invoiceAttachmentId;
  }

  public void setInvoiceAttachmentId(UUID invoiceAttachmentId) {
    this.invoiceAttachmentId = invoiceAttachmentId;
  }

  public String getSubType() {
    return subType;
  }

  public void setSubType(String subType) {
    this.subType = subType;
  } 

}
