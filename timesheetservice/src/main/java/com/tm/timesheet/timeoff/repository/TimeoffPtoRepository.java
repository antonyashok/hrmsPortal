package com.tm.timesheet.timeoff.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.tm.timesheet.timeoff.domain.TimeoffPto;

public interface TimeoffPtoRepository extends JpaRepository<TimeoffPto, UUID>{
	
	
	TimeoffPto findByEmployeeIdAndEngagementId(@Param("employeeId") Long employeeId,
			@Param("engagementId") UUID engagementId);
}