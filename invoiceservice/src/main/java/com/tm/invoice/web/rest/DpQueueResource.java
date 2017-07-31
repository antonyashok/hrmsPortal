package com.tm.invoice.web.rest;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.dto.BillingDetailsDTO;
import com.tm.invoice.dto.DPQueueListDTO;
import com.tm.invoice.dto.DpQueueDTO;
import com.tm.invoice.service.DpQueueService;

@RestController
@RequestMapping("/dpQueue")
public class DpQueueResource {

	private DpQueueService dpQueueService;

	@Inject
	public DpQueueResource(DpQueueService dpQueueService) {
		this.dpQueueService = dpQueueService;
	}

	@RequestMapping(method = RequestMethod.GET)
    @RequiredAuthority({ AuthoritiesConstants.FINANCE_REPRESENTATIVE })
	public PagedResources<Resource<DpQueueDTO>> getDpQueues(
			@RequestParam(value = "billToClientId", required = false) Long billToClientId,
			@RequestParam(value = "status", required = false) String status, Pageable pageable,
			PagedResourcesAssembler<DpQueueDTO> pagedAssembler) {
		Page<DpQueueDTO> result = dpQueueService.getDpQueues(billToClientId, status, pageable);
		return pagedAssembler.toResource(result);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @RequiredAuthority({ AuthoritiesConstants.FINANCE_REPRESENTATIVE })
	public DpQueueDTO getTimesheetDetails(@PathVariable UUID id) throws ParseException {
		return dpQueueService.getDpQueueDetails(id);
	}

	@RequestMapping(path = "/dpSave", method = RequestMethod.POST)
    @RequiredAuthority({ AuthoritiesConstants.FINANCE_REPRESENTATIVE })
	public Map<String, Object> saveDpQueue(@RequestBody DpQueueDTO dpQueueDTO) {
		Map<String, Object> response = new HashMap<>();
		DpQueueDTO dpQueue = dpQueueService.saveDirectPlacement(dpQueueDTO);
		if (null == dpQueue) {
			response.put(InvoiceConstants.ERROR_MESSAGE,
					InvoiceConstants.UPDATION_FAILED_BECAUSE_SOME_DATA_ARE_NOT_STORED);
			response.put(InvoiceConstants.STATUS, InvoiceConstants.ERROR);
		} else {
			response.put(InvoiceConstants.STATUS, InvoiceConstants.DP_SAVED_SUCCESSFULLY);
		}
		return response;
	}

	@RequestMapping(path = "/dpUpdate", method = RequestMethod.PUT)
    @RequiredAuthority({ AuthoritiesConstants.FINANCE_REPRESENTATIVE })
	public Map<String, Object> updateDpQueue(@RequestBody DpQueueDTO dpQueueDTO) {
		Map<String, Object> response = new HashMap<>();
		DpQueueDTO dpQueue = dpQueueService.updateDirectPlacement(dpQueueDTO);
		if (null == dpQueue) {
			response.put(InvoiceConstants.ERROR_MESSAGE,
					InvoiceConstants.UPDATION_FAILED_BECAUSE_SOME_DATA_ARE_NOT_STORED);
			response.put(InvoiceConstants.STATUS, InvoiceConstants.ERROR);
		} else {
			response.put(InvoiceConstants.STATUS, InvoiceConstants.DP_SAVED_SUCCESSFULLY);
		}
		return response;
	}

	@RequestMapping(path = "/generate", method = RequestMethod.POST)
    @RequiredAuthority({ AuthoritiesConstants.FINANCE_REPRESENTATIVE })
	public Map<String, Object> generateDpInvoice(@RequestBody BillingDetailsDTO billingDetailsDTO) {
		Map<String, Object> response = new HashMap<>();
		BillingDetailsDTO billingDetails = dpQueueService.generateInvoice(billingDetailsDTO);
		if (null == billingDetails) {
			response.put(InvoiceConstants.ERROR_MESSAGE,
					InvoiceConstants.UPDATION_FAILED_BECAUSE_SOME_DATA_ARE_NOT_STORED);
			response.put(InvoiceConstants.STATUS, InvoiceConstants.ERROR);
		} else {
			response.put(InvoiceConstants.STATUS, InvoiceConstants.DP_SAVED_SUCCESSFULLY);
		}
		return response;
	}

	@RequestMapping(path = "/notGeneratedDpQueues", method = RequestMethod.GET)
    @RequiredAuthority({ AuthoritiesConstants.FINANCE_REPRESENTATIVE })
	public DPQueueListDTO getNotGeneratedDpQueues(
			@RequestParam(value = "billToClientId", required = false) Long billToClientId) {
		return dpQueueService.getNotGeneratedDpQueues(billToClientId);
	}
}
