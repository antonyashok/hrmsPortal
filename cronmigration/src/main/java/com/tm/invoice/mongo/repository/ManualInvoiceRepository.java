package com.tm.invoice.mongo.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tm.invoice.mongo.domain.ManualInvoice;

@Repository
public interface ManualInvoiceRepository extends MongoRepository<ManualInvoice, ObjectId>{

	List<ManualInvoice> findByStatus(String status);
	
	List<ManualInvoice> findByStatusNot(String status);

}
