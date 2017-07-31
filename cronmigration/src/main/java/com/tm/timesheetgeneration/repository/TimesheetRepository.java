package com.tm.timesheetgeneration.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tm.timesheetgeneration.domain.Timesheet;

@Repository
public interface TimesheetRepository extends MongoRepository<Timesheet, UUID>, TimesheetRepositoryCustom {

}
