package com.tm.timesheet.timesheetview.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.tm.timesheet.domain.TimesheetAttachments;

public interface TimesheetAttachmentsViewRepository
        extends MongoRepository<TimesheetAttachments, UUID> {

    @Query(value = "{'timesheetId':?0}")
    List<TimesheetAttachments> getFileDetailsByTimesheetId(UUID timesheetId);

}
