package com.tm.engagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.engagement.domain.EmployeeEngagementTaskMap;

public interface EmployeeEngagementTaskMapRepository
        extends JpaRepository<EmployeeEngagementTaskMap, UUID> {

	//EmployeeEngagementTaskMap findByEmployeeEngagementEmployeeEngagamentIdAndEngagementTaskEngagementTaskId(UUID employeeEngagamentId,UUID engagementTaskId);
	
	EmployeeEngagementTaskMap findByEmployeeEngagementEmployeeEngagamentId(UUID employeeEngagamentId);

	@Query("SELECT engmtMap FROM EmployeeEngagementTaskMap engmtMap WHERE engmtMap.task.taskId IN(:taskIds)")
    List<EmployeeEngagementTaskMap> findByEngagementTaskEngagementTaskId(@Param("taskIds")List<UUID> taskIds);

	List<EmployeeEngagementTaskMap> findByTaskTaskId(UUID taskId);
	
	List<EmployeeEngagementTaskMap> findByEmployeeEngagementEmployeeEngagamentIdAndTaskTaskId(UUID employeeEngagamentId,UUID taskId);

}
