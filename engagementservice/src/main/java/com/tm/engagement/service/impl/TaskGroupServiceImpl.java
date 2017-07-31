package com.tm.engagement.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tm.engagement.domain.EmployeeEngagementTaskMap;
import com.tm.engagement.domain.EngagementTask;
import com.tm.engagement.domain.Status;
import com.tm.engagement.domain.Task;
import com.tm.engagement.domain.TaskGroup;
import com.tm.engagement.exception.TaskGroupException;
import com.tm.engagement.repository.EmployeeEngagementTaskMapRepository;
import com.tm.engagement.repository.EngagementTaskRepository;
import com.tm.engagement.repository.TaskGroupRepository;
import com.tm.engagement.repository.TaskRepository;
import com.tm.engagement.service.TaskGroupService;
import com.tm.engagement.service.dto.TaskDetailDTO;
import com.tm.engagement.service.dto.TaskGroupDTO;
import com.tm.engagement.service.mapper.TaskGroupMapper;

@Service
public class TaskGroupServiceImpl implements TaskGroupService {

    private static final int INITIAL_SIZE = 0;

    private static final String NO_TASK_GRP_EXIST = "Task group not found";

    private static final String ERR_TASK_GRP_NM = "Task group name is already exist";

    private static final String ERR_TASK_NM = "Already exist tasks";

    private static final String NO_TASK_EXIST_FOR_GRP = "No task is exist for this group";

    private static final String TASK_NM_EMPTY_MSG = "Task name should not be empty";
    
    private TaskGroupRepository taskGroupRepository;
    
    private TaskRepository taskRepository;
    
    private EmployeeEngagementTaskMapRepository employeeEngagementTaskMapRepository;
    
    private EngagementTaskRepository engagementTaskRepository;

    @Inject
	public TaskGroupServiceImpl(TaskGroupRepository taskGroupRepository, TaskRepository taskRepository,
			EmployeeEngagementTaskMapRepository employeeEngagementTaskMapRepository,
			EngagementTaskRepository engagementTaskRepository) {
		this.taskGroupRepository = taskGroupRepository;
		this.taskRepository = taskRepository;
		this.employeeEngagementTaskMapRepository = employeeEngagementTaskMapRepository;
		this.engagementTaskRepository= engagementTaskRepository;
	}

