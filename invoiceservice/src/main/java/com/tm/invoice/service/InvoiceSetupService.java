package com.tm.invoice.service;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import com.tm.invoice.domain.InvoiceSetupView;
import com.tm.invoice.dto.InvoiceSetupDTO;
import com.tm.invoice.mongo.dto.InvoiceSetupActivitiesLogDTO;

public interface InvoiceSetupService {

    List<InvoiceSetupView> getAllExistingActiveSetups(Long customerId);

    InvoiceSetupDTO populateExistingSetupDetails(UUID invoiceSetupId, String invoiceType,
            Long customerId);

    InvoiceSetupDTO saveInvoiceSetup(InvoiceSetupDTO invoiceSetup) throws ParseException;

    InvoiceSetupDTO getInvoiceSetup(UUID engagementId);

    List<InvoiceSetupActivitiesLogDTO> getInvoiceSetupActivityLog(UUID sourceReferenceId);


}
