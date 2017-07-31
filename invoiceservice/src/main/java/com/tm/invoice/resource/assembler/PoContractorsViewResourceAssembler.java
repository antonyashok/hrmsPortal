package com.tm.invoice.resource.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.inject.Inject;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.invoice.dto.PoContractorsViewDTO;
import com.tm.invoice.web.rest.PurchaseOrderResource;

@Service
public class PoContractorsViewResourceAssembler  extends ResourceAssemblerSupport<PoContractorsViewDTO,PoContractorsViewDTO> {

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;

    @Inject
    public PoContractorsViewResourceAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(PurchaseOrderResource.class, PoContractorsViewDTO.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
    }

    @Override
    public PoContractorsViewDTO toResource(PoContractorsViewDTO resource) {
        // self link to view configuration settings
     /*   resource.add(linkTo(methodOn(PurchaseOrderResource.class)
                .getAllContractorDetailsByPurchaseOrderId(resource.getPurchaseOrderId(),null,null)).withSelfRel());*/
        return resource;
    }

    
}
