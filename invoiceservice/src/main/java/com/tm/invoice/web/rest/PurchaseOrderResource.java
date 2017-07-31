package com.tm.invoice.web.rest;

import java.util.UUID;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.dto.PoContractorsViewDTO;
import com.tm.invoice.dto.PurchaseOrderDTO;
import com.tm.invoice.resource.assembler.PoContractorsViewResourceAssembler;
import com.tm.invoice.resource.assembler.PurchaseOrderResourceAssembler;
import com.tm.invoice.service.PurchaseOrderService;

@RestController
public class PurchaseOrderResource {

    private PurchaseOrderResourceAssembler purchaserOrderResourceAssembler;
    
    private PoContractorsViewResourceAssembler poContractorsViewResourceAssembler;
    
    private PurchaseOrderService purchaseOrderService;

    @Inject
    public PurchaseOrderResource(
            PurchaseOrderResourceAssembler purchaserOrderResourceAssembler,
            PoContractorsViewResourceAssembler poContractorsViewResourceAssembler,
            PurchaseOrderService purchaseOrderService){
        this.purchaserOrderResourceAssembler = purchaserOrderResourceAssembler;
        this.poContractorsViewResourceAssembler = poContractorsViewResourceAssembler;
        this.purchaseOrderService = purchaseOrderService;
    }


    @RequestMapping(value = "/po", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiredAuthority({InvoiceConstants.FINANCE_REPRESENTATIVE})
    public ResponseEntity<PurchaseOrderDTO> createPO(
            @Valid @RequestBody PurchaseOrderDTO purchaseOrderDetailDTO) {
        purchaseOrderService.createPO(purchaseOrderDetailDTO);
        PurchaseOrderDTO purchaseOrder = new PurchaseOrderDTO();
        purchaseOrder.setStatus(InvoiceConstants.OK);
        return new ResponseEntity<>(purchaseOrder, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/po", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
	public ResponseEntity<PurchaseOrderDTO> updatePO(@RequestBody PurchaseOrderDTO purchaseOrderDTO) {
	    purchaseOrderService.updatePO(purchaseOrderDTO);
		PurchaseOrderDTO purchaseOrder = new PurchaseOrderDTO();
		purchaseOrder.setStatus(InvoiceConstants.OK);
		return new ResponseEntity<>(purchaseOrder, HttpStatus.OK);
	}

    @RequestMapping(value = "/po", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
    public PagedResources<PurchaseOrderDTO> getAllPODetails(@RequestParam UUID engagementId,@RequestParam String searchParam,@RequestParam String active,Pageable pageable, PagedResourcesAssembler<PurchaseOrderDTO> pagedAssembler) {
             Page<PurchaseOrderDTO> result = purchaseOrderService.getAllPODetails(engagementId,searchParam,active, pageable);
         return pagedAssembler.toResource(result, purchaserOrderResourceAssembler);
    }
    
    
    @RequestMapping(value = "/po/{purchaseOrderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
    public PurchaseOrderDTO getPO(@PathVariable("purchaseOrderId") UUID purchaseOrderId) {
        return purchaseOrderService.getPurchaseOrder(purchaseOrderId);
    }
    
    @RequestMapping(value = "/contractors/po/{engagementId}", method = RequestMethod.GET)
    @RequiredAuthority({InvoiceConstants.FINANCE_REPRESENTATIVE})
    public PagedResources<PoContractorsViewDTO> getAllContractorDetailsByPurchaseOrderId(@PathVariable("engagementId") UUID engagementId,  Pageable pageable,  PagedResourcesAssembler<PoContractorsViewDTO> pagedAssembler) {
        Page<PoContractorsViewDTO> result=purchaseOrderService.getAllContractorDetailsByEngagementId(engagementId,pageable);
        return pagedAssembler.toResource(result,poContractorsViewResourceAssembler);
    }
    
    @RequestMapping(value = "/po/{engagementId}/contractors", method = RequestMethod.GET)
    @RequiredAuthority({InvoiceConstants.FINANCE_REPRESENTATIVE})
    public PagedResources<PoContractorsViewDTO> getAllContractorDetailsByContractorName(@PathVariable("engagementId") UUID engagementId,@RequestParam("searchParam") String searchParam,  Pageable pageable,  PagedResourcesAssembler<PoContractorsViewDTO> pagedAssembler) {
        Page<PoContractorsViewDTO> result=purchaseOrderService.getAllContractorDetailsByContractorName(engagementId,searchParam,pageable);
        return pagedAssembler.toResource(result,poContractorsViewResourceAssembler);
    }
    

}