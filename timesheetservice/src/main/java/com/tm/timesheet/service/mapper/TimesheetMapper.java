/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.service.mapper.TimesheetMapper.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.AuditFields;
import com.tm.timesheet.domain.Employee;
import com.tm.timesheet.domain.Engagement;
import com.tm.timesheet.domain.LookUpType;
import com.tm.timesheet.domain.TimeDetail;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.domain.TimesheetDetails;
import com.tm.timesheet.domain.UploadFilesDetails;
import com.tm.timesheet.service.dto.AuditFieldsDTO;
import com.tm.timesheet.service.dto.EmployeeDTO;
import com.tm.timesheet.service.dto.EngagementDTO;
import com.tm.timesheet.service.dto.LookUpTypeDTO;
import com.tm.timesheet.service.dto.TimeDetailDTO;
import com.tm.timesheet.service.dto.TimesheetDTO;
import com.tm.timesheet.service.dto.TimesheetDetailsDTO;
import com.tm.timesheet.service.dto.UploadFilesDetailsDTO;
import com.tm.timesheet.timesheetview.constants.TimesheetViewConstants;

@Mapper
public interface TimesheetMapper {

    TimesheetMapper INSTANCE = Mappers.getMapper(TimesheetMapper.class);

    @Mappings({@Mapping(source = TimesheetConstants.ID, target = TimesheetConstants.TIMESHEETID),
            @Mapping(source = TimesheetConstants.START_DATE,
                    dateFormat = TimesheetConstants.DATE_FORMAT,
                    target = TimesheetConstants.START_DATE),
            @Mapping(source = TimesheetConstants.END_DATE,
                    dateFormat = TimesheetConstants.DATE_FORMAT,
                    target = TimesheetConstants.END_DATE)})
    TimesheetDTO timesheetToTimesheetDTO(Timesheet timesheet);

    @Mapping(source = TimesheetConstants.ID, target = TimesheetConstants.EMPLOYEEID)
    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    EngagementDTO engagementToEngagementDTO(Engagement engagement);

    LookUpTypeDTO lookUpTypeToLookUpTypeDTO(LookUpType lookUpType);
    
    @Mapping(source = TimesheetConstants.ON, dateFormat = TimesheetConstants.DATE_TIME_FORMAT,
            target = TimesheetConstants.ON)
    AuditFieldsDTO auditFieldsToAuditFieldsDTO(AuditFields auditFields);
    
   	@Mappings({
   			@Mapping(source = TimesheetConstants.TIMESHEET_DATE, dateFormat = TimesheetConstants.DATE_FORMAT_OF_DDMMYYY, target = TimesheetConstants.TIMESHEET_DATE),
   			@Mapping(source = TimesheetConstants.TIMESHEET_DATE, dateFormat = TimesheetConstants.DATE_FORMAT_OF_EEEEMMMDDYYYY, target = TimesheetConstants.TIMESHEET_DATE_FORMAT),
   			@Mapping(source = TimesheetViewConstants.ID, target = TimesheetViewConstants.TIMESHEETDETAILID)})
   	TimesheetDetailsDTO timesheetDetailToTimesheetDetailDTO(TimesheetDetails timesheetDetail);

   	TimeDetailDTO timeDetailToTimeDetailDTO(TimeDetail timeDetail);
   	
   	List<TimesheetDetailsDTO> timesheetDetailsToTimesheetDetailsDTO(List<TimesheetDetails> timesheetDetails);
   	
   	List<TimeDetailDTO> timeDetailListToTimeDetailDTOList(List<TimeDetail> timeDetails);
   	
   	
   	TimeDetail timeDetailDTOToTimeDetail(TimeDetailDTO timeDetailDTO);

	@Mappings({
			@Mapping(source = TimesheetConstants.TIMESHEET_DATE, dateFormat = TimesheetConstants.DATE_FORMAT_OF_MMDDYYY, target = TimesheetConstants.TIMESHEET_DATE),
			@Mapping(source = TimesheetViewConstants.TIMESHEETDETAILID, target = TimesheetViewConstants.ID)})
	TimesheetDetails timesheetDetailDTOToTimesheetDetail(TimesheetDetailsDTO timesheetDetailsDTO);

	List<TimeDetail> timeDetailDTOListToTimeDetailList(List<TimeDetailDTO> timeDetailsDTO);
	@Mapping(source = "uploaddate", dateFormat = "MMM dd yyyy hh:mm:ss a",
            target = "uploaddate")
	UploadFilesDetailsDTO uploadFilesDetailsDTOToUploadFilesDetails(UploadFilesDetails uploadFilesDetails);
}
