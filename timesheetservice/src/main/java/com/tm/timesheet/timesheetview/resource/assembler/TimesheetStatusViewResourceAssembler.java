/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.timesheetview.resource.assembler.TimesheetStatusViewResourceAssembler.java
 * Author        : techmango.net
 * Date Created  : Feb 16, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.timesheetview.resource.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.timesheet.service.dto.TimesheetStatusCountSummary;
import com.tm.timesheet.timesheetview.constants.TimesheetViewConstants;
import com.tm.timesheet.timesheetview.web.rest.ApproverTimesheetViewResource;
import com.tm.timesheet.timesheetview.web.rest.PayrollTimesheetViewResource;
import com.tm.timesheet.timesheetview.web.rest.SubmitterTimesheetViewProxyResource;
import com.tm.timesheet.timesheetview.web.rest.SubmitterTimesheetViewResource;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@Service
public class TimesheetStatusViewResourceAssembler
        extends ResourceAssemblerSupport<TimesheetStatusCountSummary, TimesheetStatusCountSummary> {

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;

    @Autowired
    public TimesheetStatusViewResourceAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(SubmitterTimesheetViewResource.class, TimesheetStatusCountSummary.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
    }

    @Override
    public TimesheetStatusCountSummary toResource(TimesheetStatusCountSummary resource) {
        return resource;
    }

    public TimesheetStatusCountSummary toSubmitterStatusCountResource(
            TimesheetStatusCountSummary resource) throws ParseException {
        resource.add(linkTo(methodOn(SubmitterTimesheetViewResource.class)
                .getStatusCount(resource.getStartDate(), resource.getEndDate() , StringUtils.EMPTY)).withSelfRel());
        resource.add(linkTo(methodOn(SubmitterTimesheetViewResource.class).getAllMyTimesheets(null,
                null, resource.getStartDate(), resource.getEndDate(), StringUtils.EMPTY,
                TimesheetViewConstants.NOT_SUBMITTED)).withSelfRel());
        return resource;
    }
    
    public TimesheetStatusCountSummary toApproverStatusCountResource(
            TimesheetStatusCountSummary resource) throws ParseException {
        resource.add(linkTo(methodOn(SubmitterTimesheetViewProxyResource.class)
                .getStatusCount(resource.getStartDate(), resource.getEndDate() , StringUtils.EMPTY,StringUtils.EMPTY)).withSelfRel());
        return resource;
    }

    public TimesheetStatusCountSummary toManagerStatusCountResource(
            TimesheetStatusCountSummary resource, String employeeType ,String searchParam) throws ParseException {
        resource.add(linkTo(methodOn(ApproverTimesheetViewResource.class)
                .getStatusCount(resource.getStartDate(), resource.getEndDate(), employeeType,searchParam))
                        .withSelfRel());
        resource.add(linkTo(methodOn(SubmitterTimesheetViewResource.class).getAllMyTimesheets(null,
                null, resource.getStartDate(), resource.getEndDate(), StringUtils.EMPTY,
                TimesheetViewConstants.NOT_SUBMITTED)).withSelfRel());
        return resource;
    }
    
    public TimesheetStatusCountSummary toRecruiterStatusCountResource(
            TimesheetStatusCountSummary resource, String employeeType ,String searchParam) throws ParseException {
        resource.add(linkTo(methodOn(ApproverTimesheetViewResource.class)
                .getTimesheetStatusCountForRecruiter(resource.getStartDate(), resource.getEndDate(), employeeType,searchParam))
                        .withSelfRel());
        resource.add(linkTo(methodOn(SubmitterTimesheetViewResource.class).getAllMyTimesheets(null,
                null, resource.getStartDate(), resource.getEndDate(), StringUtils.EMPTY,
                TimesheetViewConstants.NOT_SUBMITTED)).withSelfRel());
        return resource;
    }

	public TimesheetStatusCountSummary toPayrollStatusCountResource(TimesheetStatusCountSummary resource,
			String employeeType, String searchParam) throws ParseException {
		resource.add(linkTo(methodOn(ApproverTimesheetViewResource.class).getTimesheetStatusCountForRecruiter(
				resource.getStartDate(), resource.getEndDate(), employeeType, searchParam)).withSelfRel());
		resource.add(linkTo(methodOn(PayrollTimesheetViewResource.class).getStatusCount(resource.getStartDate(),
				resource.getEndDate(), TimesheetViewConstants.VERIFIED,StringUtils.EMPTY)).withSelfRel());
		return resource;
	}
}
