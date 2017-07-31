package com.tm.invoice.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.dto.PoContractorsViewDTO;
import com.tm.invoice.dto.PurchaseOrderDTO;
import com.tm.invoice.engagement.dto.EngagementDTO;

public interface PurchaseOrderService {

    void createPO(PurchaseOrderDTO purchaseOrderDTO);

    EngagementDTO getEngagementDetails(UUID engagementId);

    void updatePO(PurchaseOrderDTO purchaseOrderDTO);

    Page<PurchaseOrderDTO> getAllPODetails(UUID engagementId, String searchParam,
            String activeFlagValue, Pageable pageable);

    Page<PoContractorsViewDTO> getAllContractorDetailsByEngagementId(UUID engagementId,
            Pageable pageable);

    Page<PoContractorsViewDTO> getAllContractorDetailsByContractorName(UUID engagementId,
            String searchParam, Pageable pageable);

    PurchaseOrderDTO getPurchaseOrder(UUID purchaseOrderId);

}
