package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tm.invoice.domain.GlobalInvoiceSetup;
import com.tm.invoice.enums.GlobalInvoiceFlag;

@Repository
public interface GlobalInvoiceRepository extends JpaRepository<GlobalInvoiceSetup, UUID> {

    GlobalInvoiceSetup findByInvoiceSetupId(UUID invoiceSetupId);
    
    @Modifying
    @Transactional(transactionManager="invoiceTransactionManager")
    @Query("UPDATE GlobalInvoiceSetup i SET i.activeFlag = :activeflag WHERE i.invoiceSetupId IN (:invoicesetupids)")
    Integer updateByActiveFlag(@Param("activeflag") GlobalInvoiceFlag activeFlag , @Param("invoicesetupids") List<UUID> invoiceSetupIds);
  
}
