package com.tm.invoice.mongo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.invoice.mongo.domain.InvoiceQueueDetails;

public interface InvoiceQueueDetailsRepository  extends MongoRepository<InvoiceQueueDetails, UUID> {
	List<InvoiceQueueDetails> findByInvoiceSetupId(UUID invoiceSetupId,Pageable pageable);

}
