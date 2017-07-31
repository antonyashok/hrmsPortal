package com.tm.timesheet.configuration.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.timesheet.configuration.domain.EmailConfiguration;

public interface EmailConfigurationRepository extends JpaRepository<EmailConfiguration, UUID>{

    EmailConfiguration findEmailConfigurationByEmailConfigName(String templateName);
}
