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

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.timesheet.configuration.service.dto.ConfigurationGroupViewDTO;
import com.tm.timesheet.configuration.web.rest.ConfigurationGroupResource;

@Service
public class ConfigurationGroupViewResourceAssembler
        extends ResourceAssemblerSupport<ConfigurationGroupViewDTO, ConfigurationGroupViewDTO> {

    private static final String Y = "Y";
	private static final String DELETE_CONFIGURATION_GROUP = "deleteConfigurationGroup";
    private static final String VIEW_NOTIFICATION_CONFIG = "viewnotificationConfig";
    private static final String VIEW_HOLIDAY_CONFIG = "viewholidayConfig";

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;
    protected final HolidayConfigurationResourceAssembler holidayResourceAssembler;

    @Autowired
    public ConfigurationGroupViewResourceAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider,
            HolidayConfigurationResourceAssembler holidayResourceAssembler) {
        super(ConfigurationGroupResource.class, ConfigurationGroupViewDTO.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
        this.holidayResourceAssembler = holidayResourceAssembler;
    }

    @Override
    public ConfigurationGroupViewDTO toResource(ConfigurationGroupViewDTO resource) {

        // self link to view configuration settings
        resource.add(linkTo(methodOn(ConfigurationGroupResource.class)
                .getConfigurationGroup(resource.getConfigurationGroupId())).withSelfRel());

        // link to view the Notification configuration using config grp id
        if (Objects.nonNull(resource.getNotifyFlag())
                && resource.getNotifyFlag().equalsIgnoreCase(ConfigurationGroupViewResourceAssembler.Y)) {
            resource.add(linkTo(methodOn(ConfigurationGroupResource.class)
                    .getNotificationConfiguration(resource.getConfigurationGroupId()))
                            .withRel(VIEW_NOTIFICATION_CONFIG));
        }

        // link to view the Holiday configuration using config grp id
        if (Objects.nonNull(resource.getHolidayFlag())
                && resource.getHolidayFlag().equalsIgnoreCase(ConfigurationGroupViewResourceAssembler.Y)) {
            resource.add(linkTo(methodOn(ConfigurationGroupResource.class)
                    .getHolidayConfigurations(resource.getConfigurationGroupId()))
                            .withRel(VIEW_HOLIDAY_CONFIG));
        }

        // link to delete configuration group using config grp id
        resource.add(linkTo(methodOn(ConfigurationGroupResource.class)
                .deleteConfigurationGroup(resource.getConfigurationGroupId()))
                        .withRel(DELETE_CONFIGURATION_GROUP));

        return resource;
    }
}
