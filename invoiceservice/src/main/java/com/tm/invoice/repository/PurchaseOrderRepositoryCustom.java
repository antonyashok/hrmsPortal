package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.tm.invoice.domain.PurchaseOrder;
import com.tm.invoice.enums.ActiveFlag;

@FunctionalInterface
public interface PurchaseOrderRepositoryCustom {

    List<PurchaseOrder> getPODetailsByCriteria(UUID engagementId, String searchParam, ActiveFlag active, Pageable pageable,int totalSize);

}
