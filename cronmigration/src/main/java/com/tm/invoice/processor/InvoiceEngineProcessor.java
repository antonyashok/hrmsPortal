package com.tm.invoice.processor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.tm.invoice.dto.InvoiceDTO;
import com.tm.invoice.dto.InvoiceSetupBatchDTO;

public class InvoiceEngineProcessor implements ItemProcessor<InvoiceSetupBatchDTO, List<InvoiceDTO>> {
	private static final Logger log = LoggerFactory.getLogger(InvoiceEngineProcessor.class);

	@Autowired
	InvoiceEngineMigrationProcessor invoiceEngineMigrationProcessor;

	@Override
	public synchronized List<InvoiceDTO> process(InvoiceSetupBatchDTO item) throws Exception {
		log.info("********************InvoiceEngine Processor Start*********************");
		log.info("item process invoiceList size --->"+item.getInvoiceDTOList().size());
		return invoiceEngineMigrationProcessor.startInvoiceEngineMigrationProcessor(item);
		//return null;
	}

}
