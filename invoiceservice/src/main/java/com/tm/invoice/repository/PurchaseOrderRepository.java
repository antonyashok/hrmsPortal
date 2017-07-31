package com.tm.invoice.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.domain.PurchaseOrder;
import com.tm.invoice.enums.ActiveFlag;

@Repository
public interface PurchaseOrderRepository extends
		JpaRepository<PurchaseOrder, UUID>,PurchaseOrderRepositoryCustom {

	@Query("SELECT p.purchaseOrderId as purchaseOrderId FROM PurchaseOrder as p WHERE purchaseOrderId=:purchaseOrderId")
	Map<String, Object> getPurchaseOrderById(
			@Param(InvoiceConstants.PURCHASE_ORDER_ID) UUID purchaseOrderId);

	@Query("SELECT po FROM PurchaseOrder as po WHERE purchaseOrderId=:purchaseOrderId")
	PurchaseOrder getPurchaseOrderData(
			@Param(InvoiceConstants.PURCHASE_ORDER_ID) UUID purchaseOrderId);

	@Query("SELECT poNumber FROM PurchaseOrder as po WHERE purchaseOrderId=:purchaseOrderId")
	String getPurchaseOrderNumber(
			@Param(InvoiceConstants.PURCHASE_ORDER_ID) UUID purchaseOrderId);

	@Query("SELECT po FROM PurchaseOrder as po WHERE po.poNumber=:poNumber")
	List<PurchaseOrder> getPurchaseOrderDet(
			@Param(InvoiceConstants.PONUMBER) String poNumber);

	@Query("SELECT COUNT(*) FROM PurchaseOrder as po WHERE poNumber=:poNumber")
	int getPoNumberCount(@Param(InvoiceConstants.PO_NUMBER) String poNumber);

	@Query("FROM PurchaseOrder WHERE poNumber=:poNumber")
	PurchaseOrder getChildPurchaseOrderId(
			@Param(InvoiceConstants.PO_NUMBER) String poNumber);

	@Query("delete FROM PurchaseOrder WHERE purchaseOrderId=:purchaseOrderId")
	Long deleteContractorFromPo(@Param("purchaseOrderId") Long purchaseOrderId);

	@Query("Select count(*) FROM PurchaseOrder WHERE poNumber=:poNumber")
	int getPoCountForPoNumber(@Param("poNumber") String poNumber);

	@Query("FROM PurchaseOrder WHERE poNumber=:poNumber and  purchaseOrderId!=:purchaseOrderId")
	PurchaseOrder findByPoNumber(@Param("poNumber") String poNumber,
			@Param("purchaseOrderId") UUID purchaseOrderId);

    PurchaseOrder findByPoNumber(String poNumber);

    PurchaseOrder findByPurchaseOrderId(UUID purchaseOrderId);

    @Query("FROM PurchaseOrder WHERE engagementId=:engagementId and  active=:active")
    List<PurchaseOrder> findByEngagementIdAndByActive(@Param("engagementId")UUID engagementId,@Param("active")ActiveFlag active,Pageable pageable);

    @Query("FROM PurchaseOrder WHERE engagementId=:engagementId and active=:active")
    List<PurchaseOrder> findByEngagementIdAndByActive(@Param("engagementId")UUID engagementId,
            @Param("active")ActiveFlag active);

    @Query("FROM PurchaseOrder WHERE parentPurchaseOrderId=:parentPurchaseOrderId ")
    List<PurchaseOrder> findByParentPurchaseOrderId(@Param("parentPurchaseOrderId")UUID parentPurchaseOrderId);

    @Query("FROM PurchaseOrder WHERE poNumber=:poNumber and  purchaseOrderId!=:purchaseOrderId and engagementId=:engagementId ")
    PurchaseOrder findByPoNumberAndByEngagementIdAndByPurchaseOrderId(@Param("poNumber") String poNumber,
            @Param("purchaseOrderId") UUID purchaseOrderId,
            @Param("engagementId")UUID engagementId);

    @Query("FROM PurchaseOrder WHERE poNumber=:poNumber and engagementId=:engagementId ")
    PurchaseOrder findByPoNumberAndByEngagementId(@Param("poNumber")String poNumber,@Param("engagementId") UUID engagementId);
}
