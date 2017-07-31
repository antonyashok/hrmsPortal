package com.tm.invoice.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.dto.GlobalInvoiceSetupDTO;
import com.tm.invoice.dto.GlobalInvoiceSetupGridDTO;
import com.tm.invoice.dto.InvoiceTemplateDTO;
import com.tm.invoice.resource.assembler.InvoiceAssembler;
import com.tm.invoice.service.GlobalInvoiceSetupService;
import com.tm.invoice.util.InvoiceCommonUtils;

@RestController
@RequestMapping("/globalInvoiceSetup")
public class GlobalInvoiceSetupResource {

  private GlobalInvoiceSetupService globalInvoiceSetupService;
  
  private InvoiceAssembler invoiceAssembler;

  @Inject
  public GlobalInvoiceSetupResource(GlobalInvoiceSetupService globalInvoiceSetupService,InvoiceAssembler invoiceAssembler) {
    this.globalInvoiceSetupService = globalInvoiceSetupService;
    this.invoiceAssembler=invoiceAssembler;
  }

  @RequestMapping(method = RequestMethod.GET)
  @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
  public PagedResources<Resource<GlobalInvoiceSetupGridDTO>> getAllConfigurationGroup(
      @RequestParam("invoiceStatus") String invoiceStatus, Pageable pageable,
      PagedResourcesAssembler<GlobalInvoiceSetupGridDTO> pagedAssembler, String fields) {
    Page<GlobalInvoiceSetupGridDTO> result =
        globalInvoiceSetupService.getGlobalInvoiceSetups(invoiceStatus, pageable);
    return pagedAssembler.toResource(result);
  }

    @RequestMapping(value = "/templates", method = RequestMethod.GET,
            produces = {APPLICATION_JSON_VALUE})
    @RequiredAuthority({InvoiceConstants.FINANCE_REPRESENTATIVE, InvoiceConstants.FINANCE_MANAGER})
    public ResponseEntity<List<InvoiceTemplateDTO>> getTemplateDetail() {
        return new ResponseEntity<>(globalInvoiceSetupService.getTemplateDetails(), HttpStatus.OK);
    }
    
    @RequestMapping(path = "/globalinvoicesetup", method = RequestMethod.POST)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
    public ObjectNode saveGlobalInvoiceSetup(
            @Validated @RequestBody GlobalInvoiceSetupDTO globalInvoiceSetupDTO) {
    	globalInvoiceSetupService.saveGlobalInvoiceSetup(globalInvoiceSetupDTO);
        return InvoiceCommonUtils.getResponseObjectNode(InvoiceConstants.OK, true);
    }
    
    
    @RequestMapping(path = "/globalinvoicesetup/{globalinvoicesetupid}", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
    public GlobalInvoiceSetupDTO getGlobalInvoiceSetup(@PathVariable("globalinvoicesetupid") String globalinvoicesetupid) {
    	return invoiceAssembler.toResource(globalInvoiceSetupService.getGlobalInvoiceSetup(globalinvoicesetupid));
    }
    
    @RequestMapping(path = "/globalinvoiceid", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE, InvoiceConstants.FINANCE_MANAGER })
    public Map<String, UUID> getGlobalInvoiceId(){
    	UUID invoiceId=UUID.randomUUID();
    	Map<String, UUID> mapUUID=new HashMap<>();
    	mapUUID.put("globalinvoiceid", invoiceId);
        return mapUUID;
    }
    
    @RequestMapping(path = "/globalinvoicesetup", method = RequestMethod.PUT)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
    public ObjectNode updateGlobalInvoiceSetupStatus(
            @Validated @RequestBody GlobalInvoiceSetupDTO globalInvoiceSetupDTO) {
    	globalInvoiceSetupService.updateGlobalInvoiceSetupStatus(globalInvoiceSetupDTO);
        return InvoiceCommonUtils.getResponseObjectNode(InvoiceConstants.OK, true);
    }
    
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public List<GlobalInvoiceSetupDTO> getInvoicesetupInList() {
        return globalInvoiceSetupService.getGlobalInvoiceSetup();
    }
  
}
