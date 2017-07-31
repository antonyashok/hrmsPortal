package com.tm.invoice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.dto.InvoiceQueueDTO;
import com.tm.invoice.dto.InvoiceReturnDTO;

public interface InvoiceBillWatchService {

	Page<InvoiceQueueDTO> getInvoiceQueues(Long billingSpecialistId,
			Pageable pageable);

	InvoiceReturnDTO createInvoiceReturn(String invoiceNumber);

	EmployeeProfileDTO getLoggedInUser();

	InvoiceQueueDTO createExceptionReport(String invoiceNumber);

}
