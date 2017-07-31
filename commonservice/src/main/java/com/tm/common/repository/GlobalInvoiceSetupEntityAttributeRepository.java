package com.tm.common.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.common.domain.GlobalInvoiceSetupEntityAttribute;

public interface GlobalInvoiceSetupEntityAttributeRepository
    extends JpaRepository<GlobalInvoiceSetupEntityAttribute, UUID> {

}
