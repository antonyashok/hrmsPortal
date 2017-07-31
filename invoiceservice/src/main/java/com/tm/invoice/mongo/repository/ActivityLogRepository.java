package com.tm.invoice.mongo.repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tm.invoice.domain.ActivityLog;

@Repository
public interface ActivityLogRepository extends MongoRepository<ActivityLog, UUID> {

  List<ActivityLog> findBySourceReferenceIdOrderByUpdatedOnDesc(UUID sourceReferenceId);
  
}
