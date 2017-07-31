package com.tm.timesheet.timesheetview.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tm.timesheet.domain.ActivityLog;

@Repository
public interface ActivityLogViewRepository extends MongoRepository<ActivityLog, UUID> {

    List<ActivityLog> findBySourceReferenceIdOrderByUpdatedOnDesc(UUID sourceReferenceId);
}
