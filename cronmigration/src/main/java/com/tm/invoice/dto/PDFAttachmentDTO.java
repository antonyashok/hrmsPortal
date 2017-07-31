package com.tm.invoice.dto;

import java.io.Serializable;

public class PDFAttachmentDTO implements Serializable {

    private static final long serialVersionUID = -9142256286970047165L;
    
    private String sourceReferenceId;
    private String sourceReferenceName;
    private String fileName;
    private String uploadedDate;
    private byte[] content;
    private String contentType;
    private String invoiceAttachmentId;
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

    public String getInvoiceAttachmentId() {
        return invoiceAttachmentId;
    }

    public void setInvoiceAttachmentId(String invoiceAttachmentId) {
        this.invoiceAttachmentId = invoiceAttachmentId;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

}
