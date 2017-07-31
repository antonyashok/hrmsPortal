package com.tm.invoice.resource.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.invoice.mongo.dto.InvoiceSetupActivitiesLogDTO;
import com.tm.invoice.web.rest.InvoiceSetupResource;

@Service
public class InvoiceSetupActivitiesLogAssembler extends
        ResourceAssemblerSupport<InvoiceSetupActivitiesLogDTO, InvoiceSetupActivitiesLogDTO> {

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;

    @Inject
    public InvoiceSetupActivitiesLogAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(InvoiceSetupResource.class, InvoiceSetupActivitiesLogDTO.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
    }

    public Resources<InvoiceSetupActivitiesLogDTO> toActivitiesLogResource(
            List<InvoiceSetupActivitiesLogDTO> resources, UUID invoiceSetupId) {
        Link link = linkTo(methodOn(InvoiceSetupResource.class).getActivityLog(invoiceSetupId))
                .withSelfRel();
        return new Resources<>(resources, link);
    }

    @Override
    public InvoiceSetupActivitiesLogDTO toResource(
            InvoiceSetupActivitiesLogDTO resource) {
        return resource;
    }
}