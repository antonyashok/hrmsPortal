package com.tm.invoice.resource.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.inject.Inject;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.invoice.mongo.dto.ManualInvoiceDTO;
import com.tm.invoice.web.rest.ManualInvoiceResource;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@Service
public class ManualInvoiceResourceAssembler
        extends ResourceAssemblerSupport<ManualInvoiceDTO, ManualInvoiceDTO> {

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;

    @Inject
    public ManualInvoiceResourceAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(ManualInvoiceResource.class, ManualInvoiceDTO.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
    }

    @Override
    public ManualInvoiceDTO toResource(ManualInvoiceDTO resource) {
        resource.add(
                linkTo(methodOn(ManualInvoiceResource.class).getAllManualInvoice(StringUtils.EMPTY, null, null))
                        .withSelfRel());
        return resource;
    }

    public ManualInvoiceDTO toManualInvoiceResource(ManualInvoiceDTO resource) {
        resource.add(
                linkTo(methodOn(ManualInvoiceResource.class).getAllManualInvoice(StringUtils.EMPTY, null, null))
                        .withSelfRel());
        return resource;
    }
}
