package com.tm.invoice.dto;

import java.util.UUID;

import com.tm.commonapi.core.audit.mysql.BaseAuditableEntity;

public class PurchaseOrderAttachmentDTO extends BaseAuditableEntity {

    private UUID poAttachmentId;

    private String poNumber;

    private UUID purchaseOrderId;

	public UUID getPoAttachmentId() {
		return poAttachmentId;
	}

	public void setPoAttachmentId(UUID poAttachmentId) {
		this.poAttachmentId = poAttachmentId;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public UUID getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(UUID purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

}
