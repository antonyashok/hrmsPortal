package com.tm.invoice.mongo.repository;

import java.util.Date;

import com.tm.invoice.domain.InvoiceSetupDetails;

public interface InvoiceSetupDetailsRepositoryCustom {

	InvoiceSetupDetails getInvoiceSetupDetailsListByInvoiceSetupId(Long invoiceSetupId,Date runDate);
}
