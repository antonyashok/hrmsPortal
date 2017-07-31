package com.tm.timesheet.timesheetview.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.timesheet.domain.TimesheetAttachments;
import com.tm.timesheet.service.dto.TimesheetAttachmentsDTO;

@Mapper
public interface TimesheetAttachmentViewMapper {

    public TimesheetAttachmentViewMapper INSTANCE = Mappers.getMapper(TimesheetAttachmentViewMapper.class);

      TimesheetAttachmentsDTO timesheetAttachmentsToTimesheetAttachmentsDTO(
            TimesheetAttachments timesheetAttachments);

     List<TimesheetAttachmentsDTO> timesheetAttachmentsToTimesheetAttachmentsDTO(
            List<TimesheetAttachments> timesheetAttachments);

}
