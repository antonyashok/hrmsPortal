package com.tm.invoice.mongo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.mongo.domain.PurchaseOrderAttachment;

@Repository
public interface InvoiceAttachmentImageRepository extends MongoRepository<PurchaseOrderAttachment, UUID> {

    @Query("{'purchaseOrderId':?0}")
    PurchaseOrderAttachment findInvoiceAttachmentByPurchaseOrderId(
            @Param(InvoiceConstants.PURCHASE_ORDER_ID) Long purchaseOrderId);

    @Query("{'poNumber':?0}")
    PurchaseOrderAttachment findInvoiceAttachmentByPurchaseOrderNumber(
            @Param(InvoiceConstants.PO_NUMBER) String poNumber);

    @Query("{'purchaseOrderId':?0},{'fileName':?1}")
    List<PurchaseOrderAttachment> getPOAttachmentListByPOIdAndFilename(
            @Param(InvoiceConstants.PURCHASE_ORDER_ID) Long purchaseOrderId,
            @Param(InvoiceConstants.FILE_NAME) String fileName);
}
