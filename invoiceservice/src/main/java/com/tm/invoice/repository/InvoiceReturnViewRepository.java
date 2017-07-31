package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.invoice.domain.InvoiceReturnView;

public interface InvoiceReturnViewRepository extends
		JpaRepository<InvoiceReturnView, Long> {

	@Query("SELECT invoicesetup FROM InvoiceReturnView as invoicesetup WHERE invoicesetup.invoiceSetupId=:invoiceSetupId")
	List<InvoiceReturnView> findByInvoiceSetupId(
			@Param("invoiceSetupId") UUID invoiceSetupId);

}
