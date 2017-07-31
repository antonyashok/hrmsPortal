package com.tm.invoice.writer;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.tm.invoice.dto.InvoiceDTO;

public class InvoiceEngineExceptionReportWriter implements ItemWriter<List<InvoiceDTO>> {

    private static final Logger log = LoggerFactory.getLogger(InvoiceEngineExceptionReportWriter.class);
    @Autowired
    InvoiceEngineMigrationWriter invoiceEngineMigrationWriter;
    
    @Override
    public void write(List<? extends List<InvoiceDTO>> items) throws Exception {
        log.info("************* Invoice Engine Job Writer Starts *************");
        if(CollectionUtils.isNotEmpty(items) && CollectionUtils.isNotEmpty(items.get(0)))
        invoiceEngineMigrationWriter.startInvoiceEngineMigrationWriter(items.get(0));
        log.info("************* Invoice Engine Job Writer Ends *************");
    }

}