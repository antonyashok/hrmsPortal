package com.tm.invoice.mongo.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tm.commonapi.core.audit.mysql.BaseAuditableEntity;
import com.tm.commonapi.web.core.data.IEntity;

@Document(collection = "purchaseOrderAttachment")

public class PurchaseOrderAttachment extends BaseAuditableEntity implements IEntity<UUID> {

    private static final long serialVersionUID = -3915270339178865218L;

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "id")
    @Column
    private UUID poAttachmentId;

    @Column
    private String poNumber;

    @Column
    @Type(type = "uuid-char")
    private UUID purchaseOrderId;

    @Column
    private byte[] poAttachmentFile;

    @Column
    private String fileName;

    @Column
    private String contentType;

    @Column
    private String createDate;

    public UUID getPoAttachmentId() {
        return poAttachmentId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public UUID getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public byte[] getPoAttachmentFile() {
        return poAttachmentFile;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setPoAttachmentId(UUID poAttachmentId) {
        this.poAttachmentId = poAttachmentId;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public void setPurchaseOrderId(UUID purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public void setPoAttachmentFile(byte[] poAttachmentFile) {
        this.poAttachmentFile = poAttachmentFile;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public UUID getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setId(UUID id) {
        // TODO Auto-generated method stub
    }

}
