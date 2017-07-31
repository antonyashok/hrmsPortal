package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.invoice.domain.PreBillView;

public interface InvoicePreBillRepository extends JpaRepository<PreBillView, UUID>  {
	
	@Query("SELECT preBillView FROM PreBillView AS preBillView WHERE "
    		+ "preBillView.invoiceSetupId IN (:invoiceSetupIds)")
    List<PreBillView> getInvoiceSetupsByIds(@Param("invoiceSetupIds") List<UUID> invoiceSetupIds);
}
