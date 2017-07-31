package com.tm.engagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.engagement.domain.Status;
import com.tm.engagement.domain.Task;
import com.tm.engagement.service.dto.TaskDetailDTO;
import com.tm.engagement.service.dto.TaskGroupDTO;

public interface TaskGroupService {

    Page<TaskGroupDTO> getAllTaskGroupDetails(Pageable pageable);

    TaskGroupDTO getAllTaskDetails(UUID taskGroupId, Pageable pageable);

    Task deleteTask(UUID taskId);

    Status saveTask(TaskGroupDTO taskGroupDTO);
    
    List<TaskGroupDTO> getTaskGroupDetailsList();

    List<TaskDetailDTO> getTaskGroupDetailsList(String taskGroupId,UUID engagementId);

}
