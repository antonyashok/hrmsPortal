package com.tm.common.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.common.employee.domain.EmployeeAddressView;

public interface EmployeeAddressViewRepository extends JpaRepository<EmployeeAddressView, Long> {
	
	EmployeeAddressView findByEmployeeIdAndAddressType(Long employeeId,String addressType);
}
