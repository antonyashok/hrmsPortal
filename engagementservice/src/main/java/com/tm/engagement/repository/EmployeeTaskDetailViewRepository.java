package com.tm.engagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tm.engagement.domain.EmployeeTaskDetailView;
@Repository
public interface EmployeeTaskDetailViewRepository extends JpaRepository<EmployeeTaskDetailView, UUID> {

	List<EmployeeTaskDetailView> findByEngagementId(UUID engagementId);
	
	List<EmployeeTaskDetailView> findByEngagementIdAndTaskId(UUID engagementId,UUID taskId);

}
