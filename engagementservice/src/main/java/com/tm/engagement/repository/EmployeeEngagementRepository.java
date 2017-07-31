package com.tm.engagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tm.engagement.domain.EmployeeEngagement;

@Repository
public interface EmployeeEngagementRepository extends JpaRepository<EmployeeEngagement, UUID> {

	EmployeeEngagement findByEmployeeIdAndEngagementEngagementIdAndActiveFlag(Long employeeId, UUID engagementId,
			String activeFlag);
	
	EmployeeEngagement findByEmployeeEngagamentIdAndActiveFlag(UUID employeeEngagamentId,
			String activeFlag);
	
	EmployeeEngagement findByEmployeeEngagamentId(UUID employeeEngagamentId);

}
