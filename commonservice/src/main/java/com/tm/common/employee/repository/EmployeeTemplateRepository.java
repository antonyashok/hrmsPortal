package com.tm.common.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.common.employee.domain.EmployeeTemplate;

public interface EmployeeTemplateRepository extends JpaRepository<EmployeeTemplate, Long> {

	EmployeeTemplate findByEmployeeTemplateId(Long employeeTemplateId);
}
