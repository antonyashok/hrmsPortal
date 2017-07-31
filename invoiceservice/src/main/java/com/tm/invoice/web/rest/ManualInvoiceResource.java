package com.tm.invoice.web.rest;

import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.dto.EmployeeEngagementDetailsViewDTO;
import com.tm.invoice.mongo.domain.ManualInvoice;
import com.tm.invoice.mongo.domain.Status;
import com.tm.invoice.mongo.dto.ManualInvoiceDTO;
import com.tm.invoice.resource.assembler.EmployeeEngagementDetailsViewAssembler;
import com.tm.invoice.resource.assembler.ManualInvoiceResourceAssembler;
import com.tm.invoice.service.InvoiceAttachmentService;
import com.tm.invoice.service.ManualInvoiceService;

@RestController
@RequestMapping("/manual")
public class ManualInvoiceResource {

    private ManualInvoiceService manualInvoiceService;

    private ManualInvoiceResourceAssembler manualInvoiceResourceAssembler;

    private InvoiceAttachmentService invoiceAttachmentsService;
    private EmployeeEngagementDetailsViewAssembler employeeEngagementDetailsViewAssembler;

    @Inject
    public ManualInvoiceResource(ManualInvoiceService manualInvoiceService,
            ManualInvoiceResourceAssembler manualInvoiceResourceAssembler,
            InvoiceAttachmentService invoiceAttachmentsService,
            EmployeeEngagementDetailsViewAssembler employeeEngagementDetailsViewAssembler) {
        this.manualInvoiceService = manualInvoiceService;
        this.manualInvoiceResourceAssembler = manualInvoiceResourceAssembler;
        this.invoiceAttachmentsService = invoiceAttachmentsService;
        this.employeeEngagementDetailsViewAssembler = employeeEngagementDetailsViewAssembler;
    }

    @RequestMapping(method = RequestMethod.POST)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public Status generateManualInvoice(@Validated @RequestBody ManualInvoiceDTO manualInvoiceDTO) {
        ManualInvoice response = manualInvoiceService.generateManualInvoice(manualInvoiceDTO);
        if (null != response && null != manualInvoiceDTO.getManualInvoiceAttachements()) {
            invoiceAttachmentsService.updateFileDetails(response.getInvoiceId().toString(),
                    manualInvoiceDTO.getManualInvoiceAttachements());
        }
        return new Status("ok");
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public PagedResources<ManualInvoiceDTO> getAllManualInvoice(@RequestParam String action,
            Pageable pageable, PagedResourcesAssembler<ManualInvoiceDTO> pagedAssembler) {
        Page<ManualInvoiceDTO> result = manualInvoiceService.getAllManualInvoices(action, pageable);
        return pagedAssembler.toResource(result, manualInvoiceResourceAssembler);
    }

    @RequestMapping(value = "/{invoiceId}", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public ManualInvoiceDTO getManualInvoice(@PathVariable("invoiceId") UUID invoiceId) {
        ManualInvoiceDTO result = manualInvoiceService.getManualInvoices(invoiceId);
        return manualInvoiceResourceAssembler.toManualInvoiceResource(result);
    }

    @RequestMapping(path = "/status/update", method = RequestMethod.PUT)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public Status updateInvoiceQueueStatus(@RequestBody ManualInvoiceDTO manualInvoiceDTO) throws IOException {
        manualInvoiceService.updateManualInvoiceStatus(manualInvoiceDTO);
        return new Status("Ok");
    }

    @RequestMapping(path = "/contractor/{contractorName}", method = RequestMethod.PUT)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public PagedResources<EmployeeEngagementDetailsViewDTO> getContractorsDetailsByEngagement(
            @PathVariable("contractorName") String contractorName,@RequestParam("engagementId") UUID engagementId,@RequestBody EmployeeEngagementDetailsViewDTO employeeEngagementDetailsViewDTO,Pageable pageable,
            PagedResourcesAssembler<EmployeeEngagementDetailsViewDTO> pagedAssembler) {
        Page<EmployeeEngagementDetailsViewDTO> result =
                manualInvoiceService.getContractorsDetailsByEngagement(contractorName,engagementId,employeeEngagementDetailsViewDTO.getContractorIds(),pageable);
        return pagedAssembler.toResource(result, employeeEngagementDetailsViewAssembler);
    }
    
    @RequestMapping(value = "/{invoiceId}", method = RequestMethod.DELETE)
    @RequiredAuthority({ InvoiceConstants.FINANCE_MANAGER,InvoiceConstants.FINANCE_REPRESENTATIVE })
    public Status deleteRejectedManualInvoice(@PathVariable("invoiceId") UUID invoiceId) {
      return manualInvoiceService.deleteRejectedManualInvoice(invoiceId);
      
    }

}
