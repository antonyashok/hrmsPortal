package com.tm.common.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.common.employee.domain.EmployeeAddress;

public interface EmployeeAddressRepository extends JpaRepository<EmployeeAddress, Long> {
	
	List<EmployeeAddress> findByEmployeeId(Long employeeId);

}
