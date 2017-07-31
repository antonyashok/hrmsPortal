package com.tm.timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.timesheet.domain.TimesheetTemplate;


public interface TimesheetTemplateRepository extends JpaRepository<TimesheetTemplate, Long> {

	TimesheetTemplate findByTimesheetTemplateId(Long timesheetTemplateId);

}
