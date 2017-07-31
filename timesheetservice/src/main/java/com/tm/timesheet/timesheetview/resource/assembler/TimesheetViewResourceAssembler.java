package com.tm.timesheet.timesheetview.resource.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.timesheet.service.dto.TimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetResourceDTO;
import com.tm.timesheet.service.dto.TimesheetTaskDTO;
import com.tm.timesheet.timesheetview.constants.TimesheetViewConstants;
import com.tm.timesheet.timesheetview.service.mapper.TimesheetViewMapper;
import com.tm.timesheet.timesheetview.web.rest.SubmitterTimesheetViewResource;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@Service
public class TimesheetViewResourceAssembler extends ResourceAssemblerSupport<TimesheetDTO, TimesheetDTO> {

	private static final Logger log = LoggerFactory.getLogger(TimesheetViewResourceAssembler.class);

	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;
	protected final TimesheetTaskViewResourceAssembler taskResourceAssembler;

	@Inject
	public TimesheetViewResourceAssembler(final EntityLinks entityLinks, final RelProvider relProvider,
			TimesheetTaskViewResourceAssembler taskResourceAssembler) {
		super(SubmitterTimesheetViewResource.class, TimesheetDTO.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
		this.taskResourceAssembler = taskResourceAssembler;
	}

	@Override
	public TimesheetDTO toResource(TimesheetDTO resource) {
		try {
			resource.add(
					linkTo(methodOn(SubmitterTimesheetViewResource.class).getAllMyTimesheets(null, null, StringUtils.EMPTY,
							StringUtils.EMPTY, StringUtils.EMPTY, TimesheetViewConstants.NOT_SUBMITTED)).withSelfRel());
		} catch (ParseException e) {
			log.error("Exception Occured", e);
		}
		return resource;
	}

	public TimesheetResourceDTO toTimesheetDetailsResource(TimesheetDTO resource) {
		TimesheetResourceDTO timesheetResourceDTO = TimesheetViewMapper.INSTANCE
				.timesheetResourceDTOToTimesheetResourceMap(resource);

		timesheetResourceDTO.setEmbeddedResource(toEmbeddableResource(resource));
		return timesheetResourceDTO;
	}

	public Resources<EmbeddedWrapper> toEmbeddableResource(TimesheetDTO resource) {
		final EmbeddedWrappers wrapper = new EmbeddedWrappers(true); // Prefer
																		// for
																		// collection
		List<EmbeddedWrapper> embeddeds = new ArrayList<EmbeddedWrapper>();

		embeddeds = toEmbeddableTaskDetailList(wrapper, embeddeds, resource.getTaskDetails());

		return new Resources<EmbeddedWrapper>(embeddeds, new Link[0]);
	}

	public List<EmbeddedWrapper> toEmbeddableTaskDetailList(final EmbeddedWrappers wrapper,
			List<EmbeddedWrapper> embeddeds, List<TimesheetTaskDTO> timesheetTaskConfiguration) {

		timesheetTaskConfiguration.forEach(
				timesheetResource -> embeddeds.add(wrapper.wrap(taskResourceAssembler.toResource(timesheetResource))));

		return embeddeds;
	}

	public List<EmbeddedWrapper> toEmbeddableTimesheetTask(final EmbeddedWrappers wrapper,
			List<EmbeddedWrapper> embeddeds, List<TimesheetTaskDTO> timesheetTaskResources) {

		timesheetTaskResources.forEach(
				taskDetails -> embeddeds.add(wrapper.wrap(new Resource<TimesheetTaskDTO>(taskDetails, new Link[0]))));
		return embeddeds;

	}
}
