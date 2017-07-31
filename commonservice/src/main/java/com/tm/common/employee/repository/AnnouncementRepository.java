package com.tm.common.employee.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.common.domain.Announcement;



public interface AnnouncementRepository extends MongoRepository<Announcement, UUID> {
	
}