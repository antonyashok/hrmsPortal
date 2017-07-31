package com.tm.engagement.service.dto;

import java.util.UUID;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TaskDetailDTO extends ResourceSupport {

    private static final String TASK_NAME_BLANK_ERR = "Task name is required";

    @Type(type = "uuid-char")
    private UUID taskId;

    private String taskDescription;

    @NotNull(message = TASK_NAME_BLANK_ERR)
    private String taskName;
    
    @Transient
    private boolean deletable;
    
    @Transient
    @Type(type = "uuid-char")
    private UUID taskGroupId;
   
    @ManyToOne
    @JoinColumn
    private TaskGroupDTO taskGroupDTO;
    
    @Transient
    private boolean taskDescriptionEditable;
    
    @Transient
    private boolean taskNameEditable;
    
    @Transient
    private boolean showDelete;
    
    private String employeeId = "";
    
    private String employeeName = "";

    public boolean isShowDelete() {
        return showDelete;
    }

    public void setShowDelete(boolean showDelete) {
        this.showDelete = showDelete;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public TaskGroupDTO getTaskGroupDTO() {
        return taskGroupDTO;
    }

    public void setTaskGroupDTO(TaskGroupDTO taskGroupDTO) {
        this.taskGroupDTO = taskGroupDTO;
    }

    public UUID getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(UUID taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public boolean isTaskDescriptionEditable() {
        return taskDescriptionEditable;
    }

    public void setTaskDescriptionEditable(boolean taskDescriptionEditable) {
        this.taskDescriptionEditable = taskDescriptionEditable;
    }

    public boolean isTaskNameEditable() {
        return taskNameEditable;
    }

    public void setTaskNameEditable(boolean taskNameEditable) {
        this.taskNameEditable = taskNameEditable;
    }

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
    
}
