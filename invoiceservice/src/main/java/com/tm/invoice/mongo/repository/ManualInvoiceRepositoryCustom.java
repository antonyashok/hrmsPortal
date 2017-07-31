package com.tm.invoice.mongo.repository;

import java.util.List;
import java.util.UUID;

import com.tm.invoice.mongo.domain.ManualInvoice;

public interface ManualInvoiceRepositoryCustom {

    List<ManualInvoice> findManualInvoices(List<UUID> invoiceIds);
    
	Long getManualInvoiceCountByFinanceRepIdAndStatus(String status, Long financeRepId); 
}
