package com.tm.invoice.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.mongo.dto.InvoiceAlertsDTO;
import com.tm.invoice.service.AlertsService;

@RestController
@RequestMapping("/alerts")
public class AlertsResource {

	@Autowired
	AlertsService alertsService;

	@Inject
	public AlertsResource(AlertsService alertsService) {
		this.alertsService = alertsService;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @RequiredAuthority({ AuthoritiesConstants.FINANCE_MANAGER,AuthoritiesConstants.FINANCE_REPRESENTATIVE })
	public List<InvoiceAlertsDTO> getInvoiceAlerts(Pageable pageable,PagedResourcesAssembler<InvoiceAlertsDTO> pagedAssembler) {
		List<InvoiceAlertsDTO> alertsResult = alertsService.getInvoiceAlerts();
		return alertsResult;
	}
	
}
