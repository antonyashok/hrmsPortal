package com.tm.timesheetgeneration.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.timesheetgeneration.domain.TimesheetDetails;

public interface TimesheetDetailsRepository extends MongoRepository<TimesheetDetails, UUID> {
	
	List<TimesheetDetails> findByTimesheetId(UUID timesheetId);

}
