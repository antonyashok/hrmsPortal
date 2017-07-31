package com.tm.invoice.dto;

import java.util.Date;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.tm.commonapi.web.core.data.BaseDTO;

public class UploadDocumentDTO extends BaseDTO {

    private UUID poAttachmentId;
    private UUID purchaseOrderId;
    private String fileName;
    private byte[] poImage;
    private MultipartFile originalFile;
    private String contentType;
    private byte[] fileContent;
    private Date createDate;
    private String poNumber;

    public UUID getPoAttachmentId() {
        return poAttachmentId;
    }

    public UUID getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getPoImage() {
        return poImage;
    }

    public MultipartFile getOriginalFile() {
        return originalFile;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoAttachmentId(UUID poAttachmentId) {
        this.poAttachmentId = poAttachmentId;
    }

    public void setPurchaseOrderId(UUID purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setPoImage(byte[] poImage) {
        this.poImage = poImage;
    }

    public void setOriginalFile(MultipartFile originalFile) {
        this.originalFile = originalFile;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }
}
