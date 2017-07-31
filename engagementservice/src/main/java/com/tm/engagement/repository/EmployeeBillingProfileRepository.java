package com.tm.engagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.engagement.domain.EmployeeBillingProfile;

public interface EmployeeBillingProfileRepository extends JpaRepository<EmployeeBillingProfile, UUID> {

	EmployeeBillingProfile findByEmplIdAndEmployeeEngagementEmployeeEngagamentId(Long emplId,UUID employeeEngagamentId);
	
	EmployeeBillingProfile findByEmployeeEngagementEmployeeEngagamentId(UUID employeeEngagamentId);
	
	EmployeeBillingProfile findByEmployeeEngagementEmployeeEngagamentIdAndActiveFlag(UUID employeeEngagamentId,String activeFlag);


}
