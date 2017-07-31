package com.tm.common.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.common.domain.ContractorEmployeeEngagementTaskView;

@Repository
public interface ContractorEmployeeEngagementTaskViewRepository
		extends JpaRepository<ContractorEmployeeEngagementTaskView, UUID> {

	@Query("SELECT ceetv FROM ContractorEmployeeEngagementTaskView ceetv WHERE ceetv.employeeEngagementId IN (:employeeEngagementId)")
	List<ContractorEmployeeEngagementTaskView> getContractorEmployeeEngagementTaskViewByEmployeeEngagement(
			@Param("employeeEngagementId") List<UUID> employeeEngagementIds);

}
