package com.tm.invoice.mongo.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tm.invoice.mongo.domain.InvoiceAlertDetails;

@Repository
public interface InvoiceAlertDetailRepository extends
		MongoRepository<InvoiceAlertDetails, ObjectId>,
		InvoiceAlertDetailRepositoryCustom {

}
