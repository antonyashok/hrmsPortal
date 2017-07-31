/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.service.dto.TimesheetTaskDTO.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "taskDetails", collectionRelation = "taskDetails")
public class TimesheetTaskDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 9197397201740176391L;
	
	private String taskName = "task0";
	private String taskId = "task0";
	private Boolean activeTaskFlag = false;
	private Boolean startFlag = false;
	private List<TimesheetDetailsDTO> timesheetDetailList;
	private UUID timesheetId;

	public UUID getTimesheetId() {
		return timesheetId;
	}

	public void setTimesheetId(UUID timesheetId) {
		this.timesheetId = timesheetId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<TimesheetDetailsDTO> getTimesheetDetailList() {
		return timesheetDetailList;
	}

	public void setTimesheetDetailList(List<TimesheetDetailsDTO> timesheetDetailList) {
		this.timesheetDetailList = timesheetDetailList;
	}

	public Boolean getActiveTaskFlag() {
		return activeTaskFlag;
	}

	public void setActiveTaskFlag(Boolean activeTaskFlag) {
		this.activeTaskFlag = activeTaskFlag;
	}

	public Boolean getStartFlag() {
		return startFlag;
	}

	public void setStartFlag(Boolean startFlag) {
		this.startFlag = startFlag;
	}
}
