package com.tm.invoice.mongo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tm.invoice.mongo.domain.InvoiceReturn;

@Repository
public interface InvoiceReturnRepository extends MongoRepository<InvoiceReturn, UUID>,InvoiceSetupDetailsRepositoryCustom {

	List<InvoiceReturn> findByStatus(String status);
	
	List<InvoiceReturn> findByStatusNot(String status);

}
