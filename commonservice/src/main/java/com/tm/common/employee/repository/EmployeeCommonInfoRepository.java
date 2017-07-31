package com.tm.common.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.employee.domain.EmployeeCommonInfo;

public interface EmployeeCommonInfoRepository extends JpaRepository<EmployeeCommonInfo, Long> {

	@Modifying
	@Query("delete from EmployeeCommonInfo e where e.employeeId=:employeeId")
	int deleteEmployeeCommonInfo(@Param("employeeId") Long employeeId);

}
