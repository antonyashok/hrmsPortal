package com.tm.timesheet.configuration.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.timesheet.configuration.service.dto.ConfigurationGroupDTO;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupViewDTO;
import com.tm.timesheet.configuration.service.dto.HolidayConfigurationDTO;
import com.tm.timesheet.configuration.service.dto.NotificationAttributeDTO;
import com.tm.timesheet.configuration.service.dto.OfficeLocationDTO;
import com.tm.timesheet.configuration.service.dto.UserGroupDTO;

/**
 * Service Interface for managing ConfigurationGroup.
 */
public interface ConfigurationGroupService {

    ConfigurationGroupDTO createConfigurationGroup(ConfigurationGroupDTO configurationGroupDTO);

    ConfigurationGroupDTO updateConfigurationGroup(ConfigurationGroupDTO configurationGroupDTO);

    Page<ConfigurationGroupViewDTO> getAllConfigurationGroupsView(Pageable pageable);

    ConfigurationGroupDTO getConfigurationGroup(UUID id);

    void deleteConfigurationGroup(UUID id);

    void deleteHolidayConfiguration(UUID id);

    void deleteNotificationConfiguration(UUID id);

    List<HolidayConfigurationDTO> createHolidayConfiguration(UUID configGroupId,
            List<HolidayConfigurationDTO> holidayConfigurationDTOs);

    List<HolidayConfigurationDTO> updateHolidayConfiguration(UUID configGroupId,
            List<HolidayConfigurationDTO> holidayConfigurationDTOs);

    List<HolidayConfigurationDTO> getConfiguredHolidayConfigurations(UUID id);

    HolidayConfigurationDTO getHolidayConfiguration(UUID id);

    List<NotificationAttributeDTO> getConfiguredNotificationConfigurations(UUID id);

    List<NotificationAttributeDTO> getNotificationAttributes(String userGroupCategory);

    List<NotificationAttributeDTO> createNotification(UUID id,
            List<NotificationAttributeDTO> notificationAttribute);

    List<NotificationAttributeDTO> updateNotification(UUID id,
            List<NotificationAttributeDTO> notificationAttribute);

    List<OfficeLocationDTO> getConfiguredOfficeIdsByConfigIdOrUserGroupIds(
        UUID configurationGroupId, String userGroupIds);

    List<OfficeLocationDTO> getOfficeLocationsByConfigurationGroup(UUID id);

    List<UserGroupDTO> getUserGroupByConfigurationGroup(UUID id) throws Exception;
    
    Boolean createHolidayConfigurationValidation(
            List<HolidayConfigurationDTO> holidayConfigurationDTOs);

    Boolean configurationGroupValidation(ConfigurationGroupDTO groupDTO);
}
