package com.tm.common.engagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.common.engagement.domain.EmailSettings;


public interface EmailSettingsRepository extends JpaRepository<EmailSettings, UUID> {
	
}
