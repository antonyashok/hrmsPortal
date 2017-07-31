/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.resource.assemeblers.TimeruleResourceAssembler.java
 * Author        : Annamalai L
 * Date Created  : Mar 10, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.resource.assemeblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.timesheet.configuration.domain.TimeruleConfiguration;
import com.tm.timesheet.configuration.service.mapper.TimeruleConfigurationMapper;
import com.tm.timesheet.configuration.service.resources.TimeruleResource;
import com.tm.timesheet.configuration.web.rest.TimeruleConfigurationResource;

@Service
public class TimeruleResourceAssembler extends ResourceAssemblerSupport<TimeruleConfiguration, TimeruleResource> {

	private static final String UPDATE_TIME_RULE = "updateTimerule";
	protected final RelProvider relProvider;
	protected final EntityLinks entityLinks;

	@Inject
	public TimeruleResourceAssembler(final EntityLinks entityLinks, final RelProvider relProvider) {
		super(TimeruleConfigurationResource.class, TimeruleResource.class);
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;
	}

	@Override
	public TimeruleResource toResource(TimeruleConfiguration entity) {
		TimeruleResource resource = TimeruleConfigurationMapper.INSTANCE
				.timeruleConfigurationTotimeruleResource(entity);
			resource.add(linkTo(
					methodOn(TimeruleConfigurationResource.class).getTimeruleConfiguration(entity.getTimeRuleId()))
							.withSelfRel());

		return resource;
	}

	public TimeruleResource toResources(TimeruleConfiguration entity) {
		TimeruleResource resource = TimeruleConfigurationMapper.INSTANCE
				.timeruleConfigurationTotimeruleResource(entity);
			resource.add(linkTo(
					methodOn(TimeruleConfigurationResource.class).getTimeruleConfiguration(entity.getTimeRuleId()))
							.withRel(UPDATE_TIME_RULE));
			resource.add(linkTo(methodOn(TimeruleConfigurationResource.class)
					.updateTimeruleConfiguration(entity.getTimeRuleId(), entity)).withSelfRel());
		return resource;
	}

	protected final @NotNull TimeruleConfigurationResource self() {
		final Class<TimeruleConfigurationResource> c = TimeruleConfigurationResource.class;
		return methodOn(c);
	}

}

