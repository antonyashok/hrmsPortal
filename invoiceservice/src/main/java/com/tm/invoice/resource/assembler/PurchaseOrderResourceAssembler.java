package com.tm.invoice.resource.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.inject.Inject;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.invoice.dto.PurchaseOrderDTO;
import com.tm.invoice.web.rest.PurchaseOrderResource;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@Service
public class PurchaseOrderResourceAssembler
        extends ResourceAssemblerSupport<PurchaseOrderDTO, PurchaseOrderDTO> {

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;

    @Inject
    public PurchaseOrderResourceAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(PurchaseOrderResource.class, PurchaseOrderDTO.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
    }

    @Override
    public PurchaseOrderDTO toResource(PurchaseOrderDTO resource) {
        // self link to view configuration settings
        resource.add(linkTo(methodOn(PurchaseOrderResource.class).getAllPODetails(resource.getEngagementId(),StringUtils.EMPTY,StringUtils.EMPTY,null, null)).withSelfRel());
        return resource;
    }



}
