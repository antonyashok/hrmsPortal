package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.invoice.domain.InvoiceSetupOption;

public interface InvoiceSetupOptionRepository extends JpaRepository<InvoiceSetupOption, Long> {

    @Query("select options from InvoiceSetupOption options where options.invoiceSetup.invoiceSetupId=:invoiceSetupId ")
    List<InvoiceSetupOption> findByInvoiceSetupId(@Param("invoiceSetupId") UUID invoiceSetupId);

}
