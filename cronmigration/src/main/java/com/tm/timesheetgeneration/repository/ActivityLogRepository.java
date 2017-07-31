package com.tm.timesheetgeneration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tm.timesheetgeneration.domain.ActivityLog;

@Repository
public interface ActivityLogRepository extends MongoRepository<ActivityLog, String> {

}
