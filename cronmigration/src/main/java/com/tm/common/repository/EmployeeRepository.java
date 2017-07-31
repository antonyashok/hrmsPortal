package com.tm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.common.domain.EmployeeProfile;

public interface EmployeeRepository extends JpaRepository<EmployeeProfile, Long> {
	
}