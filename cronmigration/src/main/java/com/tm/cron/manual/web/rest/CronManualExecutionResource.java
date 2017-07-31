package com.tm.cron.manual.web.rest;

import java.util.UUID;

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

import com.tm.invoice.domain.InvoiceQueue;
import com.tm.invoice.dto.StatusDTO;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.scheduler.Scheduler;

@RestController
@RequestMapping("/cron/manual")
public class CronManualExecutionResource {

	private static final Logger log = LoggerFactory.getLogger(CronManualExecutionResource.class);
	
	@Autowired
	Scheduler scheduler;  

	@Autowired(required = true)
	private InvoiceQueueRepository invoiceQueueRepository;

	@RequestMapping(value = "/invoiceprocess", method = RequestMethod.POST)
	public HttpStatus runInvoiceProcess() {
		log.info("Manual Cron - Invoice Process : Starts");
		try {
			scheduler.invoiceProcess();
		} catch (Exception e) {
			log.error("Manual Cron - Invoice Process : Exception occured", e.getCause());
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		log.info("Manual Cron - Invoice Process : Ends");
		return HttpStatus.OK;
	}
	
	@RequestMapping(value = "/invoiceexceptionprocess", method = RequestMethod.POST)
	public HttpStatus runInvoiceExceptionReportProcess() {
		log.info("Manual Cron - Invoice exception report process : Starts");
		try {
			scheduler.invoiceExceptionReportProcess();
		} catch (Exception e) {
			log.error("Manual Cron - Invoice exception report process : Exception occured", e.getCause());
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		log.info("Manual Cron - Invoice exception report process : Ends");
		return HttpStatus.OK;
	}
	
	@RequestMapping(value = "/regenrateInvoice/{invoiceQueueId}", method = RequestMethod.POST)
	public ResponseEntity<StatusDTO> regenrateInvoice(@PathVariable("invoiceQueueId") String invoiceQueueId)
			throws Exception {
		log.info("regenrateInvoice : Starts");
		String status = "Failure";
		
		InvoiceQueue existingInvoiceQueue = invoiceQueueRepository.findOne(UUID.fromString(invoiceQueueId));
		scheduler.regenrateInvoice(invoiceQueueId);

		InvoiceQueue invoiceQueue = invoiceQueueRepository.findOneByInvoiceNumber(existingInvoiceQueue.getInvoiceNumber());
		StatusDTO statusDTO = new StatusDTO();
		if (null != invoiceQueue && StringUtils.isBlank(invoiceQueue.getExceptionSource())) {
			status = "Success";
		}  
		statusDTO.setStatus(status);
		log.info("regenrateInvoice : Ends");
		return new ResponseEntity<>(statusDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/contractortimesheet", method = RequestMethod.POST)
	public HttpStatus runContractorTimesheet() {
		log.info("Manual Cron - Contractor Timesheet : Starts");
		try {
			scheduler.contractorManualGenerateTimesheet();
		} catch (Exception e) {
			log.error("Manual Cron - Contractor Timesheet : Exception occured", e.getCause());
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		log.info("Manual Cron - Contractor Timesheet : Ends");
		return HttpStatus.OK;
	}
	
	
	@RequestMapping(value = "/contractorpositivetimesheet", method = RequestMethod.POST)
	public HttpStatus runContractorPositiveTimesheet() {
		log.info("Manual Cron - Contractor Positive Timesheet : Starts");
		try {
			scheduler.contractorManualGenerateTodayTimesheet();
		} catch (Exception e) {
			log.error("Manual Cron - Contractor Positive  Timesheet : Exception occured", e.getCause());
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		log.info("Manual Cron - Contractor Timesheet : Ends");
		return HttpStatus.OK;
	}

	@RequestMapping(value = "/contractornegativetimesheet", method = RequestMethod.POST)
	public HttpStatus runContractorNegativeTimesheet() {
		log.info("Manual Cron - Contractor Negative Timesheet : Starts");
		try {
			scheduler.contractorNegativeProcessTimesheet();
		} catch (Exception e) {
			log.error("Manual Cron - Contractor Negative Timesheet : Exception occured", e.getCause());
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		log.info("Manual Cron - Contractor Timesheet : Ends");
		return HttpStatus.OK;
	}
	

	@RequestMapping(value = "/employeetimesheet", method = RequestMethod.POST)
	public HttpStatus runEmployeeTimesheet() {
		log.info("Manual Cron - Employee Timesheet : Starts");
		try {
			scheduler.employeeTimesheet();
		} catch (Exception e) {
			log.error("Manual Cron - Employee Timesheet : Exception occured", e.getCause());
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		log.info("Manual Cron - Employee Timesheet : Ends");
		return HttpStatus.OK;
	}
	
	@RequestMapping(value = "/recruitertimesheet", method = RequestMethod.POST)
	public HttpStatus runRecruiterTimesheet() {
		log.info("Manual Cron - Recruiter Timesheet : Starts");
		try {
			scheduler.recruiterTimesheet();
		} catch (Exception e) {
			log.error("Manual Cron - Recruiter Timesheet : Exception occured", e.getCause());
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		log.info("Manual Cron - Recruiter Timesheet : Ends");
		return HttpStatus.OK;
	}

	@RequestMapping(value = "/emailjob", method = RequestMethod.POST)
	public HttpStatus runemailJob() {
		log.info("Manual Cron - Email Job : Starts");
		try {
			scheduler.emailJob();
		} catch (Exception e) {
			log.error("Manual Cron - Email Job : Exception occured", e.getCause());
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		log.info("Manual Cron - Email Job : Ends");
		return HttpStatus.OK;
	}
	
	@RequestMapping(value = "/invoicealertsjob", method = RequestMethod.POST)
	public HttpStatus runinvoiceAlertsJob() {
		log.info("Manual Cron - Invoice Alerts Job : Starts");
		try {
			scheduler.invoiceAlertsJob();
		} catch (Exception e) {
			log.error("Manual Cron - Invoice Alerts Job : Exception occured", e.getCause());
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		log.info("Manual Cron - Invoice Alerts Job : Ends");
		return HttpStatus.OK;
	}
}
