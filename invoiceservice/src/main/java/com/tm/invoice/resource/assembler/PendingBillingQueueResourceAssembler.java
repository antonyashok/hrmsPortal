package com.tm.invoice.resource.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.invoice.dto.BillingQueueDTO;
import com.tm.invoice.web.rest.PendingBillingQueueResource;

@Service
public class PendingBillingQueueResourceAssembler
        extends ResourceAssemblerSupport<BillingQueueDTO, BillingQueueDTO> {

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;

    @Inject
    public PendingBillingQueueResourceAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(PendingBillingQueueResource.class, BillingQueueDTO.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
    }

    @Override
    public BillingQueueDTO toResource(BillingQueueDTO resource) {
        // self link to view configuration settings
        resource.add(linkTo(methodOn(PendingBillingQueueResource.class)
                .getPendingBillingQueueList(UUID.randomUUID(),"Active", null, null))
                        .withSelfRel());
        return resource;
    }

    public BillingQueueDTO toPendingBillingResource(BillingQueueDTO resource) {
        resource.add(linkTo(methodOn(PendingBillingQueueResource.class)
                .getPendingBillingQueueDetail(resource.getBillingQueueId())).withSelfRel());
        return resource;
    }

    public BillingQueueDTO toPendingBillingDetailsResource(BillingQueueDTO resource) {
    	resource.add(
                linkTo(methodOn(PendingBillingQueueResource.class).updateBillingQueue(resource))
                        .withSelfRel());
        return resource;
    }

}
