package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.invoice.domain.InvoiceSetupBillCycle;

public interface InvoiceSetupBillCycleRepository
        extends JpaRepository<InvoiceSetupBillCycle, Long> {

    InvoiceSetupBillCycle findByInvoiceSetupId(UUID invoiceSetupId);
    
    @Query("SELECT isbc FROM InvoiceSetupBillCycle AS isbc WHERE "
    		+ "isbc.invoiceSetupId IN (:invoiceSetupIds)")
    List<InvoiceSetupBillCycle> findInvoiceSetupBillCyclesByInvoiceSetupIds(@Param("invoiceSetupIds") List<UUID> invoiceSetupIds);

}
