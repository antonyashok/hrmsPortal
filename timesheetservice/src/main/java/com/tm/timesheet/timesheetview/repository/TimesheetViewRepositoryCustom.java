package com.tm.timesheet.timesheetview.repository;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.timesheet.configuration.service.dto.OfficeLocationDTO;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.service.dto.TimesheetStatusCount;

public interface TimesheetViewRepositoryCustom {

    public List<TimesheetStatusCount> getStatusCount(Date startDate, Date endDate, List<Long> employeeIds,
            String roleId,String searchParam,String actorType, String employeeType, String timesheetType,String office) throws ParseException;

	public Page<Timesheet> getAllTimesheet(List<Long> employeeIds,
			String status, Date startDate, Date endDate, Pageable pageable,
			String roleId, String searchParam, String actorType, String type);

	public List<Timesheet> getTimesheetsDetail(Long employeeId,
			UUID engagementId, Date startDate, Date endDate);

	public List<Timesheet> getTimesheetsDetailTimeoff(Long employeeId,
			UUID engagementId, Date startDate, Date endDate);

	public Timesheet getPreviousTimesheetForApprover(Long employeeId,
			String engagementId, Date startDate);

	public Timesheet getNextTimesheetForApprover(Long employeeId,
			String engagementId, Date endDate);

	public Timesheet getPreviousTimesheetForSubmitter(Long employeeId,
			String engagementId, Date startDate);

	public Timesheet getNextTimesheetForSubmitter(Long employeeId,
			String engagementId, Date endDate);

	Page<Timesheet> getAllPayrollTimesheet(Long employeeId, String status,
			Date startDate, Date endDate, String office, Pageable pageable,
			String searchParam, String actorType, String timesheetType);

	public List<Timesheet> getTimesheetsDetailByStatus(Long employeeId,
			Date startDate, Date endDate, UUID engagementId);

	public Page<Timesheet> getAllTimesheetForAccountManager(Long employeeId,
			String status, Date startDate, Date endDate, Pageable pageable,
			String roleId, String searchParam, String actorType);

	Page<Timesheet> getAllTimesheetForAccountManager(Long employeeId,
			String status, Date startDate, Date endDate, String office,
			Pageable pageable, String searchParam, String actorType);

	public Page<Timesheet> getAllTimesheetForVerification(Long employeeId,
			String status, Date startDate, Date endDate, Pageable pageable,
			String roleId, String searchParam, String actorType);

	Page<Timesheet> getAllTimesheetForVerification(Long employeeId,
			String status, Date startDate, Date endDate, String office,
			Pageable pageable, String searchParam, String actorType);

	public List<OfficeLocationDTO> getOfficeLocations(Long employeeId,
			String roleId, String employeeType);

	public List<Timesheet> getTimesheetsReports(List<Long> employeeIds,
			String status, String startDate, String endDate, UUID projectId,
			String month, String year);
}
