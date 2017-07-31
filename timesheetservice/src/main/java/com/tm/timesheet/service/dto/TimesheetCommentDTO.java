package com.tm.timesheet.service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimesheetCommentDTO implements Serializable {


    private static final long serialVersionUID = -8962706472832158101L;

    private String commentDate;
    private String taskName;
    private String hours;
    private String comments;
    private String timeOffType;
    private Boolean isTask;
    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getTimeOffType() {
        return timeOffType;
    }

    public void setTimeOffType(String timeOffType) {
        this.timeOffType = timeOffType;
    }

	public Boolean getIsTask() {
		return isTask;
	}

	public void setIsTask(Boolean isTask) {
		this.isTask = isTask;
	}

	
}
