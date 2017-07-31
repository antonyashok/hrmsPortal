package com.tm.invoice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.dto.EngagementContractorsDTO;
import com.tm.invoice.dto.InvoiceReturnDTO;
import com.tm.invoice.mongo.domain.InvoiceReturn;

public interface InvoiceService {

	Page<InvoiceReturnDTO> getReturnRequest(Pageable pageable,
			String searchParam, Long userid);

	InvoiceReturnDTO getReturnRequestById(UUID invoicequeueid);

	InvoiceReturn createReturnRequest(InvoiceReturnDTO invoiceReturnDTO,
			EmployeeProfileDTO employeeProfileDTO);

	EmployeeProfileDTO getLoggedInUser();

	Page<InvoiceReturnDTO> getTeamReturnRequest(Pageable pageable, Long userid);

	Page<InvoiceReturnDTO> getMyReturnRequest(Pageable pageable, Long userid);

	// InvoiceReturnDTO updateReturnApprovalStatus(UUID invoiceReturnId, String
	// status, String
	// returnComments, String approvalComments);

	InvoiceReturnDTO updateReturnApprovalStatus(
			InvoiceReturnDTO invoiceReturnDTO);
	
	List<EngagementContractorsDTO> getEngagementContracor(UUID engagementId);

	Long getInvoiceApprovalCountByUserId(Long employeeId);
}
