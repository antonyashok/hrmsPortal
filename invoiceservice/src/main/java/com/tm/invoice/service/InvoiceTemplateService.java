package com.tm.invoice.service;

import java.util.List;

import com.tm.invoice.dto.InvoiceTemplateDTO;

public interface InvoiceTemplateService {

	List<InvoiceTemplateDTO> getTemplateList();

	InvoiceTemplateDTO getTemplate(Long invoiceTemplateId);
}
