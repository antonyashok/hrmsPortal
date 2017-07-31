package com.tm.timesheet.timesheetview.resource.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.timesheet.service.dto.TimesheetTaskDTO;
import com.tm.timesheet.timesheetview.web.rest.SubmitterTimesheetViewResource;



@Service
public class TimesheetTaskViewResourceAssembler extends
		ResourceAssemblerSupport<TimesheetTaskDTO, TimesheetTaskDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	@Autowired
	public TimesheetTaskViewResourceAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(SubmitterTimesheetViewResource.class, TimesheetTaskDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public TimesheetTaskDTO toResource(TimesheetTaskDTO taskResource) {
		try {
            taskResource.add(
            		linkTo(methodOn(SubmitterTimesheetViewResource.class).getTimesheetDetails(taskResource.getTimesheetId()))
            				.withSelfRel());
        } catch (ParseException e) {           
        }
		return taskResource;
	}

}
