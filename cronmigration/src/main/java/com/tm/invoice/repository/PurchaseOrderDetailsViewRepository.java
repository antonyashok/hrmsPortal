package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tm.invoice.domain.PurchaseOrderDetailsView;
import com.tm.invoice.enums.ActiveFlag;

@Repository
public interface PurchaseOrderDetailsViewRepository extends
		JpaRepository<PurchaseOrderDetailsView, UUID> {

	List<PurchaseOrderDetailsView> findPurchaseOrderDetailsViewsByPoActiveFlagAndPoParentIdIsNull(
			ActiveFlag activeFlag);

}