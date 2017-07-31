package com.tm.common.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.EmployeeCTC;
import com.tm.common.engagement.domain.EmployeeProfile.ActiveFlagEnum;

public interface EmployeeCTCRepository extends JpaRepository<EmployeeCTC, Long> {
	
	@Query("SELECT e from EmployeeCTC e WHERE e.employeeId=:employeeId and e.activeFlag=:activeFlagEnum")
	EmployeeCTC getEmployeeCTC(@Param("employeeId") Long employeeId,@Param("activeFlagEnum") ActiveFlagEnum activeFlagEnum);
}
