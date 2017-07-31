package com.tm.invoice.resource.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.inject.Inject;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.invoice.dto.InvoiceSetupDTO;
import com.tm.invoice.web.rest.InvoiceSetupResource;

@Service
public class InvoiceSetupResourceAssembler
        extends ResourceAssemblerSupport<InvoiceSetupDTO, InvoiceSetupDTO> {

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;

    @Inject
    public InvoiceSetupResourceAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(InvoiceSetupResource.class, InvoiceSetupDTO.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
    }

    @Override
    public InvoiceSetupDTO toResource(InvoiceSetupDTO resource) {
        // self link to view configuration settings
      /*  resource.add(linkTo(methodOn(InvoiceSetupResource.class).getAllInvoicesetup(
                resource.getBillToOrganizationId(), resource.getStatus(), null, null))
                        .withSelfRel());*/
        return resource;
    }

}
