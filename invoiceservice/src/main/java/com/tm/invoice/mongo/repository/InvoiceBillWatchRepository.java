package com.tm.invoice.mongo.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.invoice.mongo.domain.InvoiceBillWatch;

public interface InvoiceBillWatchRepository extends MongoRepository<InvoiceBillWatch, UUID>, InvoiceBillWatchRepositoryCustom{

}
