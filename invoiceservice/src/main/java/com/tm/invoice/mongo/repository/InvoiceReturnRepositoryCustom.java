package com.tm.invoice.mongo.repository;


public interface InvoiceReturnRepositoryCustom {

	Long getInvoiceReturnCountByReportingManagerIdAndStatus(String status, Long reportingManagerId);
}
