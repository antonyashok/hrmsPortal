/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.repository.TimesheetDetailsRepositoryCustom.java
 * Author        : Annamalai L
 * Date Created  : Mar 15, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.tm.timesheet.domain.TimesheetDetails;

@Repository
public interface TimesheetDetailsRepositoryCustom {
		
	public List<TimesheetDetails> getAllTimesheetDetailsByTimesheetIds(List<UUID> timesheetIds);
	
	UUID updateTimesheetdetails(UUID timesheetId, Date timesheetDate, String taskName, double hours);
	
	List<TimesheetDetails> getTimesheetDetails(UUID timesheetId,Date timesheetDate,String taskName);
}