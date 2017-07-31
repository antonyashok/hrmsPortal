package com.tm.invoice.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.dto.HistoricalDTO;

public interface HistoricalService {

	Page<HistoricalDTO> getHistoricals(Long billingSpecialistId,
			Pageable pageable);

	HistoricalDTO saveHistoricals(UUID invoiceQueueId);

	void saveInvoiceHistoricals(UUID id, String status, Long employeeId);
}
