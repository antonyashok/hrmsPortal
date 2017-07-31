package com.tm.timesheet.timeoff.resource.assemeblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.timesheet.timeoff.service.dto.TimeoffDTO;
import com.tm.timesheet.timeoff.web.rest.TimeoffResource;

@Service
public class TimeoffAssembler extends ResourceAssemblerSupport<TimeoffDTO, TimeoffDTO> {

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	@Autowired
	public TimeoffAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(TimeoffResource.class, TimeoffDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;

	}

	@Override
	public TimeoffDTO toResource(TimeoffDTO resource) {
		return resource;
	}

	public Resources<TimeoffDTO> toHolidayResource(String startDate, String endDate,String engagementId,List<TimeoffDTO> detailDTOs){
		Link link = linkTo(methodOn(TimeoffResource.class).getMyTimeoffHolidays(startDate, endDate,engagementId)).withSelfRel();
		Resources<TimeoffDTO> resources = new Resources<>(detailDTOs, link);
		return resources;
	}
}
