package com.tm.invoice.mongo.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.invoice.mongo.domain.InvoiceAlertDetails;

public interface AlertDetailsRepository extends MongoRepository<InvoiceAlertDetails, ObjectId> {

	Page<InvoiceAlertDetails>findByAlertsType(String alertType,Pageable pageRequest);
	
	List<InvoiceAlertDetails>findByAlertsType(String alertType);
	
}
