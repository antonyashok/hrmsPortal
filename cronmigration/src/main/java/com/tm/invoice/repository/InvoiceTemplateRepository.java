package com.tm.invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tm.invoice.domain.InvoiceTemplate;

@Repository
public interface InvoiceTemplateRepository extends JpaRepository<InvoiceTemplate, Long> {
	
	InvoiceTemplate findByInvoiceTemplateId(Long invoiceTemplateId);
}
