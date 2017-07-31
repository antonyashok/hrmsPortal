package com.tm.invoice.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.tm.invoice.dto.InvoiceAttachmentsDTO;
import com.tm.invoice.dto.InvoiceQueueAttachmentDTO;

public interface InvoiceAttachmentService {

	List<InvoiceAttachmentsDTO> uploadMultipleInvoiceFiles(
			MultipartFile[] files, String sourceReferenceId,
			String sourceReferenceName) throws ParseException, IOException;

	List<InvoiceAttachmentsDTO> getInvoiceFileDetails(String sourceReferenceId);

	String deleteInvoiceFile(UUID invoiceAttachmentId);

	List<InvoiceAttachmentsDTO> updateFileDetails(String sourceReferenceId,
			List<InvoiceAttachmentsDTO> list);

	InvoiceQueueAttachmentDTO getInvoiceQueueFiles(String sourceReferenceId)
			throws IOException;

	InvoiceAttachmentsDTO getInvoiceFile(UUID invoiceAttachmentId)
			throws IOException;

}
