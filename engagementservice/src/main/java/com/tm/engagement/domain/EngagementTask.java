package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "engagement_task")
public class EngagementTask extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1681163983825941872L;

	@Id
	@Column(name = "engmt_task_id")
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID engagementTaskId;

	@Column(name = "engmt_id")
	@Type(type = "uuid-char")
	private UUID engagementId;

	@Column(name = "task_id")
	@Type(type = "uuid-char")
	private UUID taskId;
	
	@Column(name = "task_name")
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
