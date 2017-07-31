package com.tm.invoice.web.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.dto.BillingQueueDTO;
import com.tm.invoice.dto.TaskDTO;
import com.tm.invoice.resource.assembler.PendingBillingQueueResourceAssembler;
import com.tm.invoice.service.PendingBillingQueueService;

@RestController
public class PendingBillingQueueResource {

    private PendingBillingQueueService pendingBillingQueueService;
    private PendingBillingQueueResourceAssembler pendingBillingQueueResourceAssembler;

    @Inject
    public PendingBillingQueueResource(PendingBillingQueueService pendingBillingQueueService,
            PendingBillingQueueResourceAssembler pendingBillingQueueResourceAssembler) {
        this.pendingBillingQueueService = pendingBillingQueueService;
        this.pendingBillingQueueResourceAssembler = pendingBillingQueueResourceAssembler;
    }

    @RequestMapping(value = "/billingQueue", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE,InvoiceConstants.PROFILE_VIEW })
    public PagedResources<BillingQueueDTO> getPendingBillingQueueList(
            @RequestParam UUID engagementId,@RequestParam String status, Pageable pageable,
            PagedResourcesAssembler<BillingQueueDTO> pagedAssembler) {
        Page<BillingQueueDTO> result =
                pendingBillingQueueService.getPendingBillingQueueList(engagementId,status, pageable);
        return pagedAssembler.toResource(result, pendingBillingQueueResourceAssembler);
    }
    
    @RequestMapping(value = "/billingQueue/employee/{employeeId}", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE,InvoiceConstants.PROFILE_VIEW })
    public PagedResources<BillingQueueDTO> getPendingBillingQueueByEmployeeIdList(
            @PathVariable Long employeeId,@RequestParam String status, Pageable pageable,
            PagedResourcesAssembler<BillingQueueDTO> pagedAssembler) {
        Page<BillingQueueDTO> result =
                pendingBillingQueueService.getPendingBillingQueueList(employeeId,status, pageable);
        return pagedAssembler.toResource(result, pendingBillingQueueResourceAssembler);
    }
    
    @RequestMapping(path = "/billingQueue/{billingQueueId}", method = RequestMethod.GET)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE ,InvoiceConstants.PROFILE_VIEW})
    public BillingQueueDTO getPendingBillingQueueDetail(@PathVariable UUID billingQueueId) {
        BillingQueueDTO contractorDetail = pendingBillingQueueService.getPendingBillingQueueDetail(billingQueueId);
       return  pendingBillingQueueResourceAssembler.toPendingBillingResource(contractorDetail);
    }
    
    @RequestMapping(path = "/billingQueue", method = RequestMethod.POST)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE ,InvoiceConstants.PROFILE_VIEW})
	public BillingQueueDTO createBillingQueue(@RequestBody BillingQueueDTO pendingBillDetailsDTO) {
		BillingQueueDTO pendingBillDetails = pendingBillingQueueService.createBillingDetails(pendingBillDetailsDTO);
		return pendingBillingQueueResourceAssembler.toPendingBillingDetailsResource(pendingBillDetails);
	}
    
    @RequestMapping(path = "/billingQueue", method = RequestMethod.PUT)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE,InvoiceConstants.PROFILE_VIEW })
	public BillingQueueDTO updateBillingQueue(@RequestBody BillingQueueDTO pendingBillDetailsDTO) {
		BillingQueueDTO pendingBillDetails = pendingBillingQueueService.updateBillingDetails(pendingBillDetailsDTO);
		return pendingBillingQueueResourceAssembler.toPendingBillingDetailsResource(pendingBillDetails);
	}
    
	@RequestMapping(path = "/billingQueue/submit", method = RequestMethod.PUT)
	@RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE,InvoiceConstants.PROFILE_VIEW })
	public BillingQueueDTO submitBillingQueue(@RequestBody BillingQueueDTO pendingBillDetailsDTO) {
		BillingQueueDTO pendingBillDetails = pendingBillingQueueService.submitBillingDetails(pendingBillDetailsDTO);
		return pendingBillingQueueResourceAssembler.toPendingBillingDetailsResource(pendingBillDetails);
	}
	
	@RequestMapping(path = "/billingQueueId", method = RequestMethod.GET)
	@RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE,InvoiceConstants.PROFILE_VIEW })
    public Map<String, UUID> getGlobalInvoiceId(){
    	UUID billingQueueId=UUID.randomUUID();
    	Map<String, UUID> mapUUID=new HashMap<>();
    	mapUUID.put(InvoiceConstants.BILLING_QUEUE_ID, billingQueueId);
        return mapUUID;
    }
	
	@RequestMapping(path = "/billingQueue/{status}", method = RequestMethod.PUT)
	@RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
	public BillingQueueDTO approveBillingQueue(@RequestBody BillingQueueDTO pendingBillDetailsDTO) {
		BillingQueueDTO pendingBillDetails = pendingBillingQueueService.activeInactiveBillingDetails(pendingBillDetailsDTO);
		return pendingBillingQueueResourceAssembler.toPendingBillingDetailsResource(pendingBillDetails);
	}
	
	@RequestMapping(path = "/billingQueueTask/{billingQueueId}", method = RequestMethod.PUT)
    @RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE,InvoiceConstants.PROFILE_VIEW })
	public BillingQueueDTO updateTaskBillingQueue(@PathVariable UUID billingQueueId,@RequestBody List<TaskDTO> taskDTOList) {
		BillingQueueDTO pendingBillDetails = pendingBillingQueueService.updateTaskBillingDetails(billingQueueId,taskDTOList);
		return pendingBillingQueueResourceAssembler.toPendingBillingDetailsResource(pendingBillDetails);
	}
	

}
