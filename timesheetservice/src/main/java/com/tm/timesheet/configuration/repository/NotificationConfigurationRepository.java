package com.tm.timesheet.configuration.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.tm.timesheet.configuration.domain.ConfigurationGroup;
import com.tm.timesheet.configuration.domain.NotificationConfiguration;

public interface NotificationConfigurationRepository extends
        CrudRepository<NotificationConfiguration, UUID> {
  
  List<NotificationConfiguration> findByConfigurationGroup(ConfigurationGroup configurationGroup);

}
