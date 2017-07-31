package com.tm.invoice.mongo.repository;

import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.tm.invoice.mongo.domain.ManualInvoice;

public interface ManualInvoiceRepository
        extends MongoRepository<ManualInvoice, ObjectId>, ManualInvoiceRepositoryCustom {

    @Query("{'status':?0}")
    Page<ManualInvoice> getAllManualInvoices(String status, Pageable pageRequest);

    ManualInvoice findByInvoiceId(UUID invoiceId);

    void deleteByInvoiceId(UUID invoiceId);    
    
    ManualInvoice findTopByOrderByCreatedDateDesc();


}
