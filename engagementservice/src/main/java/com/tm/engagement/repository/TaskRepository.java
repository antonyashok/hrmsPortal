package com.tm.engagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.engagement.domain.Task;
import com.tm.engagement.domain.TaskGroup;

@Repository
public interface TaskRepository extends JpaRepository<Task,UUID>{

    Page<Task> findByTaskGroup(TaskGroup taskGroup, Pageable pageRequest);

    List<Task> findByTaskGroup(TaskGroup taskGroup);

    @Query("SELECT task FROM Task task WHERE task.taskName IN(:taskNames) AND task.taskGroup=:taskGroup")
    List<Task> findTasksByTaskName(@Param("taskNames")List<String> taskNames,@Param("taskGroup") TaskGroup taskGroup);

    @Query("SELECT task FROM Task task WHERE task.taskName IN (:taskNames)")
    List<Task> findTasksByTaskName(@Param("taskNames")List<String> newTaskNames);

    List<Task> findByTaskGroupTaskGroupId(UUID taskGroupId);
    
    Task findByTaskId(UUID taskId);
}
