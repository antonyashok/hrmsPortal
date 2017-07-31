package com.tm.invoice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.mongo.dto.InvoiceAlertDetailsDTO;
import com.tm.invoice.mongo.dto.InvoiceAlertsDTO;

public interface AlertsService {

	List<InvoiceAlertsDTO> getInvoiceAlerts();
	
}
