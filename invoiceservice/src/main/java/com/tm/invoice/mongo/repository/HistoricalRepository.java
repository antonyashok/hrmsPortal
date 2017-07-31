package com.tm.invoice.mongo.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.invoice.mongo.domain.Historical;

public interface HistoricalRepository extends MongoRepository<Historical, UUID>, HistoricalRepositoryCustom {

    

}
