package com.tm.timesheetgeneration.service.dto;

import java.util.List;

import com.tm.timesheet.configuration.domain.EmailTaskLog;

public class EmailDTO {

	List<EmailTaskLog> emailTaskLogs;

	public List<EmailTaskLog> getEmailTaskLogs() {
		return emailTaskLogs;
	}

	public void setEmailTaskLogs(List<EmailTaskLog> emailTaskLogs) {
		this.emailTaskLogs = emailTaskLogs;
	}

}