    @Override
    @Transactional
    public Page<TaskGroupDTO> getAllTaskGroupDetails(Pageable pageable) {
        int totalSize=taskGroupRepository.findAll().size();
        Pageable pageRequest = pageable;
        if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort()) && totalSize>0) {
            pageRequest = new PageRequest(INITIAL_SIZE, totalSize,
                    Sort.Direction.DESC, "lastModifiedDate");
        }
        List<TaskGroupDTO> taskGroupDetails=new ArrayList<>();
        Page<TaskGroup> taskGroups = taskGroupRepository.findAll(pageRequest);
        if (CollectionUtils.isNotEmpty(taskGroups.getContent())) {
            taskGroupDetails = TaskGroupMapper.INSTANCE.taskGroupsToTaskGroupDTOs(taskGroups.getContent());
        }
        return new PageImpl<>(taskGroupDetails, pageRequest, totalSize);
    }

    @Override
    @Transactional
    public TaskGroupDTO getAllTaskDetails(UUID taskGroupId,Pageable pageable) {
        TaskGroup taskGroup = taskGroupRepository.findByTaskGroupId(taskGroupId);
        if (Objects.nonNull(taskGroup)) {
        TaskGroupDTO taskGroupDTO;
        int totalSize=taskRepository.findByTaskGroup(taskGroup).size();
        if(totalSize==0){
            throw new TaskGroupException(NO_TASK_EXIST_FOR_GRP);
        }
        Pageable pageRequest = pageable;
        if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
            pageRequest = new PageRequest(INITIAL_SIZE, totalSize,
                    Sort.Direction.DESC, "lastModifiedDate");
        }
            Page<Task> tasks = taskRepository.findByTaskGroup(taskGroup,pageRequest);
            taskGroup.setTaskDetails(tasks.getContent());
            taskGroupDTO= TaskGroupMapper.INSTANCE.taskGroupToTaskGroupDTO(taskGroup);
            validateTheTaskForDeleteOption(taskGroupDTO);
            taskGroupDTO.setTotalElements(totalSize);
            return taskGroupDTO;
        } else {
            throw new TaskGroupException(NO_TASK_GRP_EXIST);
        }
    }

    private void validateTheTaskForDeleteOption( TaskGroupDTO taskGroupDTO) {
        List<UUID> taskIds=new ArrayList<>();
        List<UUID> mappedTaskIds=new ArrayList<>();
        List<UUID> commonIds=new ArrayList<>() ;
        List<TaskDetailDTO> tasks=taskGroupDTO.getTaskDetails();
        tasks.forEach(task->taskIds.add(task.getTaskId()));
        List<EmployeeEngagementTaskMap> mappedTaks=employeeEngagementTaskMapRepository.findByEngagementTaskEngagementTaskId(taskIds);
        if(CollectionUtils.isNotEmpty(mappedTaks)){
        mappedTaks.forEach(task->mappedTaskIds.add(task.getTask().getTaskId()));
       commonIds = new ArrayList<>(mappedTaskIds);
        commonIds.retainAll(taskIds);
        }
        for(TaskDetailDTO task :tasks){
            commonIds.forEach(commonId->{
                if(!task.getTaskId().equals(commonId)){
                    task.setShowDelete(true);
                 }
                });
        }
    }

    @Override
    public Task deleteTask(UUID taskId) {
        if (null != taskId) {
            taskRepository.delete(taskId);
        }
        return taskRepository.findOne(taskId);
    }

    @Override
    public Status saveTask(TaskGroupDTO taskGroupDTO) {
        if (CollectionUtils.isNotEmpty(taskGroupDTO.getTaskDetails())) {
            List<TaskDetailDTO> deletedTasks=new ArrayList<>();
            List<TaskDetailDTO> tasks = taskGroupDTO.getTaskDetails();
            List<TaskDetailDTO> validTasks=new ArrayList<>();
            tasks.forEach(task->{
             if(!task.isDeletable()){
                 if(task.getTaskName().equals(StringUtils.EMPTY)){
                     throw new TaskGroupException(TASK_NM_EMPTY_MSG);
                 }
                 validTasks.add(task); 
             }if(task.isDeletable() && null!=task.getTaskId()){
                 deletedTasks.add(task);
             }
            });
            taskGroupDTO.setTaskDetails(validTasks);
            TaskGroup taskGroup = TaskGroupMapper.INSTANCE.taskGroupDTOToTaskGroup(taskGroupDTO);
            validateTaskGroupName(taskGroupDTO,validTasks,taskGroup);
            if(CollectionUtils.isNotEmpty(deletedTasks)){
                List<Task> tasksToDelete= TaskGroupMapper.INSTANCE.taskDetailDTOsToTasks(deletedTasks);
                   taskRepository.delete(tasksToDelete);
               }
            if(null==taskGroup.getTaskGroupId()){
            taskGroup.setTaskGroupId(UUID.randomUUID());
            }
            List<Task> taskDetails=taskGroup.getTaskDetails();
            taskDetails.forEach(taskDetail->taskDetail.setTaskGroup(taskGroup));
            taskGroup.setTaskDetails(taskDetails);
            taskGroupRepository.save(taskGroup);
        }
        return new Status("ok");
    }

    private void validateTaskName(List<TaskDetailDTO> tasks, TaskGroup taskGroup) {
        List<String> newTaskNames = new ArrayList<>();
        List<Task> existingTasks=new ArrayList<>();
        List<String> existingTaskNames = new ArrayList<>();
        List<UUID> existingIds = new ArrayList<>();
        tasks.forEach(task -> {
            if (null == task.getTaskId()) {
                newTaskNames.add(task.getTaskName());
            } else if (null != task.getTaskId()) {
                existingTaskNames.add(task.getTaskName());
                existingIds.add(task.getTaskId());
            }
        });
        if (null != taskGroup.getTaskGroupId() && CollectionUtils.isNotEmpty(newTaskNames)){
            existingTasks = taskRepository.findTasksByTaskName(newTaskNames, taskGroup);
        } 
        if (CollectionUtils.isNotEmpty(existingTasks)) {
            raiseExceptionForDuplicateTaskName(existingTasks, tasks);
        }
        if (null != taskGroup.getTaskGroupId() && CollectionUtils.isNotEmpty(existingTaskNames)) {
        List<Task> existingTaskNamesByOldTasks =
                taskRepository.findTasksByTaskName(existingTaskNames, taskGroup);
        if (CollectionUtils.isNotEmpty(existingTaskNamesByOldTasks)) {
            existingTaskNamesByOldTasks.forEach(oldTasks -> {
                if (oldTasks.getTaskName().equals(existingTaskNames)
                        && !oldTasks.getTaskId().equals(existingIds)) {
                    raiseExceptionForDuplicateTaskName(existingTaskNamesByOldTasks, tasks);
                }
            });
        }
        }
    }

    private void raiseExceptionForDuplicateTaskName(List<Task> tasks,
            List<TaskDetailDTO> taskDetailDTOs) {
        List<Task> taskDetails = TaskGroupMapper.INSTANCE.taskDetailDTOsToTasks(taskDetailDTOs);
        StringBuilder duplicateNames = new StringBuilder();
        taskDetails.addAll(tasks);
        taskDetails.stream().collect(Collectors.groupingBy(Task::getTaskName)).values().stream()
                .filter(taskWithSameName -> taskWithSameName.size() > 1)
                .forEach(taskWithSameName -> taskWithSameName
                        .forEach(task -> duplicateNames.append(task.getTaskName()).append(",")));
        throw new TaskGroupException(ERR_TASK_NM + " " + duplicateNames.toString());
    }

    private void validateTaskGroupName(TaskGroupDTO taskGroupDTO, List<TaskDetailDTO> tasks,
            TaskGroup taskGroupDetail) {
        StringBuilder duplicateNames = new StringBuilder();
        if (null != taskGroupDTO.getTaskGroupId()) {
            TaskGroup taskGroup =
                    taskGroupRepository.findByTaskGroupName(taskGroupDTO.getTaskGroupName());
            if (Objects.nonNull(taskGroup)
                    && !taskGroup.getTaskGroupId().equals(taskGroupDTO.getTaskGroupId())) {
                throw new TaskGroupException(ERR_TASK_GRP_NM);
            }
            if (CollectionUtils.isNotEmpty(tasks)) {
                validateTaskName(tasks, taskGroupDetail);
            }
        } else {
            TaskGroup taskGroup =
                    taskGroupRepository.findByTaskGroupName(taskGroupDTO.getTaskGroupName());
            if (Objects.nonNull(taskGroup)) {
                throw new TaskGroupException(ERR_TASK_GRP_NM);
            }
            if (CollectionUtils.isNotEmpty(tasks)) {
                tasks.stream().collect(Collectors.groupingBy(TaskDetailDTO::getTaskName)).values()
                        .stream().filter(taskWithSameName -> taskWithSameName.size() > 1)
                        .forEach(taskWithSameName -> taskWithSameName.forEach(
                                task -> duplicateNames.append(task.getTaskName()).append(",")));
                if (duplicateNames.length() > 0) {
                    throw new TaskGroupException(ERR_TASK_NM + " " + duplicateNames.toString());
                }
            }
        }
    }

    @Override
    @Transactional
    public List<TaskGroupDTO> getTaskGroupDetailsList() {
        List<TaskGroup> taskGroupDetails = taskGroupRepository.findAll();
        return TaskGroupMapper.INSTANCE.taskGroupsToTaskGroupDTOs(taskGroupDetails);

    }

    @Override
	public List<TaskDetailDTO> getTaskGroupDetailsList(String taskGroupId, UUID engagementId) {
		List<Task> taskList = new ArrayList<>();
		List<TaskDetailDTO> taskDetailDTOList = new ArrayList<>();
		if (StringUtils.isNotEmpty(taskGroupId)) {
			String[] taskGroupArr = taskGroupId.split(",");
			List<String> taskGroupList = new ArrayList<>(Arrays.asList(taskGroupArr));
			for (String groupId : taskGroupList) {
				taskList = taskRepository.findByTaskGroupTaskGroupId(UUID.fromString(groupId));
			}
			taskDetailDTOList = TaskGroupMapper.INSTANCE.tasksToTaskDetailDTOs(taskList);
			taskDetailDTOList.forEach(taskDetailDTO -> {
				List<EngagementTask> engagementTaskList = engagementTaskRepository
						.findByEngagementIdAndTaskName(engagementId, taskDetailDTO.getTaskName());
				if (CollectionUtils.isNotEmpty(engagementTaskList)) {
					taskDetailDTO.setDeletable(true);
				} else {
					taskDetailDTO.setDeletable(false);
				}
			});
		}
		return taskDetailDTOList;
	}
    
}
