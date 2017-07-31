package com.tm.invoice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.invoice.domain.EmailSettings;

public interface EmailSettingsRepository extends JpaRepository<EmailSettings, UUID> {
	
}
