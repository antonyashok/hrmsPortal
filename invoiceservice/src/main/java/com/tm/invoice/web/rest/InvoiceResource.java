package com.tm.invoice.web.rest;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.dto.EngagementContractorsDTO;
import com.tm.invoice.dto.InvoiceReturnDTO;
import com.tm.invoice.mongo.domain.InvoiceReturn;
import com.tm.invoice.service.InvoiceService;

@RestController
public class InvoiceResource {

    @Autowired
    InvoiceService invoiceService;

    @RequestMapping(value = "/returnrequest", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public PagedResources<Resource<InvoiceReturnDTO>> getReturnRequest(Pageable pageable,
            PagedResourcesAssembler<InvoiceReturnDTO> pagedAssembler, String searchParam) {
        // return invoiceService.getReturnRequest(pageable,searchParam);

        return pagedAssembler.toResource(invoiceService.getReturnRequest(pageable, searchParam,
                invoiceService.getLoggedInUser().getEmployeeId()));
    }

    @RequestMapping(value = "/returnrequest/{invoicequeueid}", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public InvoiceReturnDTO getReturnRequestById(@PathVariable UUID invoicequeueid) {
        return invoiceService.getReturnRequestById(invoicequeueid);
    }

    @RequestMapping(value = "/returnrequest", method = RequestMethod.POST)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public InvoiceReturn createReturnRequest(@RequestBody InvoiceReturnDTO invoiceReturnDTO) {
        return invoiceService.createReturnRequest(invoiceReturnDTO,
                invoiceService.getLoggedInUser());
    }

    @RequestMapping(value = "/teamreturnrequest", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER })
    public PagedResources<Resource<InvoiceReturnDTO>> getTeamReturnRequest(Pageable pageable,
            PagedResourcesAssembler<InvoiceReturnDTO> pagedAssembler) {
        return pagedAssembler.toResource(invoiceService.getTeamReturnRequest(pageable,
                invoiceService.getLoggedInUser().getEmployeeId()));
    }


    @RequestMapping(value = "/myreturnrequest", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
    public PagedResources<Resource<InvoiceReturnDTO>> getMyReturnRequest(Pageable pageable,
            PagedResourcesAssembler<InvoiceReturnDTO> pagedAssembler) {
        return pagedAssembler.toResource(invoiceService.getMyReturnRequest(pageable,
                invoiceService.getLoggedInUser().getEmployeeId()));
    }

    @RequestMapping(value = "/updateapprovalstatus", method = RequestMethod.PUT)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public Map<String, Object> updateReturnApprovalStatus(
            @RequestBody InvoiceReturnDTO invoiceReturnDTO) {
        Map<String, Object> finalMap = new HashMap<>();
        finalMap.put("InvoiceReturnDTO",
                invoiceService.updateReturnApprovalStatus(invoiceReturnDTO));
        finalMap.put("STATUS", "OK");
        return finalMap;
    }

    @RequestMapping(value = "/engagementContractors/{engagementId}", method = RequestMethod.GET)
	@RequiredAuthority({ InvoiceConstants.PROFILE_VIEW })
	public ResponseEntity<List<EngagementContractorsDTO>> getEngagementContractors(@PathVariable String engagementId) {
		List<EngagementContractorsDTO> engagementContractorDTO = invoiceService
				.getEngagementContracor(UUID.fromString(engagementId));
		return new ResponseEntity<>(engagementContractorDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/getinvoiceapprovalcount", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.ALL })
	public ResponseEntity<Long> getInvoiceApprovalCountByUserId() throws ParseException {
		return new ResponseEntity<>(invoiceService.getInvoiceApprovalCountByUserId(invoiceService.getLoggedInUser().getEmployeeId()), 
				HttpStatus.OK);
	}
}
