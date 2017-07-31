package com.tm.invoice.web.rest;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.dto.HistoricalDTO;
import com.tm.invoice.service.HistoricalService;

@RestController
@RequestMapping("/historicals")
public class HistoricalResource {

	private HistoricalService historicalService;

	@Inject
	public HistoricalResource(HistoricalService historicalService) {
		this.historicalService = historicalService;
	}

	@RequestMapping(method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
	public PagedResources<Resource<HistoricalDTO>> getHistoricals(
			@RequestParam(value = "billingSpecialistId", required = false) Long billingSpecialistId,
			Pageable pageable,
			PagedResourcesAssembler<HistoricalDTO> pagedAssembler) {
		Page<HistoricalDTO> result = historicalService.getHistoricals(
				billingSpecialistId, pageable);
		return pagedAssembler.toResource(result);
	}

	@RequestMapping(value = "/{invoiceQueueId}", method = RequestMethod.POST)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
	public HistoricalDTO saveHistoricals(@PathVariable UUID invoiceQueueId) {
		return historicalService.saveHistoricals(invoiceQueueId);
	}

}
