package com.tm.timesheet.timeoff.repository;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.timesheet.timeoff.domain.Timeoff;

public interface TimeoffRepositoryCustom {

	Page<Timeoff> getMyTimeoffList(Long employeeId, String[] status, Date startDate, Date endDate, Pageable pageable,String searchParam)
			throws ParseException;

	Long getTimeoffStatusCountWithDate(String status, Long userid, Date startDate, Date endDate, String searchParam) throws ParseException;

	//List<Timeoff> timeoffList(Long employeeId, String[] status, Date startDate, Date endDate);engagementId
	
	List<Timeoff> timeoffList(Long employeeId, String[] status, Date startDate, Date endDate,String engagementId);
	
	Page<Timeoff> getMyTeamTimeoff(Long employeeId,String[] status,
			Date startDate,Date endDate,Pageable pageable,String searchParam) throws ParseException;
	
	Long getMyTeamTimeoffStatusCountWithDate(String status,Long userid,
			Date startDate,Date endDate, String searchParam) throws ParseException;
	
	void updateTimeoffStatus(String lastUpdatedDate,String employeeId, UUID timeoffId,String status,String userName,Long userId);
	
	Timeoff getTimeoffByRequestDate(Long employeeId, String[] status, Date requestDate, String ptoType);
	
	List<Timeoff> getMobileTimeoffByRequestDate(Long employeeId, String[] status, Date requestDate, String ptoType,String engagementId);

	UUID fileUploadTimeoffDetailsUpdate(UUID timesheetId, UUID engagementId, long employeeId, String employeeName,
			Date requestedDate, double totalHours, String ptoName);
	
	List<Timeoff> findByPtoTypeNameAndPtoRequestDetailTimesheetId(String ptoTypeName, UUID timesheetId);
	
	List<Timeoff> findByPtoRequestDetailTimesheetId(UUID timesheetId);
	
}