package com.tm.timesheet.configuration.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.tm.timesheet.configuration.domain.ConfigurationGroup;
import com.tm.timesheet.configuration.domain.HolidayConfiguration;

public interface HolidayConfigurationRepository extends
        CrudRepository<HolidayConfiguration, UUID> {

    List<HolidayConfiguration> findByConfigurationGroup(ConfigurationGroup configurationGroup);
    
}
