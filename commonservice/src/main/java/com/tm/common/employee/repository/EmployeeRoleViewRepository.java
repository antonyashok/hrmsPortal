package com.tm.common.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.common.employee.domain.EmployeeRoleView;

public interface EmployeeRoleViewRepository extends JpaRepository<EmployeeRoleView, Long> {

	
}
