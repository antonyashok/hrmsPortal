package com.tm.invoice.mongo.repository;

import java.util.List;
import java.util.UUID;

import com.tm.invoice.mongo.domain.InvoiceAlertDetails;

public interface InvoiceAlertDetailRepositoryCustom {

	List<InvoiceAlertDetails> getInvoiceAlertDetails(List<UUID> poIds, List<UUID> engagementIds);
	
}
