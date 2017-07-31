package com.tm.engagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.engagement.domain.EngagementTask;

public interface EngagementTaskRepository extends JpaRepository<EngagementTask, UUID>{
	
	List<EngagementTask> findByEngagementId(UUID engagementId);
	
	List<EngagementTask> findByEngagementIdAndTaskId(UUID engagementId,UUID taskId);
	
	List<EngagementTask> findByEngagementIdAndTaskName(UUID engagementId,String taskName);

}
