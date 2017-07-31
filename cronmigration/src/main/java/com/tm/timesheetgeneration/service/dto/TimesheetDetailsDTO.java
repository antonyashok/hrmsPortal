package com.tm.timesheetgeneration.service.dto;

import java.io.Serializable;
import java.util.List;

import com.tm.timesheetgeneration.domain.TimesheetDetails;

public class TimesheetDetailsDTO implements Serializable {

	private static final long serialVersionUID = -6190654269075815174L;

	private Long employeeId;

	private List<TimesheetDetails> timesheetDetailList;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public List<TimesheetDetails> getTimesheetDetailList() {
		return timesheetDetailList;
	}

	public void setTimesheetDetailList(List<TimesheetDetails> timesheetDetailList) {
		this.timesheetDetailList = timesheetDetailList;
	}

}
