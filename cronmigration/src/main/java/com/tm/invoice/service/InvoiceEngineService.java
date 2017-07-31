package com.tm.invoice.service;

import java.time.LocalDate;
import java.util.Date;

import com.tm.invoice.dto.InvoiceSetupBatchDTO;

public interface InvoiceEngineService {

	InvoiceSetupBatchDTO prepareInvoiceEngineReader(int fromId, int toId, LocalDate runCronDate, Date invoiceLiveDate);
}
