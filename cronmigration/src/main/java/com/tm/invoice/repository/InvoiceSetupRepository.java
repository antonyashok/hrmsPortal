package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tm.invoice.domain.InvoiceSetup;
import com.tm.invoice.domain.InvoiceSetup.ActiveFlag;

@Repository
public interface InvoiceSetupRepository extends JpaRepository<InvoiceSetup, UUID> {

    InvoiceSetup findByInvoiceSetupId(UUID invoiceSetupId);

    @Query("SELECT invoiceSetup FROM InvoiceSetup AS invoiceSetup WHERE invoiceSetup.invoiceSetupId IN (:invoiceSetupIds)")
    List<InvoiceSetup> getInvoiceSetupsByIds(@Param("invoiceSetupIds") List<UUID> invoiceSetupIds);
    
    @Modifying
    @Transactional(transactionManager="invoiceTransactionManager")
    @Query("UPDATE InvoiceSetup inv SET inv.activeFlag =:activeflag WHERE inv.invoiceSetupId IN (:invoicesetupids)")
    Integer updateByActiveFlag(@Param("activeflag") ActiveFlag activeFlag , @Param("invoicesetupids") List<UUID> invoiceSetupIds);

    List<InvoiceSetup> findByStatus(String status);
    
//    InvoiceSetup findOneByPrefix(@Param("activeflag") ActiveFlag activeFlag);
}
