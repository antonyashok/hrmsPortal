package com.tm.timesheet.service.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.domain.ActivityLog;
import com.tm.timesheet.repository.ActivityLogRepository;

public class TimesheetServiceUtils {
    
    @Inject
    private  ActivityLogRepository activityLogRepository;
    
    public void saveTimehseetActivityLog1(EmployeeProfileDTO employee, UUID timesheetId,
            String updatedDate, String comment, String refType) {
        List<ActivityLog> activityLogList = new ArrayList<>();
        ActivityLog activityLog = new ActivityLog();
        activityLog.setEmployeeId(employee.getEmployeeId());
        activityLog.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
        activityLog.setEmployeeRoleName(employee.getRoleName());
        activityLog.setSourceReferenceId(timesheetId);
        activityLog.setSourceReferenceType(refType);
        activityLog.setComment(comment);
        activityLog.setDateTime(updatedDate);
        activityLog.setUpdatedOn(new Date());
        activityLog.setId(ResourceUtil.generateUUID());
        activityLogList.add(activityLog);
        activityLogRepository.save(activityLogList);
    }
    
    public static void saveTimehseetActivityLog(EmployeeProfileDTO employee, UUID timesheetId,
            String updatedDate, String comment, String refType) {
        TimesheetServiceUtils.saveTimehseetActivityLog(employee, timesheetId, updatedDate, comment, refType);
    }


}
