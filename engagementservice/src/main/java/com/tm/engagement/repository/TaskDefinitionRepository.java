package com.tm.engagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.engagement.domain.TaskDefinition;

public interface TaskDefinitionRepository extends JpaRepository<TaskDefinition, UUID> {
	
	TaskDefinition findByTaskName(String taskName);
	
	List<TaskDefinition> findByTaskNameLike(String taskName);

}
