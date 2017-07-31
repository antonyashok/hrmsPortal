package com.tm.invoice.mongo.repository;

import java.util.List;

import com.tm.invoice.mongo.domain.InvoiceBillWatch;

public interface InvoiceBillWatchRepositoryCustom {
	
	List<InvoiceBillWatch> getBillWatchByInvoiceNumber(String invoiceNumber);

}
