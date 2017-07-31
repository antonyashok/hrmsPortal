package com.tm.engagement.service.dto;

import java.util.UUID;

import org.hibernate.annotations.Type;

public class EngagementTaskDTO {

    @Type(type = "uuid-char")
    private UUID engagementTaskId;

    @Type(type = "uuid-char")
    private UUID engagementId;

    @Type(type = "uuid-char")
    private UUID taskId;
    
    private String taskName;

    public UUID getEngagementTaskId() {
        return engagementTaskId;
    }

    public void setEngagementTaskId(UUID engagementTaskId) {
        this.engagementTaskId = engagementTaskId;
    }

    public UUID getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(UUID engagementId) {
        this.engagementId = engagementId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
    
}
