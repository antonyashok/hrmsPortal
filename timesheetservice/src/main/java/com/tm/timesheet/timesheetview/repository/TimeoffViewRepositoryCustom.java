package com.tm.timesheet.timesheetview.repository;

import java.util.Date;
import java.util.List;

import com.tm.timesheet.timeoff.domain.Timeoff;

 
public interface TimeoffViewRepositoryCustom {

    public List<Timeoff> getTimeoffComments(Long employeeId, Date startDate, Date endDate);

}
