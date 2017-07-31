/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.resource.assembler.TimesheetResourceAssembler.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.resource.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.inject.Inject;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.timesheet.service.dto.TimesheetDTO;
import com.tm.timesheet.web.rest.SubmitterTimesheetResource;

@Service
public class TimesheetResourceAssembler
        extends ResourceAssemblerSupport<TimesheetDTO, TimesheetDTO> {

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;

    @Inject
    public TimesheetResourceAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(SubmitterTimesheetResource.class, TimesheetDTO.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
    }

    @Override
    public TimesheetDTO toResource(TimesheetDTO resource) {
        resource.add(linkTo(methodOn(SubmitterTimesheetResource.class)).withSelfRel());
        return resource;
    }
}
