package com.tm.timesheet.timeoff.resource.assemeblers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.timesheet.timeoff.service.dto.PtoAvailableDTO;
import com.tm.timesheet.timeoff.web.rest.TimeoffResource;


@Service
public class PtoAvailableAssembler extends 
									ResourceAssemblerSupport<PtoAvailableDTO, PtoAvailableDTO> {

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;

    @Autowired
    public PtoAvailableAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(TimeoffResource.class, PtoAvailableDTO.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;

    }

    @Override
    public PtoAvailableDTO toResource(PtoAvailableDTO resource) {

        // self link to view configuration settings
        /*resource.add(linkTo(
                methodOn(TimesheetConfigurationResource.class).getTimeoffList(
                        resource.getPtoRequestId())).withSelfRel());*/

        return resource;
    }
    
}
