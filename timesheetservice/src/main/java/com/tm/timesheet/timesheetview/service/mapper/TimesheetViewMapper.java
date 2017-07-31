package com.tm.timesheet.timesheetview.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.ActivityLog;
import com.tm.timesheet.domain.AuditFields;
import com.tm.timesheet.domain.Employee;
import com.tm.timesheet.domain.Engagement;
import com.tm.timesheet.domain.LookUpType;
import com.tm.timesheet.domain.TimeDetail;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.domain.TimesheetDetails;
import com.tm.timesheet.service.dto.ActivityLogDTO;
import com.tm.timesheet.service.dto.AuditFieldsDTO;
import com.tm.timesheet.service.dto.EmployeeDTO;
import com.tm.timesheet.service.dto.EngagementDTO;
import com.tm.timesheet.service.dto.LookUpTypeDTO;
import com.tm.timesheet.service.dto.TimeDetailDTO;
import com.tm.timesheet.service.dto.TimesheetCommentDTO;
import com.tm.timesheet.service.dto.TimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetDetailsDTO;
import com.tm.timesheet.service.dto.TimesheetResourceDTO;
import com.tm.timesheet.timesheetview.constants.TimesheetViewConstants;


@Mapper
public interface TimesheetViewMapper {

	TimesheetViewMapper INSTANCE = Mappers.getMapper(TimesheetViewMapper.class);

	@Mappings({ @Mapping(source = TimesheetViewConstants.ID, target = TimesheetViewConstants.TIMESHEETID),
			@Mapping(source = TimesheetViewConstants.START_DATE, dateFormat = TimesheetViewConstants.DATE_FORMAT, target = TimesheetViewConstants.START_DATE),
			@Mapping(source = TimesheetViewConstants.END_DATE, dateFormat = TimesheetViewConstants.DATE_FORMAT, target = TimesheetViewConstants.END_DATE),
			@Mapping(source = TimesheetViewConstants.START_DATE, dateFormat = TimesheetViewConstants.DATE_FORMAT_OF_MMDDYYY, target = TimesheetViewConstants.FORMATTED_START_DATE),
			@Mapping(source = TimesheetViewConstants.END_DATE, dateFormat = TimesheetViewConstants.DATE_FORMAT_OF_MMDDYYY, target = TimesheetViewConstants.FORMATTED_END_DATE) })
	TimesheetDTO timesheetToTimesheetDTO(Timesheet timesheet);

	@Mapping(source = TimesheetViewConstants.ID, target = TimesheetViewConstants.EMPLOYEEID)
	EmployeeDTO employeeToEmployeeDTO(Employee employee);

	@Mapping(source = TimesheetViewConstants.ID, target = TimesheetViewConstants.ENGAGEMENT_DTO_ID)
	EngagementDTO engagementToEngagementDTO(Engagement engagement);

	LookUpTypeDTO lookUpTypeToLookUpTypeDTO(LookUpType lookUpType);

	@Mapping(source = TimesheetConstants.ON, dateFormat = TimesheetConstants.DATE_TIME_FORMAT,
            target = TimesheetConstants.ON)
	AuditFieldsDTO auditFieldsToAuditFieldsDTO(AuditFields auditFields);

	ActivityLogDTO activityLogToActivityLogDTO(ActivityLog activityLog);

	List<ActivityLogDTO> activityLogsToActivityLogDTOs(List<ActivityLog> activityLogs);

	TimesheetResourceDTO timesheetResourceDTOToTimesheetResourceMap(TimesheetDTO timesheetDTO);

	@Mappings({
			@Mapping(source = TimesheetViewConstants.TIMESHEET_DATE, dateFormat = TimesheetViewConstants.DATE_FORMAT_OF_MMDDYYY, target = TimesheetViewConstants.TIMESHEET_DATE),
			@Mapping(source = TimesheetViewConstants.TIMESHEET_DATE, dateFormat = TimesheetViewConstants.DATE_FORMAT_OF_EEEEMMMDDYYYY, target = TimesheetViewConstants.TIMESHEET_DATE_FORMAT),
			@Mapping(source = TimesheetViewConstants.ID, target = TimesheetViewConstants.TIMESHEETDETAILID)})
	TimesheetDetailsDTO timesheetDetailToTimesheetDetailDTO(TimesheetDetails timesheetDetail);

	List<TimesheetDetailsDTO> timesheetDetailsToTimesheetDetailsDTO(List<TimesheetDetails> timesheetDetails);

	TimeDetailDTO timeDetailToTimeDetailDTO(TimeDetail timeDetail);

	List<TimeDetailDTO> timeDetailListToTimeDetailDTOList(List<TimeDetail> timeDetails);

	List<TimesheetDTO> timesheetToTimesheetDTO(List<Timesheet> timesheet);

	@Mappings({
			@Mapping(source = "timesheetDate", dateFormat = TimesheetViewConstants.DATE_FORMAT_OF_MMDDYY, target = "commentDate") })
	TimesheetCommentDTO timesheetDetailsToTimesheetCommentDTO(TimesheetDetails timesheetDetails);

	List<TimesheetCommentDTO> timesheetDetailsToTimesheetCommentDTOs(List<TimesheetDetails> timesheetDetails);
	
	@Mappings({
		@Mapping(source = TimesheetViewConstants.TIMESHEET_DATE, dateFormat = TimesheetViewConstants.DATE_FORMAT_OF_MMDDYYY, target = TimesheetViewConstants.TIMESHEET_DATE),
		@Mapping(source = TimesheetViewConstants.TIMESHEETDETAILID, target = TimesheetViewConstants.ID)})
	TimesheetDetails timesheetDetailDTOToTimesheetDetail(TimesheetDetailsDTO timesheetDetailDTO);

	List<TimesheetDetails> timesheetDetailsDTOToTimesheetDetails(List<TimesheetDetailsDTO> timesheetDetailsDTO);

	TimeDetail timeDetailDTOToTimeDetail(TimeDetailDTO timeDetailDTO);

	List<TimeDetail> timeDetailDTOListToTimeDetailList(List<TimeDetailDTO> timeDetailsDTO);
}
