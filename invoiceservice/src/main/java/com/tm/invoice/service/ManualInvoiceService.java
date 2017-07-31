package com.tm.invoice.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.dto.EmployeeEngagementDetailsViewDTO;
import com.tm.invoice.mongo.domain.ManualInvoice;
import com.tm.invoice.mongo.domain.Status;
import com.tm.invoice.mongo.dto.ManualInvoiceDTO;

public interface ManualInvoiceService {

	ManualInvoice generateManualInvoice(ManualInvoiceDTO manualInvoiceDTO);

	Page<ManualInvoiceDTO> getAllManualInvoices(String action, Pageable pageable);

	ManualInvoiceDTO getManualInvoices(UUID invoiceId);

	String updateManualInvoiceStatus(ManualInvoiceDTO manualInvoiceDTO)  throws IOException;

	Page<EmployeeEngagementDetailsViewDTO> getContractorsDetailsByEngagement(
			String contractorName, UUID engagementId, List<Long> contractorIds, Pageable pageable);

	Status deleteRejectedManualInvoice(UUID invoiceId);

}
