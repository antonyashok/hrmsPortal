/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.repository.TimesheetRepositoryCustom.java
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

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.service.dto.TimesheetStatusCount;

public interface TimesheetRepositoryCustom {

    public List<TimesheetStatusCount> getStatusCount(Date startDate, Date endDate, Long employeeId,
            String roleId) throws ParseException;

    public Page<Timesheet> getAllTimesheet(Long employeeId, String status, Date startDate,
            Date endDate, Pageable pageable, String roleId);
    
    public List<Timesheet> getAllTimesheetbyIds(List<UUID> timesheetIds);
    
    List<Timesheet> getCreatedTimesheetsDetail(Date startDate, Date endDate);
    List<Timesheet> getTimesheetsForFileuploadProcess(UUID engagementId, String engagementName, 
                    long employeeNumber, String employeeName);
    void getTimesheetsForFileuploadProcessUpdate(UUID timesheetId);
    
	Long getMyTeamTimesheetsCountByStatus(String status, Long userId, boolean isApproval);

	Date getTimesheetEndDateByStatusAndEmployeeId(String status, Long employeeId, Date date);
}
