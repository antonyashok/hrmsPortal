package com.tm.timesheet.timesheetview.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.tm.timesheet.domain.TimesheetDetails;

 
public interface TimesheetDetailsViewRepository extends MongoRepository<TimesheetDetails, UUID>, TimesheetDetailsViewRepositoryCustom {

	@Query(value = "{'timesheetId':?0}", fields = "{_id:1,timesheetId:1,taskName:1,timesheetDate:1,dayOfWeek:1,hours:1,units:1,comments:1,"
			+ "timeDetail.startTime:1,timeDetail.endTime:1,timeDetail.hours:1,timeDetail.breakHours:1,timeDetail.sqncNo:1,activeTaskFlag:1,"
			+ "startFlag:1,timeDetail.overrideFlag:1,timeDetail.activeFlag:1,overrideFlag:1,Id:1,employeeEngagementTaskMapId:1}")
	List<TimesheetDetails> findByTimesheetId(UUID id);

	List<TimesheetDetails> findByTimesheetIdAndTimesheetDate(UUID timesheetId, Date timesheetDate);
	
	List<TimesheetDetails> findByEmployeeEngagementTaskMapId(UUID employeeEngagementTaskMapId);

}
