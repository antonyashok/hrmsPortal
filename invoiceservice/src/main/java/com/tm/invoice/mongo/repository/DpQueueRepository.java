package com.tm.invoice.mongo.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.invoice.mongo.domain.DpQueue;

public interface DpQueueRepository extends MongoRepository<DpQueue, UUID> ,DpQueueRepositoryCustom{
	
	DpQueue findById(UUID id);

	DpQueue findByContractorId(Long contractorId);

}
