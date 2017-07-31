package com.tm.invoice.resource.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.inject.Inject;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.invoice.dto.EmployeeEngagementDetailsViewDTO;
import com.tm.invoice.web.rest.ManualInvoiceResource;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@Service
public class EmployeeEngagementDetailsViewAssembler extends
        ResourceAssemblerSupport<EmployeeEngagementDetailsViewDTO, EmployeeEngagementDetailsViewDTO> {

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;

    @Inject
    public EmployeeEngagementDetailsViewAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(ManualInvoiceResource.class, EmployeeEngagementDetailsViewDTO.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
    }

    @Override
    public EmployeeEngagementDetailsViewDTO toResource(EmployeeEngagementDetailsViewDTO resource) {
        resource.add(
                linkTo(methodOn(ManualInvoiceResource.class).getContractorsDetailsByEngagement(StringUtils.EMPTY,resource.getEngagementId(),resource,null, null))
                        .withSelfRel());
        return resource;
    }
}
