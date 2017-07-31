package com.tm.invoice.web.rest;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.exception.InvoiceSetupException;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.constants.InvoiceSetupConstants;
import com.tm.invoice.domain.InvoiceSetupView;
import com.tm.invoice.dto.InvoiceSetupDTO;
import com.tm.invoice.mongo.dto.InvoiceSetupActivitiesLogDTO;
import com.tm.invoice.resource.assembler.InvoiceSetupActivitiesLogAssembler;
import com.tm.invoice.service.InvoiceAttachmentService;
import com.tm.invoice.service.InvoiceSetupService;

@RestController
@RequestMapping("/invoiceSetup")
public class InvoiceSetupResource {

    private InvoiceSetupService invoiceSetupService;

    private InvoiceAttachmentService invoiceAttachmentsService;
    
    private InvoiceSetupActivitiesLogAssembler  invoiceSetupActivitiesLogAssembler;

    @Inject
    public InvoiceSetupResource(InvoiceSetupService invoiceSetupService,
            InvoiceAttachmentService invoiceAttachmentsService) {
        this.invoiceSetupService = invoiceSetupService;
        this.invoiceAttachmentsService = invoiceAttachmentsService;
    }
 
    @RequiredAuthority({InvoiceConstants.FINANCE_MANAGER, InvoiceConstants.FINANCE_REPRESENTATIVE})
    @RequestMapping(value = "/list/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<InvoiceSetupView> getAllExistingActiveSetups(
            @PathVariable("customerId") Long customerId) {
        return invoiceSetupService.getAllExistingActiveSetups(customerId);
    }
    
    @RequiredAuthority({InvoiceConstants.FINANCE_MANAGER, InvoiceConstants.FINANCE_REPRESENTATIVE})
    @RequestMapping(value = "/setup", produces = MediaType.APPLICATION_JSON_VALUE)
    public InvoiceSetupDTO populateExistingSetupDetails(
            @RequestParam("invoiceSetupId") UUID invoiceSetupId,@RequestParam("invoiceType") String invoiceType,@RequestParam("customerId") Long customerId) {
        return invoiceSetupService.populateExistingSetupDetails(invoiceSetupId,invoiceType,customerId);
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @RequiredAuthority({InvoiceConstants.FINANCE_MANAGER, InvoiceConstants.FINANCE_REPRESENTATIVE})
    public InvoiceSetupDTO saveInvoiceSetup(@Validated @RequestBody InvoiceSetupDTO invoiceSetup)
            throws ParseException {
        InvoiceSetupDTO response = invoiceSetupService.saveInvoiceSetup(invoiceSetup);
        if (null == invoiceSetup) {
            throw new InvoiceSetupException(InvoiceSetupConstants.ERR_IN_SAVE_BILL_TO_MANAGER);
        }
        if (null == invoiceSetup.getInvoiceSetupId()
                && CollectionUtils.isNotEmpty(invoiceSetup.getInvoiceSetupAttachements())) {
            invoiceAttachmentsService.updateFileDetails(response.getInvoiceSetupId().toString(),
                    invoiceSetup.getInvoiceSetupAttachements());
        }
        return response;
    }
    
    @RequestMapping(value = "activities/{sourceReferenceId}", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public Resources<InvoiceSetupActivitiesLogDTO> getActivityLog(
            @PathVariable("sourceReferenceId") UUID sourceReferenceId) {
        List<InvoiceSetupActivitiesLogDTO> activityLogDTOs =
                invoiceSetupService.getInvoiceSetupActivityLog(sourceReferenceId);
        return invoiceSetupActivitiesLogAssembler.toActivitiesLogResource(activityLogDTOs,
                sourceReferenceId);
    }
    
    @RequestMapping(path = "/{engagementId}", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public InvoiceSetupDTO getInvoiceSetup(
            @PathVariable(InvoiceConstants.ENGAGEMENTID) UUID engagementId) {
        return invoiceSetupService.getInvoiceSetup(engagementId);
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public InvoiceSetupDTO editInvoiceSetup(@Validated @RequestBody InvoiceSetupDTO invoiceSetup)
            throws ParseException {
        return  invoiceSetupService.saveInvoiceSetup(invoiceSetup);
    }

}