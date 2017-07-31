package com.tm.common.engagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.common.engagement.domain.EmailConfiguration;


public interface EmailConfigurationRepository extends JpaRepository<EmailConfiguration, UUID>{

    EmailConfiguration findEmailConfigurationByEmailConfigName(String templateName);
}
