package com.tm.invoice.web.rest;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.dto.InvoiceQueueAttachmentDTO;
import com.tm.invoice.dto.InvoiceQueueDTO;
import com.tm.invoice.service.InvoiceAttachmentService;
import com.tm.invoice.mongo.domain.Status;
import com.tm.invoice.service.InvoiceQueueService;

@RestController
@RequestMapping("/invoiceQueues")
public class InvoiceQueueResource {

  private InvoiceQueueService invoiceQueueService;
  private InvoiceAttachmentService invoiceAttachmentService;

  @Inject
  public InvoiceQueueResource(InvoiceQueueService invoiceQueueService,
      InvoiceAttachmentService invoiceAttachmentService) {
    this.invoiceQueueService = invoiceQueueService;
    this.invoiceAttachmentService = invoiceAttachmentService;
  }

  @RequestMapping(method = RequestMethod.GET)
  @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
  public PagedResources<Resource<InvoiceQueueDTO>> getInvoiceQueues(
      @RequestParam(value = "billingSpecialistId", required = false) Long billingSpecialistId,
      Pageable pageable, PagedResourcesAssembler<InvoiceQueueDTO> pagedAssembler) {
    Page<InvoiceQueueDTO> result =
        invoiceQueueService.getInvoiceQueues(billingSpecialistId, pageable);
    return pagedAssembler.toResource(result);
  }

  @RequestMapping(value = "attachments/{id}", method = RequestMethod.GET)
  @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
  public ResponseEntity<InvoiceQueueAttachmentDTO> getAttachments(@PathVariable("id") String id)
      throws IOException {
    return new ResponseEntity<>(
        invoiceAttachmentService.getInvoiceQueueFiles(id), HttpStatus.OK);
  }

  @RequestMapping(path = "/status/update", method = RequestMethod.PUT)
  @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
  public Status updateInvoiceQueueStatus(@RequestBody InvoiceQueueDTO invoiceQueueDTO) {
    invoiceQueueService.updateInvoiceQueueStatus(invoiceQueueDTO);
    return new Status("Ok");
  }
  
  @RequestMapping(value = "/{exception}",method = RequestMethod.GET)
  @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
 	public PagedResources<Resource<InvoiceQueueDTO>> getInvoiceQueuesException(
 			@RequestParam(value = "billingSpecialistId", required = false) Long billingSpecialistId,
 			Pageable pageable,
 			PagedResourcesAssembler<InvoiceQueueDTO> pagedAssembler) {
 		Page<InvoiceQueueDTO> result = invoiceQueueService.getInvoiceException(billingSpecialistId,pageable);
 		return pagedAssembler.toResource(result);
 	}


}
