package com.tm.invoice.reader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.tm.invoice.dto.InvoiceSetupBatchDTO;

public class RegenerateInvoiceReader implements ItemReader<InvoiceSetupBatchDTO> {

	private static final Logger log = LoggerFactory.getLogger(RegenerateInvoiceReader.class);

	@StepScope
	@Value("#{stepExecutionContext[from]}")
	public int fromId;

	@StepScope
	@Value("#{stepExecutionContext[to]}")
	public int toId;

	@StepScope
	@Value("#{stepExecutionContext[applicationlivedate]}")
	public Date applicationLiveDate;

	@StepScope
	@Value("#{stepExecutionContext[weekStartDate]}")
	public LocalDate runCronDate;

	@StepScope
	@Value("#{stepExecutionContext[invoiceLiveDate]}")
	public Date invoiceLiveDate;

	public List<Integer> keys = new ArrayList<>();

	@Autowired
	public InvoiceEngineMigrationReader invoiceEngineServiceImpl;

	private String invoiceQueueId;
	
	@Override
	@StepScope
	public InvoiceSetupBatchDTO read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (keys.contains(fromId)) {
			return null;
		}
		keys.add(fromId);
		log.info("********************InvoiceEngine Reader Start*********************");
		log.info("InvoiceSetup reader is calling");
		log.info("startIndex ---{}", fromId);
		log.info("endIndex ---{}", toId);
		log.info("applicationLiveDate ---{}", applicationLiveDate);
		log.info("runCronDate ---{}", runCronDate);
		log.info("invoiceLiveDate ---{}", invoiceLiveDate);
		
		log.info("InvoiceEngineExceptionReportReader Constructor invoiceQueueId : {}" ,invoiceQueueId);
		InvoiceSetupBatchDTO invoiceBatchDTO = null;
		if(invoiceQueueId != null){
			invoiceBatchDTO = invoiceEngineServiceImpl.regenrateInvoiceExceptionReportEngineReader(invoiceQueueId, runCronDate, invoiceLiveDate);
		}
		log.info("********************InvoiceEngine Reader End*********************");
		return invoiceBatchDTO;
	}
	public RegenerateInvoiceReader(final String invoiceQueueId){
		this.invoiceQueueId = invoiceQueueId;
		log.info("InvoiceEngineExceptionReportReader Constructor invoiceQueueId : {}" ,invoiceQueueId);
	}
	
 
}