package com.tm.invoice.mongo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.invoice.mongo.domain.InvoiceAttachments;

public interface InvoiceAttachementsRepository extends
		MongoRepository<InvoiceAttachments, ObjectId> {

	 @Query("{'sourceReferenceId':?0,'sourceReferenceName':?1}")
	 InvoiceAttachments getInvoiceSetupDetailsByInvoiceSetupIdAndByStatus(
            @Param("sourceReferenceId") Long sourceReferenceId,
            @Param("sourceReferenceName") String sourceReferenceName);
	 
}
