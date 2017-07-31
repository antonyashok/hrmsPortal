package com.tm.invoice.web.rest;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.dto.InvoiceQueueDTO;
import com.tm.invoice.service.InvoiceBillWatchService;

@RestController
@RequestMapping("/billWatch")
public class InvoiceBillWatchResource {

	private InvoiceBillWatchService invoiceBillWatchService;

	@Inject
	public InvoiceBillWatchResource(InvoiceBillWatchService invoiceBillWatchService) {
		this.invoiceBillWatchService = invoiceBillWatchService;
	}

	@RequestMapping(method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
	public PagedResources<Resource<InvoiceQueueDTO>> getInvoiceQueues(
			@RequestParam(value = "billingSpecialistId", required = false) Long billingSpecialistId, Pageable pageable,
			PagedResourcesAssembler<InvoiceQueueDTO> pagedAssembler) {
		Page<InvoiceQueueDTO> result = invoiceBillWatchService.getInvoiceQueues(billingSpecialistId, pageable);
		return pagedAssembler.toResource(result);
	}
	
	@RequestMapping(value = "/createInvoiceReturn", method = RequestMethod.PUT)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
	public Map<String, Object> createInvoiceReturn(String invoiceNumber){
		Map<String, Object> finalMap = new HashMap<>();
		finalMap.put("InvoiceReturnDTO",
				invoiceBillWatchService.createInvoiceReturn(invoiceNumber));
        finalMap.put("STATUS","OK");
        return finalMap;
	}
	
	
	@RequestMapping(value = "/createExceptionReport", method = RequestMethod.PUT)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
	public Map<String, Object> createExceptionReport(String invoiceNumber){
		Map<String, Object> finalMap = new HashMap<>();
//		finalMap.put("InvoiceQueueDTO",
//				invoiceBillWatchService.createExceptionReport(invoiceNumber));
		invoiceBillWatchService.createExceptionReport(invoiceNumber);
        finalMap.put("STATUS","OK");
        return finalMap;
	}
	
	
	
	
}
