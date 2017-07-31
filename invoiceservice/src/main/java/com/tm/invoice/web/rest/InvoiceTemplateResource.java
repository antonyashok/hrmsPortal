package com.tm.invoice.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.dto.InvoiceTemplateDTO;
import com.tm.invoice.service.InvoiceTemplateService;

@RestController
@RequestMapping("/template")
public class InvoiceTemplateResource {

    private static final String INV_TEMP_ID = "invoiceTemplateId";
    
    private InvoiceTemplateService invoiceTemplateService;

    @Inject
    public InvoiceTemplateResource(InvoiceTemplateService invoiceTemplateService) {

        this.invoiceTemplateService = invoiceTemplateService;
    }

    @RequestMapping(value = "/templateList", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE,InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.PROFILE_VIEW})
    public List<InvoiceTemplateDTO> getTemplateList() {
        return invoiceTemplateService.getTemplateList();
    }

    @RequestMapping(value = "/{invoiceTemplateId}", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE,InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.PROFILE_VIEW })
    public InvoiceTemplateDTO getTemplate(@PathVariable(INV_TEMP_ID) Long invoiceTemplateId) {
        return invoiceTemplateService.getTemplate(invoiceTemplateId);
    }
}
