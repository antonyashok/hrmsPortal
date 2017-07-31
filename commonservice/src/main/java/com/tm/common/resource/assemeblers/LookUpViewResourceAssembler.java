package com.tm.common.resource.assemeblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.common.domain.LookUpView;
import com.tm.common.service.dto.LookUpViewDTO;
import com.tm.common.web.rest.LookUpResource;

@Service
public class LookUpViewResourceAssembler
        extends ResourceAssemblerSupport<LookUpView, LookUpViewDTO> {

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;

    public LookUpViewResourceAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(LookUpResource.class, LookUpViewDTO.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
    }

    public List<LookUpViewDTO> toLookUpResource(List<LookUpViewDTO> list) {

        list.forEach(lookUp -> lookUp.add(
                linkTo(methodOn(LookUpResource.class).getLookupValues(lookUp.getAttributeName()))
                        .withSelfRel()));
        return list;
    }

    @Override
    public LookUpViewDTO toResource(LookUpView lookUp) {
     return new LookUpViewDTO();
    }

}
