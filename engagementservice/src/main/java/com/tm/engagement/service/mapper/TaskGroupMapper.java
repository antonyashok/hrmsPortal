package com.tm.engagement.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.engagement.domain.Task;
import com.tm.engagement.domain.TaskGroup;
import com.tm.engagement.service.dto.TaskDetailDTO;
import com.tm.engagement.service.dto.TaskGroupDTO;

@Mapper
public interface TaskGroupMapper {


    TaskGroupMapper INSTANCE = Mappers.getMapper(TaskGroupMapper.class);

    TaskGroupDTO taskGroupToTaskGroupDTO(TaskGroup taskGroup);

    TaskGroup taskGroupDTOToTaskGroup(TaskGroupDTO taskGroupDTO);

    List<TaskGroupDTO> taskGroupsToTaskGroupDTOs(List<TaskGroup> taskGroups);

    List<TaskGroup> taskGroupDTOsToTaskGroups(List<TaskGroupDTO> taskGroupDTOs);

    TaskDetailDTO taskToTaskDetailDTO(Task task);

    Task taskDetailDTOToTask(TaskDetailDTO taskDetailDTO);

    List<TaskDetailDTO> tasksToTaskDetailDTOs(List<Task> tasks);

    List<Task> taskDetailDTOsToTasks(List<TaskDetailDTO> taskDetailDTOs);
}
