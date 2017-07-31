package com.tm.timesheet.timeoff.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.tm.timesheet.timeoff.domain.TimeoffActivityLog;

public interface TimeoffActivityLogRepository extends MongoRepository<TimeoffActivityLog, UUID> {

	@Query("{timeoffId : ?0}")
	List<TimeoffActivityLog> findByTimeoffIdQuery(UUID timeoffId);

}
