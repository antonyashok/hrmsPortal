/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.resource.assembler.TimesheetStatusResourceAssembler.java
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.timesheet.service.dto.TimesheetStatusCountSummary;
import com.tm.timesheet.web.rest.SubmitterTimesheetResource;

@Service
public class TimesheetStatusResourceAssembler
        extends ResourceAssemblerSupport<TimesheetStatusCountSummary, TimesheetStatusCountSummary> {

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;

    @Autowired
    public TimesheetStatusResourceAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(SubmitterTimesheetResource.class, TimesheetStatusCountSummary.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
    }

    @Override
    public TimesheetStatusCountSummary toResource(TimesheetStatusCountSummary resource) {
        return resource;
    }

}
