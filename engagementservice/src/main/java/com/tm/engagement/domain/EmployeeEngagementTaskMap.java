package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.tm.engagement.domain.Engagement.ActiveFlagEnum;

@Entity
@Table(name = "empl_engmt_task_map")
public class EmployeeEngagementTaskMap extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = -6124852792372627165L;

	@Id
	@Column(name = "empl_engmt_task_map_id")
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID employeeEngagementTaskMapId;

	@Column(name = "actv_flg")
	@Enumerated(EnumType.STRING)
	private ActiveFlagEnum activeFlag;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "empl_engmt_id", nullable = false)
	private EmployeeEngagement employeeEngagement;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id", nullable = false)
	private Task task;
	
	@Column(name = "timesheet_generation")
	private String timesheetGeneratedFlag;
	
	@Transient
	private List<UUID> taskIds;

	public UUID getEmployeeEngagementTaskMapId() {
		return employeeEngagementTaskMapId;
	}

	public void setEmployeeEngagementTaskMapId(UUID employeeEngagementTaskMapId) {
		this.employeeEngagementTaskMapId = employeeEngagementTaskMapId;
	}

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
	}

	public EmployeeEngagement getEmployeeEngagement() {
		return employeeEngagement;
	}

	public void setEmployeeEngagement(EmployeeEngagement employeeEngagement) {
		this.employeeEngagement = employeeEngagement;
	}

    public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public List<UUID> getTaskIds() {
		return taskIds;
	}

	public void setTaskIds(List<UUID> taskIds) {
		this.taskIds = taskIds;
	}

	public String getTimesheetGeneratedFlag() {
		return timesheetGeneratedFlag;
	}

	public void setTimesheetGeneratedFlag(String timesheetGeneratedFlag) {
		this.timesheetGeneratedFlag = timesheetGeneratedFlag;
	}

}
