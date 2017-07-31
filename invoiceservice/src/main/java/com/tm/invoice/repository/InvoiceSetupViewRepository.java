package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.invoice.domain.InvoiceSetupView;

public interface InvoiceSetupViewRepository extends JpaRepository<InvoiceSetupView, UUID> {

    @Query("SELECT invoiceSetupView FROM InvoiceSetupView invoiceSetupView WHERE invoiceSetupView.customerId=:customerId OR invoiceSetupView.invoiceType=:invoiceType")
    List<InvoiceSetupView> getAllExistingActiveSetups(@Param("customerId") Long customerId,
            @Param("invoiceType") String invoiceType);

}
