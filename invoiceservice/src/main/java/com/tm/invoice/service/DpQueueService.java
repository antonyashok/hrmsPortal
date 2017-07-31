package com.tm.invoice.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.dto.BillingDetailsDTO;
import com.tm.invoice.dto.DPQueueListDTO;
import com.tm.invoice.dto.DpQueueDTO;

public interface DpQueueService {

	Page<DpQueueDTO> getDpQueues(Long billToClientId, String status, Pageable pageable);

	DpQueueDTO getDpQueueDetails(UUID id);

	DpQueueDTO saveDirectPlacement(DpQueueDTO dpQueueDTO);

	DpQueueDTO updateDirectPlacement(DpQueueDTO dpQueueDTO);

	BillingDetailsDTO generateInvoice(BillingDetailsDTO billingDetailsDTO);
	
	DPQueueListDTO getNotGeneratedDpQueues(Long billToClientId);

}
