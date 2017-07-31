package com.tm.invoice.mongo.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.invoice.constants.InvoiceConstants;
import com.tm.invoice.domain.InvoiceSetupDetails;

@Repository
public interface InvoiceSetupDetailsRepository extends MongoRepository<InvoiceSetupDetails, ObjectId>,InvoiceSetupDetailsRepositoryCustom {

    @Query("{'invoiceSetupId':?0}")
    InvoiceSetupDetails findInvoiceSetupDetailsByInvoiceSetupId(
            @Param("invoiceSetupId") Long invoiceSetupId);

    @Query("{'invoiceSetupId':?0,'status':?1}")
    InvoiceSetupDetails getInvoiceSetupDetailsByInvoiceSetupIdAndByStatus(
            @Param(InvoiceConstants.INVOICESETUP_ID) Long invoiceSetupId,
            @Param(InvoiceConstants.STATUS) String status);


    @Query("{'invoiceSetupId':?0,'status':?1}")
    InvoiceSetupDetails getInvoiceSetupDetailsByInvoiceSetupIdByStatus(
            @Param(InvoiceConstants.INVOICESETUP_ID) Long invoiceSetupId,
            @Param(InvoiceConstants.STATUS) String status);


    @Query("{'invoiceSetupName':?0}")
    List<InvoiceSetupDetails> getSetupBySetupName(
            @Param(InvoiceConstants.INVOICESETUP_NAME) String invoiceSetupName);

}
