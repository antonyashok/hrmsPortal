package com.tm.timesheet.configuration.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.timesheet.configuration.domain.EmailSettings;

public interface EmailSettingsRepository extends JpaRepository<EmailSettings, UUID> {
	
}
