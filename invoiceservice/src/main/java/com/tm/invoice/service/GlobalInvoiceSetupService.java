package com.tm.invoice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.dto.GlobalInvoiceSetupDTO;
import com.tm.invoice.dto.GlobalInvoiceSetupGridDTO;
import com.tm.invoice.dto.InvoiceTemplateDTO;

public interface GlobalInvoiceSetupService {

	Page<GlobalInvoiceSetupGridDTO> getGlobalInvoiceSetups(String status,
			Pageable pageable);

	List<InvoiceTemplateDTO> getTemplateDetails();

	void saveGlobalInvoiceSetup(GlobalInvoiceSetupDTO globalInvoiceSetupDTO);

	GlobalInvoiceSetupDTO getGlobalInvoiceSetup(String timeoffId);

	void updateGlobalInvoiceSetupStatus(
			GlobalInvoiceSetupDTO globalInvoiceSetupDTO);

	List<GlobalInvoiceSetupDTO> getGlobalInvoiceSetup();
}
