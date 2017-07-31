package com.tm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.domain.EmployeeVO;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeVO, Long> {

	@Query("select CONCAT(e.lastName,', ',e.firstName) as submitterName from EmployeeVO e where e.employeeId=:userId")
	String getSubmitterNameById(@Param("userId") Long userId);

}
