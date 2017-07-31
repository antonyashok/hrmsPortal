package com.tm.invoice.web.rest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.exception.FileUploadException;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.dto.InvoiceAttachmentsDTO;
import com.tm.invoice.mongo.domain.Status;
import com.tm.invoice.service.InvoiceAttachmentService;

@RestController
@RequestMapping("/attachment")
public class InvoiceAttachmentsResource {

	private InvoiceAttachmentService invoiceAttachmentService;

	@Inject
	public InvoiceAttachmentsResource(
			InvoiceAttachmentService invoiceAttachmentService) {
		this.invoiceAttachmentService = invoiceAttachmentService;
	}

	@RequestMapping(value = "/{sourceReferenceName}/{sourceReferenceId}/files", method = RequestMethod.POST)
    @RequiredAuthority({ InvoiceConstants.PROFILE_VIEW })
	public List<InvoiceAttachmentsDTO> uploadMultipleInvoiceFiles(
			@RequestPart("files") MultipartFile[] files,
			@PathVariable("sourceReferenceName") String sourceReferenceName,
			@PathVariable("sourceReferenceId") String sourceReferenceId)
			throws Exception {
		return invoiceAttachmentService.uploadMultipleInvoiceFiles(files,
				sourceReferenceId, sourceReferenceName);
	}

	@RequestMapping(value = "/{sourceReferenceId}/fileDetails", method = RequestMethod.GET)
	@RequiredAuthority({ InvoiceConstants.PROFILE_VIEW })
	public List<InvoiceAttachmentsDTO> getTimesheetFileDetails(
			@PathVariable("sourceReferenceId") String sourceReferenceId)
			throws FileUploadException {
		return invoiceAttachmentService
				.getInvoiceFileDetails(sourceReferenceId);
	}

	@RequestMapping(value = "/file/{invoiceAttachmentId}", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.PROFILE_VIEW })
	public InvoiceAttachmentsDTO getTimesheetFile(
			@PathVariable("invoiceAttachmentId") UUID invoiceAttachmentId)
			throws FileUploadException, IOException {
		return invoiceAttachmentService.getInvoiceFile(invoiceAttachmentId);
	}

	@RequestMapping(value = "/file/{invoiceAttachmentId}", method = RequestMethod.DELETE)
    @RequiredAuthority({ InvoiceConstants.PROFILE_VIEW })
	public @ResponseBody Status deleteTimesheetFile(
			@PathVariable("invoiceAttachmentId") UUID invoiceAttachmentId)
			throws Exception {
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new Status(
				invoiceAttachmentService.deleteInvoiceFile(invoiceAttachmentId));

	}

}
