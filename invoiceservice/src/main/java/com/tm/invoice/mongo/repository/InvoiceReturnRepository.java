package com.tm.invoice.mongo.repository;


import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tm.invoice.mongo.domain.InvoiceReturn;

@Repository
public interface InvoiceReturnRepository extends MongoRepository<InvoiceReturn, UUID>, InvoiceReturnRepositoryCustom {

  
}
