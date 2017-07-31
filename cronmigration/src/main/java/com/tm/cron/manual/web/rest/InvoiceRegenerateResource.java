package com.tm.cron.manual.web.rest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.domain.InvoiceQueue;
import com.tm.invoice.dto.StatusDTO;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.scheduler.Scheduler;

@RestController
public class InvoiceRegenerateResource {

	private static final Logger log = LoggerFactory.getLogger(InvoiceRegenerateResource.class);

	@Autowired
	Scheduler scheduler;

	@Autowired(required = true)
	private InvoiceQueueRepository invoiceQueueRepository;

	@RequestMapping(value = "/regenrateInvoice/{invoiceNumber}", method = RequestMethod.POST)
	@RequiredAuthority({ InvoiceConstants.FINANCE_REPRESENTATIVE })
	public ResponseEntity<StatusDTO> regenrateInvoice(@PathVariable("invoiceNumber") String invoiceNumber)
			throws Exception {
		log.info("regenrateInvoice : Starts");
		String status = "Failure";
		scheduler.regenrateInvoice(invoiceNumber);

		InvoiceQueue invoiceQueue = invoiceQueueRepository.findOneByInvoiceNumber(invoiceNumber);
		StatusDTO statusDTO = new StatusDTO();
		if (null != invoiceQueue && StringUtils.isNotBlank(invoiceQueue.getExceptionSource())) {
			status = "Failure";
		} else if (null != invoiceQueue) {
			status = "Success";
		}
		statusDTO.setStatus(status);
		log.info("regenrateInvoice : Ends");
		return new ResponseEntity<>(statusDTO, HttpStatus.OK);
	}

}
