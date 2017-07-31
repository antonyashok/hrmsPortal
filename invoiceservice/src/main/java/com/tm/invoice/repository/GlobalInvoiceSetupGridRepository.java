package com.tm.invoice.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.invoice.domain.GlobalInvoiceSetupGrid;

public interface GlobalInvoiceSetupGridRepository extends
		JpaRepository<GlobalInvoiceSetupGrid, UUID> {

	Page<GlobalInvoiceSetupGrid> findByInvoiceStatus(String status,
			Pageable pageable);

}
