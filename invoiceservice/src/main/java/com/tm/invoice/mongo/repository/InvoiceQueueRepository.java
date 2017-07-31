package com.tm.invoice.mongo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.invoice.mongo.domain.InvoiceQueue;

public interface InvoiceQueueRepository extends MongoRepository<InvoiceQueue, UUID>, InvoiceQueueRepositoryCustom {	
	List<InvoiceQueue> findByInvoiceSetupIdAndStatus(UUID invoiceSetupId, String status);
}
