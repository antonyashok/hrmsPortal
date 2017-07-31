package com.tm.invoice.mongo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tm.invoice.mongo.domain.PendingBillDetails;

@Repository
public interface PendingBillDetailsRepository extends MongoRepository<PendingBillDetails, ObjectId> {

   /* @Query("{'contractorId':?0}")
    List<PendingBillDetails> getCollectionBycontractorId(
            @Param(InvoiceConstants.CONTRACTOR_ID) Long contractorId);

    @Query("{'contractorId':?0}")
    PendingBillDetails getBillingProfileDetBycontractorId(
            @Param(InvoiceConstants.CONTRACTOR_ID) Long contractorId);*/

}
