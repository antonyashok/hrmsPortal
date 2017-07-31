package com.tm.engagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tm.engagement.domain.TaskGroup;

@Repository
public interface TaskGroupRepository extends JpaRepository<TaskGroup,UUID> {

    TaskGroup findByTaskGroupId(UUID taskGroupId);

    TaskGroup findByTaskGroupName(String taskGroupName);

}
