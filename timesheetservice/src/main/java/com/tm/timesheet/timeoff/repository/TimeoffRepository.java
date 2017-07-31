package com.tm.timesheet.timeoff.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tm.timesheet.timeoff.domain.Timeoff;

public interface TimeoffRepository extends MongoRepository<Timeoff, UUID>, TimeoffRepositoryCustom {
	
	/*@Query(value="{ptoTypeName : ?0, ptoRequestDetail.timesheetId : ?1}")
	List<Timeoff> findByPtoTypeNameAndPtoRequestDetailTimesheetId(String ptoTypeName, UUID timesheetId);
	
	@Query(value="{ptoRequestDetail.timesheetId : ?0}")
	List<Timeoff> findByPtoRequestDetailTimesheetId(UUID timesheetId);*/
	
}