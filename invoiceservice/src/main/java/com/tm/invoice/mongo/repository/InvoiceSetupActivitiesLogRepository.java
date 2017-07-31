package com.tm.invoice.mongo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.invoice.mongo.domain.InvoiceSetupActivitiesLog;

public interface InvoiceSetupActivitiesLogRepository
        extends MongoRepository<InvoiceSetupActivitiesLog, UUID> {

    List<InvoiceSetupActivitiesLog> findBySourceReferenceIdOrderByUpdatedOnDesc(
            UUID sourceReferenceId);

}
