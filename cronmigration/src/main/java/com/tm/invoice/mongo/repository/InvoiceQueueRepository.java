package com.tm.invoice.mongo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.invoice.domain.InvoiceQueue;


@Repository
public interface InvoiceQueueRepository extends MongoRepository<InvoiceQueue, UUID>, InvoiceQueueRepositoryCustom{
	
	@Query("SELECT invoiceQueue FROM InvoiceQueue AS invoiceQueue WHERE "
    		+ "invoiceQueue.invoiceSetupId IN (:invoiceSetupIds)")
    List<InvoiceQueue> getInvoiceSetupsByInvoiceSetupId(@Param("invoiceSetupIds") List<UUID> invoiceSetupIds);
	
	@Query("SELECT invoiceQueue FROM InvoiceQueue AS invoiceQueue WHERE "
    		+ "invoiceQueue.id = :invoiceSetupIds)")
    List<InvoiceQueue> getInvoiceSetupsByInvoiceSetupId(@Param("id") UUID id);

	InvoiceQueue findOneByInvoiceNumber(String invoiceNumber);
}
