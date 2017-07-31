package com.tm.invoice.mongo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.mongo.domain.InvoiceQueue;

public interface InvoiceQueueRepositoryCustom {

	public Page<InvoiceQueue> getInvoiceQueues(Long billingSpecialistId, List<String> statuses, Pageable pageable,
			boolean isHistorical);

	Page<InvoiceQueue> getInvoiceException(Long billingSpecialistId, Pageable pageable, Boolean isHistorical);

	List<InvoiceQueue> getInvoiceQueues(List<UUID> invoiceQueueIds);

	List<InvoiceQueue> getInvoiceQueuesByInvoiceNumber(String invoiceNumber, List<String> statuses);
	
	Long getInvoiceQueueCountByBillingSpecialistIdAndStatus(String status, Long userId); 
}
