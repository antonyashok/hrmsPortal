package com.tm.invoice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.dto.InvoiceQueueDTO;

public interface InvoiceQueueService {

	Page<InvoiceQueueDTO> getInvoiceQueues(Long billingSpecialistId,
			Pageable pageable);

	String updateInvoiceQueueStatus(InvoiceQueueDTO invoiceQueueDTO);

	Page<InvoiceQueueDTO> getInvoiceException(Long billingSpecialistId,
			Pageable pageable);
}
