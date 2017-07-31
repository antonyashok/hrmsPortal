package com.tm.invoice.mongo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tm.invoice.mongo.domain.PendingBillingQueueDetails;

@Repository
public interface PendingBillingQueueDetailsRepository
        extends MongoRepository<PendingBillingQueueDetails, ObjectId> {

   /* @Query("{'contractorId':?0}")
    List<PendingBillingQueueDetails> getCollectionBycontractorId(
            @Param(InvoiceConstants.CONTRACTOR_ID) Long contractorId);*/

}
