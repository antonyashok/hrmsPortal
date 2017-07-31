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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.timesheet.configuration.service.dto.ConfigurationGroupDTO;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupResourceDTO;
import com.tm.timesheet.configuration.service.dto.HolidayConfigurationDTO;
import com.tm.timesheet.configuration.service.dto.NotificationAttributeDTO;
import com.tm.timesheet.configuration.service.mapper.ConfigurationGroupMapper;
import com.tm.timesheet.configuration.web.rest.ConfigurationGroupResource;

@Service
public class ConfigurationGroupResourceAssembler extends
		ResourceAssemblerSupport<ConfigurationGroupDTO, ConfigurationGroupDTO> {

	private static final String DELETE_CONFIGURATION_GROUP = "deleteConfigurationGroup";
	private static final String VIEW_NOTIFICATION_CONFIG = "viewnotificationConfig";
	private static final String VIEW_HOLIDAY_CONFIG = "viewholidayConfig";

	protected final HolidayConfigurationResourceAssembler holidayResourceAssembler;

	@Inject
	public ConfigurationGroupResourceAssembler(
			HolidayConfigurationResourceAssembler holidayResourceAssembler) {
		super(ConfigurationGroupResource.class, ConfigurationGroupDTO.class);
		this.holidayResourceAssembler = holidayResourceAssembler;
	}

	@Override
	public ConfigurationGroupDTO toResource(ConfigurationGroupDTO resource) {

		// self link to view configuration settings
		resource.add(linkTo(
				self().getConfigurationGroup(resource.getConfigurationGroupId()))
				.withSelfRel());

		// link to view the Notification configuration using config grp id and
		// jobTitle
			resource.add(linkTo(
					self().getNotificationConfiguration(
							resource.getConfigurationGroupId())).withRel(
					VIEW_NOTIFICATION_CONFIG));


		// link to view the Notification configuration using config grp id and
		// jobTitle
			resource.add(linkTo(
					self().getHolidayConfigurations(
							resource.getConfigurationGroupId())).withRel(
					VIEW_HOLIDAY_CONFIG));

		// link to delete configuration group using config grp id
			resource.add(linkTo(
					self().deleteConfigurationGroup(
							resource.getConfigurationGroupId())).withRel(
					DELETE_CONFIGURATION_GROUP));
		return resource;
	}

	public ConfigurationGroupResourceDTO toConfigResource(
			ConfigurationGroupDTO resource) {

		// pre build to embedded resource
		ConfigurationGroupResourceDTO configurationGroupResource = ConfigurationGroupMapper.INSTANCE
				.configurationGroupDTOToconfigurationGroupResourceMap(resource);

		// build embedded resource
		configurationGroupResource
				.setEmbeddedResource(toEmbeddableResource(resource));

		// self link to view configuration settings
		configurationGroupResource.add(linkTo(
				methodOn(ConfigurationGroupResource.class)
						.getConfigurationGroup(
								configurationGroupResource
										.getConfigurationGroupId()))
				.withSelfRel());

		// link to view the Notification configuration using config grp id and
		// jobTitle
			configurationGroupResource.add(linkTo(
					self().getNotificationConfiguration(
							resource.getConfigurationGroupId())).withRel(
					VIEW_NOTIFICATION_CONFIG));

		// link to view the Notification configuration using config grp id and
		// jobTitle
			resource.add(linkTo(
					methodOn(ConfigurationGroupResource.class)
							.getHolidayConfigurations(
									resource.getConfigurationGroupId()))
					.withRel(VIEW_HOLIDAY_CONFIG));

		// link to delete configuration group using config grp id
			configurationGroupResource.add(linkTo(
					self().deleteConfigurationGroup(
							resource.getConfigurationGroupId())).withRel(
					DELETE_CONFIGURATION_GROUP));
		return configurationGroupResource;
	}

	public List<EmbeddedWrapper> toEmbeddableHolidayList(
			final EmbeddedWrappers wrapper, List<EmbeddedWrapper> embeddeds,
			List<HolidayConfigurationDTO> holidayResources) {

		holidayResources.forEach(holidayResource -> embeddeds.add(wrapper
				.wrap(holidayResourceAssembler.toResource(holidayResource))));

		return embeddeds;
	}

	public List<EmbeddedWrapper> toEmbeddableNotification(
			final EmbeddedWrappers wrapper, List<EmbeddedWrapper> embeddeds,
			List<NotificationAttributeDTO> notificationAttributeResources) {

		notificationAttributeResources
				.forEach(notificationAttribute -> embeddeds.add(wrapper
						.wrap(new Resource<NotificationAttributeDTO>(
								notificationAttribute, new Link[0]))));
		return embeddeds;
	}

	// Build embedded resource
	public Resources<EmbeddedWrapper> toEmbeddableResource(
			ConfigurationGroupDTO resource) {
		final EmbeddedWrappers wrapper = new EmbeddedWrappers(true);
		List<EmbeddedWrapper> embeddeds = new ArrayList<>();
		// Holiday configuration
		if (!CollectionUtils.isEmpty(resource.getHolidayConfiguration())) {

			embeddeds = toEmbeddableHolidayList(wrapper, embeddeds,
					resource.getHolidayConfiguration());
		}
		// Notification Configuration
		if (CollectionUtils.isNotEmpty(resource.getNotificationAttribute())) {
			embeddeds = toEmbeddableNotification(wrapper, embeddeds,
					resource.getNotificationAttribute());
		}
		return new Resources<>(embeddeds, new Link[0]);
	}

	protected final @NotNull ConfigurationGroupResource self() {
		final Class<ConfigurationGroupResource> c = ConfigurationGroupResource.class;
		return methodOn(c);
	}

}
