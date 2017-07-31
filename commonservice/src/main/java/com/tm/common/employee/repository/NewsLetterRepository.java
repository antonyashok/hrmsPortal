package com.tm.common.employee.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.common.domain.Newsletter;


public interface NewsLetterRepository extends MongoRepository<Newsletter, UUID> {
	
}