package com.tm.timesheet.configuration.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.tm.timesheet.configuration.domain.ConfigurationGroup;
import com.tm.timesheet.configuration.domain.ConfigurationGroupLocation;
import com.tm.timesheet.configuration.domain.ConfigurationGroupUserJobTitle;
import com.tm.timesheet.configuration.domain.ConfigurationGroupView;
import com.tm.timesheet.configuration.domain.HolidayConfiguration;
import com.tm.timesheet.configuration.domain.NotificationAttribute;
import com.tm.timesheet.configuration.domain.NotificationConfiguration;
import com.tm.timesheet.configuration.domain.TimesheetConfiguration;
import com.tm.timesheet.configuration.domain.TimesheetHourConfiguration;
import com.tm.timesheet.configuration.domain.TimesheetTimeConfiguration;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupDTO;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupLocationDTO;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupResourceDTO;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupUserJobTitleDTO;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupViewDTO;
import com.tm.timesheet.configuration.service.dto.HolidayConfigurationDTO;
import com.tm.timesheet.configuration.service.dto.NotificationAttributeDTO;
import com.tm.timesheet.configuration.service.dto.NotificationConfigurationDTO;



/**
 * Mapper for the entity TimesheetConfigurationDTO and its DTO 1. ConfigurationGroup 2.
 * ConfigurationGroupLocation 3. HolidayConfigurationRepository 4. NotificationConfiguration 5.
 * TimesheetHourConfiguration 6. TimesheetTimeConfiguration 7. TimesheetConfiguration
 */
@Mapper
public interface ConfigurationGroupMapper {
    ConfigurationGroupMapper INSTANCE = Mappers.getMapper(ConfigurationGroupMapper.class);

    // --One DTO convert each entity object using MapStruct.

    ConfigurationGroup configurationDTOToConfigurationGroup(
            ConfigurationGroupDTO configurationGroupDTO);

    List<ConfigurationGroupLocation> configurationGroupLocationDTOsToConfigurationGroupLocations(
            List<ConfigurationGroupLocationDTO> configurationGroupLocationDTOs);

    ConfigurationGroupLocation configurationGroupLocationDTOToConfigurationGroupLocation(
            ConfigurationGroupLocationDTO configurationGroupLocationDTO);

    @Mapping(target = "timeCalculation",
            expression = "java(new TimesheetConfiguration().getTimeCalculation().fromString(configurationGroupDTO.getTimeCalculation()))")
    @Mapping(target = "defaultInput",
            expression = "java(new TimesheetConfiguration().getDefaultInput().fromString(configurationGroupDTO.getDefaultInput()))")
    TimesheetConfiguration timesheetConfigurationDTOToTimesheetConfiguration(
            ConfigurationGroupDTO configurationGroupDTO);

    HolidayConfiguration holidayConfigurationDTOToHolidayConfiguration(
            HolidayConfigurationDTO holidayConfigurationDTO);

    List<HolidayConfiguration> holidayConfigurationDTOsToHolidayConfiguration(
            List<HolidayConfigurationDTO> holidayConfigurationDTOs);
    
    NotificationConfiguration notificationConfigurationDTOToNotificationConfiguration(
            NotificationConfigurationDTO notificationConfigurationDTO);

    List<NotificationConfiguration> notificationConfigurationDTOsToNotificationConfigurations(
            List<NotificationConfigurationDTO> notificationConfigurationDTO);

    TimesheetHourConfiguration configurationGroupDTOToTimesheetHourConfiguration(
            ConfigurationGroupDTO configurationGroupDTO);

    TimesheetTimeConfiguration configurationGroupDTOToTimesheetTimeConfiguration(
            ConfigurationGroupDTO configurationGroupDTO);
    
    ConfigurationGroupUserJobTitle configurationGroupUserJobTitleDTOToConfigurationGroupUserJobTitle(
            ConfigurationGroupUserJobTitleDTO configurationGroupUserJobTitle);

    // --Entity object to DTO convert


    ConfigurationGroupLocationDTO configurationGroupLocationToConfigurationGroupLocationDTO(
            ConfigurationGroupLocation configurationGroupLocation);

    HolidayConfigurationDTO holidayConfigurationToHolidayConfigurationDTO(
    		HolidayConfiguration holidayConfiguration);
    
    List<HolidayConfigurationDTO> holidayConfigurationsToHolidayConfigurationDTOs(
            List<HolidayConfiguration> holidayConfiguration);
    
    NotificationAttributeDTO notificationAttributeToNotificationAttributeDTO(
           NotificationAttribute notificationAttributes);
    
    List<NotificationAttributeDTO> notificationAttributesToNotificationAttributeDTOs(
            List<NotificationAttribute> notificationAttributes);
    
    ConfigurationGroupUserJobTitleDTO configurationGroupUserJobTitleToConfigurationGroupUserJobTitleDTO(
            ConfigurationGroupUserJobTitle configurationGroupUserJobTitle);
    
    @Mapping(target = "notificationConfiguration", ignore = true)
    @Mapping(target = "timeCalculation",
            expression = "java((timesheetConfiguration.getTimeCalculation().timeCalutaion()))")
    @Mapping(target = "defaultInput",
            expression = "java((timesheetConfiguration.getDefaultInput().workInput()))")
    ConfigurationGroupDTO configurationGroupToConfigurationGroupDTO(
            ConfigurationGroup configurationGroup,
            List<ConfigurationGroupLocation> configurationGroupLocation,
            List<ConfigurationGroupUserJobTitle> configurationGroupUserJobTitle,
            TimesheetConfiguration timesheetConfiguration,
            List<HolidayConfiguration> holidayConfigurations, TimesheetHourConfiguration workingHour,
            TimesheetTimeConfiguration workingTime);
    
    
    ConfigurationGroupResourceDTO configurationGroupDTOToconfigurationGroupResourceMap(ConfigurationGroupDTO configurationGroup);
    
    @Mapping(target = "timeCalculation",
            expression = "java((configurationGroupView.getTimeCalculation().timeCalutaion()))")
    @Mapping(target = "defaultInput",
            expression = "java((configurationGroupView.getDefaultInput().workInput()))")
    ConfigurationGroupViewDTO configurationGroupViewToConfigurationGroupGroupViewDTO(
            ConfigurationGroupView configurationGroupView);
}
