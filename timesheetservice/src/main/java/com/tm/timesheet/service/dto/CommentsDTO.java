package com.tm.timesheet.service.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentsDTO implements Serializable {


    private static final long serialVersionUID = -7568286292060410307L;
    private Boolean isTask;
    private List<TimesheetCommentDTO> timesheetComments;
    private List<TimesheetCommentDTO> timeOffComments;

    public List<TimesheetCommentDTO> getTimesheetComments() {
        return timesheetComments;
    }

    public void setTimesheetComments(List<TimesheetCommentDTO> timesheetComments) {
        this.timesheetComments = timesheetComments;
    }

    public List<TimesheetCommentDTO> getTimeOffComments() {
        return timeOffComments;
    }

    public void setTimeOffComments(List<TimesheetCommentDTO> timeOffComments) {
        this.timeOffComments = timeOffComments;
    }

	public Boolean getIsTask() {
		return isTask;
	}

	public void setIsTask(Boolean isTask) {
		this.isTask = isTask;
	}



}
