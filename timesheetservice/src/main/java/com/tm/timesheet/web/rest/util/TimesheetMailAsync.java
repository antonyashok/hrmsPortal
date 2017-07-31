/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.TimesheetManagementApp.java
 * Author        : Annamalai L
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.web.rest.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.Employee;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.timeoff.domain.Timeoff;
import com.tm.timesheet.web.rest.util.MailManagerUtil;


@Configuration
@EnableAsync
public class TimesheetMailAsync {

	@Async
	public void sendMailWithAsync(Employee timesheetBelongsToEmployee, EmployeeProfileDTO reportingEmployeeProfile,
			Timesheet timesheet, String configName,MailManagerUtil mailManagerUtil) {
		mailManagerUtil.sendTimesheetNotificationMail(timesheetBelongsToEmployee, reportingEmployeeProfile,
				reportingEmployeeProfile.getPrimaryEmailId(), timesheet, configName,
				TimesheetConstants.MAIL_HIGH_PRIORITY);
	}
	
	@Async
	public void sendMailWithAsync(EmployeeProfileDTO timesheetBelongsToEmployee, EmployeeProfileDTO reportingEmployeeProfile,
			Timeoff timesheet, String configName,MailManagerUtil mailManagerUtil) {
		mailManagerUtil.sendTimeOffNotificationMail(timesheetBelongsToEmployee, reportingEmployeeProfile,
				reportingEmployeeProfile.getPrimaryEmailId(), timesheet, configName,
				TimesheetConstants.MAIL_HIGH_PRIORITY);
	}
}
