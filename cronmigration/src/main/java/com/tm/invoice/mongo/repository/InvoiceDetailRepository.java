package com.tm.invoice.mongo.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.invoice.domain.InvoiceDetail;

public interface InvoiceDetailRepository  extends MongoRepository<InvoiceDetail, UUID> {


}
