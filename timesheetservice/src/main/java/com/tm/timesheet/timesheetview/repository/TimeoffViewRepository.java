package com.tm.timesheet.timesheetview.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.timesheet.timeoff.domain.Timeoff;


public interface TimeoffViewRepository extends MongoRepository<Timeoff, UUID>, TimeoffViewRepositoryCustom {

}