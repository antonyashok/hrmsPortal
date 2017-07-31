package com.tm.invoice.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tm.invoice.constants.InvoiceConstants;
import com.tm.invoice.domain.PurchaseOrder;
import com.tm.invoice.enums.ActiveFlag;

@Repository
@Transactional
public interface PurchaseOrderRepository extends
		JpaRepository<PurchaseOrder, UUID> {

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

	PurchaseOrder findByPoNumber(String poNumber);

	@Query("FROM PurchaseOrder WHERE poNumber=:poNumber and  purchaseOrderId!=:purchaseOrderId")
	PurchaseOrder findByPoNumber(@Param("poNumber") String poNumber,
			@Param("purchaseOrderId") UUID purchaseOrderId);

	@Query("FROM PurchaseOrder WHERE parentPoId=:parentPoId and  purchaseOrderId!=:purchaseOrderId")
	List<PurchaseOrder> findByParentPoId(@Param("parentPoId") UUID parentPoId,
			@Param("purchaseOrderId") UUID purchaseOrderId);

	List<PurchaseOrder> findBycustomerId(Long customerId);

	@Query("FROM PurchaseOrder WHERE parentPoId IS NULL and activeFlag =:activeFlag")
	List<PurchaseOrder> getPurchaseOrdersByActiveFlagAndParentPoIdIsNull(
			@Param("activeFlag") ActiveFlag activeFlag);
	
    @Modifying
    @Transactional(transactionManager="invoiceTransactionManager")
	@Query("UPDATE PurchaseOrder i SET i.balanceRevenueAmount = :balanceRevenueAmount"
			+ " WHERE i.purchaseOrderId = :purchaseOrderId")
	Integer updatePurchaseOrderRevenueAmount(@Param("balanceRevenueAmount") Double balanceRevenueAmount ,@Param("purchaseOrderId") UUID purchaseOrderId);	

    @Modifying
    @Transactional(transactionManager="invoiceTransactionManager")
	@Query("UPDATE PurchaseOrder i SET i.balanceExpenseAmount = :balanceExpenseAmount"
			+ " WHERE i.purchaseOrderId = :purchaseOrderId")
	Integer updatePurchaseOrderExpenseAmount(@Param("balanceExpenseAmount") Double balanceExpenseAmount ,@Param("purchaseOrderId") UUID purchaseOrderId);	


}
