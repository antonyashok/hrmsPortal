package com.tm.invoice.mongo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.invoice.mongo.domain.InvoiceSetupBillToClient;

@Repository
public interface InvoiceSetupBillToClientRepository
        extends MongoRepository<InvoiceSetupBillToClient, ObjectId> {

    @Query("{'engagementId':?0}")
    InvoiceSetupBillToClient getCollectionByEngegementId(@Param("engagementId") Long engagementId);

    @Query("{'billToClient':?0}")
    InvoiceSetupBillToClient getCollectionByBillToClientName(String billToClient);

}
