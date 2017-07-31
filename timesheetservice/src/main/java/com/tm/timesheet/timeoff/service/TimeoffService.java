package com.tm.timesheet.timeoff.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.service.dto.EngagementDTO;
import com.tm.timesheet.timeoff.service.dto.PtoAvailableDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffRequestDetailDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffStatus;

public interface TimeoffService {

	Page<TimeoffDTO> getMyTimeoffList(Pageable pageable, String startDate, String endDate, String status, Long userid,String searchParam);

	TimeoffStatus getMyTimeoffStatusCount(Long userid, String startDate, String endDate, String searchParam);

	TimeoffDTO createTimeoff(TimeoffDTO timeoffDTO, EmployeeProfileDTO employeeProfileDTO) throws ParseException;

	List<TimeoffRequestDetailDTO> getTimeoffDates(EmployeeProfileDTO employeeProfileDTO, String startDate,
			String endDate,String engagementId);

	PtoAvailableDTO getMyPtoTimeoffDetails(EmployeeProfileDTO employeeProfileDTO);
	
	PtoAvailableDTO getMyPtoTimeoffDetails(EmployeeProfileDTO employeeProfileDTO,String engagementId);
	
	PtoAvailableDTO getMyTeamPtoTimeoffDetails(String employeeId,String timeoffId,String engagementId);

	List<TimeoffDTO> getMyTimeoffHolidays(String startDate, String endDate, Long provinceId, Long userid,
			Date joiningDate,String employeeType,String engagementId);
	
	List<TimeoffDTO> getMyTeamTimeoffHolidays(String startDate, String endDate,String engagementId,String employeeId);

	Page<TimeoffDTO> getMyTeamTimeoff(Pageable pageable, String startDate, String endDate, String status, Long userid,String searchParam);

	TimeoffStatus getMyTeamTimeoffStatusCount(Long userid, String startDate, String endDate, String searchParam);

	Page<PtoAvailableDTO> getMyTeamTimeoffAvaliableList(Long userid, Pageable pageable,String searchParam);

	TimeoffDTO getMyTimeoff(String timeoffId, String navigationScreen);

	void deleteMyTimeoff(String timeoffId);
	
	TimeoffDTO updateTimeoffStatus(List<TimeoffDTO> timeoffDTO, EmployeeProfileDTO employeeProfileDTO);
	
	void createTimesheetTimeoff(List<TimeoffDTO> timeoffDTOs, EmployeeProfileDTO employeeProfileDTO);
	
	TimeoffDTO updateTimesheetTimeoffStatus(List<TimeoffDTO> timeoffDTO, EmployeeProfileDTO employeeProfileDTO);
	
	void createPTOAvailable(PtoAvailableDTO ptoAvailableDTO);
	
	EmployeeProfileDTO getLoggedInUser();

	void removeAndUpdateTimesheetAppliedTimeoff(String ptoTypeName, UUID timesheetId);
	
	List<EngagementDTO> getEngagements(EmployeeProfileDTO employeeProfileDTO);

	PtoAvailableDTO getPTOAccural(String startDate,Long PTOAccural);
	
	//TimeoffDTO createMobileTimeoff(TimeoffDTO timeoffDTO, EmployeeProfileDTO employeeProfileDTO) throws ParseException;
	
	TimeoffDTO createMobileTimeoff(List<TimeoffDTO> timeoffDTOs, EmployeeProfileDTO employeeProfileDTO) throws ParseException;
	
	//TimeoffDTO getTimesheetTimeoffView(TimeoffDTO timeoffDTO, EmployeeProfileDTO employeeProfileDTO);
	
	TimeoffDTO getTimesheetTimeoffView(String startDate, String ptoType,String engagementId, EmployeeProfileDTO employeeProfileDTO);
	
	//Timesheet getTimesheetTimeoff(String timesheetid);
	
	Long getCountByUserIdAndStatus(Long reportingManagerId, String status) throws ParseException;

	//Double calculateTimeOffHours(UUID timesheetId);
	
	Double calculateTimeOffHours(Long userId,String startDate,String endDate,String engagementId);
}