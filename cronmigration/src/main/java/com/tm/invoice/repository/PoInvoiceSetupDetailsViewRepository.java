package com.tm.invoice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tm.invoice.domain.PoInvoiceSetupDetailsView;

@Repository
public interface PoInvoiceSetupDetailsViewRepository
        extends JpaRepository<PoInvoiceSetupDetailsView, UUID> {


}
