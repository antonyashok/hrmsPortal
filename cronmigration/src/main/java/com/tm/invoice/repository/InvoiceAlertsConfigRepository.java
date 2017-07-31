package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tm.invoice.domain.InvoiceAlertsConfig;

@Repository
public interface InvoiceAlertsConfigRepository extends
		JpaRepository<InvoiceAlertsConfig, UUID> {

	List<InvoiceAlertsConfig> findInvoiceAlertsConfigsByAlertId(UUID alertId);

}
