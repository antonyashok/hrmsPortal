/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.resource.assemeblers.TimeruleResourceAssembler.java
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

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.timesheet.configuration.service.dto.HolidayConfigurationDTO;
import com.tm.timesheet.configuration.web.rest.ConfigurationGroupResource;

@Service
public class HolidayConfigurationResourceAssembler
        extends ResourceAssemblerSupport<HolidayConfigurationDTO, HolidayConfigurationDTO> {

    private final static String DELETE_HOLIDAY_CONFIGURATION = "deleteHolidayConfiguration";


    @Autowired
    public HolidayConfigurationResourceAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(ConfigurationGroupResource.class, HolidayConfigurationDTO.class);
    }

    @Override
    public HolidayConfigurationDTO toResource(HolidayConfigurationDTO holidayResource) {
        
        // link to delete holiday configuration
            holidayResource.add(linkTo(
                    self().deleteHolidayConfiguration(holidayResource.getConfigurationGroupId(),
                            holidayResource.getTimeSheetHolidayId()))
                                    .withRel(DELETE_HOLIDAY_CONFIGURATION));
        
        holidayResource
                .add(linkTo(
                        methodOn(ConfigurationGroupResource.class).getHolidayConfiguration(
                                holidayResource.getConfigurationGroupId(),
                                holidayResource.getTimeSheetHolidayId())).withSelfRel());            
        return holidayResource;
    }

    protected final @NotNull ConfigurationGroupResource self() {
        final Class<ConfigurationGroupResource> configurationGroupResource = ConfigurationGroupResource.class;
        return methodOn(configurationGroupResource);
    }
}