package com.tm.common.engagement.service.dto;

public class TaskDTO {

	private String engagementTaskId;
	private String emplEngmtTaskMapId;
	private String taskName;

	public String getEngagementTaskId() {
		return engagementTaskId;
	}

	public void setEngagementTaskId(String engagementTaskId) {
		this.engagementTaskId = engagementTaskId;
	}

	public String getEmplEngmtTaskMapId() {
		return emplEngmtTaskMapId;
	}

	public void setEmplEngmtTaskMapId(String emplEngmtTaskMapId) {
		this.emplEngmtTaskMapId = emplEngmtTaskMapId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}
