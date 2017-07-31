package com.tm.timesheetgeneration.repository;

import java.util.Date;
import java.util.List;

import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.domain.TimesheetDetails;

public interface TimesheetRepositoryCustom {

//	List<Timesheet> getCreatedTimesheetsDetail(Date startDate, Date endDate);

	List<Timesheet> getCreatedTimesheetsDetailByEmployeeIds(Date startDate, Date endDate, List<Long> employeeIds);

	List<Timesheet> getCreatedTimesheetsDetailByEmployeeIdAndEngagementId(Date startDate, Date endDate, Long employeeId,
			String engagmentId);
	
	Timesheet getAllTimesheetsByEmpIdAndEngagIdAndLastBillDate(Long employeeId, String engagementId, Date lastInvoiceGeneratedDate);
	
	List<Timesheet> getAllTimesheetsFromMaxTSEndDateToBillDateByEmpIdEngagId(Long employeeId, String engagementId,
			Date maxInvoiceGeneratedDate, Date rundate);

	List<TimesheetDetails> getAllTimesheets(List<java.util.UUID> ids);
}
