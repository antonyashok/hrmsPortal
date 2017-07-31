/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.web.util.TimesheetActivityLogUtil.java
 * Author        : Annamalai L
 * Date Created  : Apr 7th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.timesheetgeneration.domain.ActivityLog;
import com.tm.timesheetgeneration.domain.Timesheet;

public class TimesheetActivityLogUtil {

	public static final String EEE_MMM_DD_YYYY_HH_MM = "MMM dd, yyyy - hh:mm:ss a";
	
	private TimesheetActivityLogUtil() {}
	
	public static List<ActivityLog> saveTimehseetActivityLog(List<Timesheet> timesheets) {
		SimpleDateFormat dateformater = new SimpleDateFormat(EEE_MMM_DD_YYYY_HH_MM);
        List<ActivityLog> activityLogList = new ArrayList<>();        
        timesheets.forEach(timesheet -> {
        	ActivityLog activityLog = new ActivityLog();
	        activityLog.setEmployeeId(0l);
	        activityLog.setEmployeeName("System");
	        activityLog.setEmployeeRoleName("");
	        activityLog.setSourceReferenceId(timesheet.getId());
	        activityLog.setSourceReferenceType("Timesheet");
	        activityLog.setComment("Timesheet has been created");
	        activityLog.setDateTime(dateformater.format(new Date()));
	        activityLog.setUpdatedOn(new Date());
	        activityLog.setId(ResourceUtil.generateUUID());
	        activityLogList.add(activityLog);
	    });
        return activityLogList;
    }
}
