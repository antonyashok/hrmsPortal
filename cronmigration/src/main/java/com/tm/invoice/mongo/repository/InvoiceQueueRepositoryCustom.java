package com.tm.invoice.mongo.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.domain.InvoiceQueue;

public interface InvoiceQueueRepositoryCustom {
	
	List<InvoiceQueue> getInvoiceQueueByBillingDateAndCronDate(Date billingDate, UUID purchaseOrderId);
	
	InvoiceQueue getOneInvoiceQueueOrderByPurchaseOrderId(UUID purchaseOrderId);

	InvoiceQueue getOneInvoiceQueueOrderByInvoiceSetupId(UUID invoiceSetupId);

	long getAllInvoiceExceptionCount();

	Page<InvoiceQueue> getAllInvoiceQueue(Pageable pageable);

}
