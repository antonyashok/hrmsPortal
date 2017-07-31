package com.tm.invoice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.invoice.domain.EmailConfiguration;

public interface EmailConfigurationRepository extends JpaRepository<EmailConfiguration, UUID>{

    EmailConfiguration findEmailConfigurationByEmailConfigName(String templateName);
}
