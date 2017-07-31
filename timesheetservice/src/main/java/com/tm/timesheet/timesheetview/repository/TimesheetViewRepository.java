package com.tm.timesheet.timesheetview.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tm.timesheet.domain.Timesheet;

@Repository
public interface TimesheetViewRepository extends MongoRepository<Timesheet, UUID>, TimesheetViewRepositoryCustom {

	@Query(value = "{'_id':?0}", fields = "{billStartDate:1,billEndDate:1,employee._id:1,employee.type:1,employee.provinceId:1,engagement._id:1,engagement.name:1,startDate:1,"
			+ "endDate:1,stHours:1,otHours:1,dtHours:1,status:1,lookupType:1,configGroupId:1,totalHours:1,source:1,ptoHours:1,leaveHours:1,workHours:1}")
	public Timesheet getTimesheetByTimesheetId(UUID id);

}
