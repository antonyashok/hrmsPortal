package com.tm.timesheet.timesheetview.repository;

import java.util.List;
import java.util.UUID;

import com.tm.timesheet.domain.TimesheetDetails;

 
public interface TimesheetDetailsViewRepositoryCustom {

    public List<TimesheetDetails> getTimesheetComments(UUID id);
    
    public List<TimesheetDetails> findByTimesheetId(List<UUID> ids);
    
    public List<TimesheetDetails> findByTimesheetId(List<UUID> ids, String startDate, String endDate);
}
