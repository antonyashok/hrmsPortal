package com.tm.common.employee.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.common.domain.Accolades;



public interface AccoladesRepository extends MongoRepository<Accolades, UUID> {
	
}