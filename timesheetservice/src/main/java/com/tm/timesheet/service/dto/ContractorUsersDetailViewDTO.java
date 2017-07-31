package com.tm.timesheet.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;

public class ContractorUsersDetailViewDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -6249337517554435018L;

	private Long employeeId;

	private String firstName;

	private String lastName;

	@Type(type = "uuid-char")
	private UUID engagementId;

	private String engagementName;

	private Long reportManagerId;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}

	public String getEngagementName() {
		return engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
	}

	public Long getReportManagerId() {
		return reportManagerId;
	}

	public void setReportManagerId(Long reportManagerId) {
		this.reportManagerId = reportManagerId;
	}

}
