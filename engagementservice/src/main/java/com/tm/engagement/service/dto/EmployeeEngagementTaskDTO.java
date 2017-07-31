package com.tm.engagement.service.dto;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class EmployeeEngagementTaskDTO extends ResourceSupport {

    private static final String TASK_NAME_BLANK_ERR = "Task name is required";

    @Type(type = "uuid-char")
    private UUID taskId;
    
    @Type(type = "uuid-char")
    private UUID engagementId;

    private String taskDescription;

    @NotBlank(message = TASK_NAME_BLANK_ERR)
    private String taskName;
    
    private List<EngagementContractorsDTO> contractorList;

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

	public List<EngagementContractorsDTO> getContractorList() {
		return contractorList;
	}

	public void setContractorList(List<EngagementContractorsDTO> contractorList) {
		this.contractorList = contractorList;
	}

	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}
    
}
