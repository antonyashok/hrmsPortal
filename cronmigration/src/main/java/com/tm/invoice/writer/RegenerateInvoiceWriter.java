package com.tm.invoice.writer;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.tm.invoice.dto.InvoiceDTO;

public class RegenerateInvoiceWriter implements ItemWriter<List<InvoiceDTO>> {

	private static final Logger log = LoggerFactory.getLogger(RegenerateInvoiceWriter.class);
	
	@Autowired
	InvoiceEngineMigrationWriter invoiceEngineMigrationWriter;

	@Override
	public void write(List<? extends List<InvoiceDTO>> items) throws Exception {
		log.info("************* RegenerateInvoiceWriter Starts *************");
		if (CollectionUtils.isNotEmpty(items) && CollectionUtils.isNotEmpty(items.get(0)))
			invoiceEngineMigrationWriter.startInvoiceEngineMigrationWriter(items.get(0));
		log.info("************* IRegenerateInvoiceWriter Ends *************");
	}

}