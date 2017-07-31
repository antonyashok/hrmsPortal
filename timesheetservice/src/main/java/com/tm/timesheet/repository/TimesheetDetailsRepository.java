/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.repository.TimesheetDetailsRepository.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tm.timesheet.domain.TimesheetDetails;

@Repository
public interface TimesheetDetailsRepository
        extends MongoRepository<TimesheetDetails, UUID> {
	
	
	Long deleteTimesheetDetailsByTimesheetId(UUID timesheetId);
	
	List<TimesheetDetails> findByTimesheetId(UUID timesheetId);
	
	Long deleteTimesheetDetailsById(UUID timesheetDetailId);
	
	List<TimesheetDetails>findByIdNotAndTimesheetId(UUID timesheetDetailId,UUID timesheetId);
}
