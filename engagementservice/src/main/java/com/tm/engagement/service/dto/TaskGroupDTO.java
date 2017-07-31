package com.tm.engagement.service.dto;

import java.util.List;
import java.util.UUID;

import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.engagement.domain.Task;

@JsonInclude(Include.NON_NULL)
public class TaskGroupDTO extends ResourceSupport {

    private static final String TASK_GRP_NM_BLANK_ERR = "Task group name is required";

    @Type(type = "uuid-char")
    private UUID taskGroupId;

    @NotNull(message = TASK_GRP_NM_BLANK_ERR)
    private String taskGroupName;

    private String taskGroupDescription;

    @OneToMany(mappedBy = "taskGroupDTO")
    private List<TaskDetailDTO> taskDetails;

    @Transient
    private int totalElements;

    @Transient
    private List<Task> tasks;
    
    public UUID getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(UUID taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public String getTaskGroupName() {
        return taskGroupName;
    }

    public void setTaskGroupName(String taskGroupName) {
        this.taskGroupName = taskGroupName;
    }

    public String getTaskGroupDescription() {
        return taskGroupDescription;
    }

    public void setTaskGroupDescription(String taskGroupDescription) {
        this.taskGroupDescription = taskGroupDescription;
    }

    public List<TaskDetailDTO> getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(List<TaskDetailDTO> taskDetails) {
        this.taskDetails = taskDetails;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }  
    

}
