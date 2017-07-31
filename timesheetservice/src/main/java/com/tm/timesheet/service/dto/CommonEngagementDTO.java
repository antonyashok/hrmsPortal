package com.tm.timesheet.service.dto;

import java.io.Serializable;
import java.util.List;


public class CommonEngagementDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum day {
		Sun(7), Mon(1), Tue(2), Wed(3), Thu(4), Fri(5), Sat(6);
		private int value;

		private day(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private Long emplId;
	private String name;
	private Long accountManagerId;
	private String accountManagerName;
	private String accountManagerMailId;
	private Long recruiterId;
	private String recruiterName;
	private String recruiterMailId;
	private Long clientManagerId;
	private String clientManagerName;
	private String clientManagerMailId;
	private Long fileNumber;
	private String employeeEngagementId;
	private String engagementName;
	private String engagementStartDate;
	private String engagementEndDate;
	private String emplEffStartDate;
	private String emplEffEndDate;
	private String engagementId;
	private String tsEntryLookUpId;
	private String tsEntryLookUpName;
	private String tsMethodLookUpId;
	private String tsMethod;
	private day startDay;
	private day endDay;
	private String timeruleId;
	private List<TaskDTO> taskDTO;

	public String getTimeruleId() {
		return timeruleId;
	}

	public void setTimeruleId(String timeruleId) {
		this.timeruleId = timeruleId;
	}

	public Long getAccountManagerId() {
		return accountManagerId;
	}

	public void setAccountManagerId(Long accountManagerId) {
		this.accountManagerId = accountManagerId;
	}

	public String getAccountManagerName() {
		return accountManagerName;
	}

	public void setAccountManagerName(String accountManagerName) {
		this.accountManagerName = accountManagerName;
	}

	public Long getRecruiterId() {
		return recruiterId;
	}

	public void setRecruiterId(Long recruiterId) {
		this.recruiterId = recruiterId;
	}

	public String getEmployeeEngagementId() {
		return employeeEngagementId;
	}

	public void setEmployeeEngagementId(String employeeEngagementId) {
		this.employeeEngagementId = employeeEngagementId;
	}

	public String getEngagementName() {
		return engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
	}

	public String getEngagementStartDate() {
		return engagementStartDate;
	}

	public void setEngagementStartDate(String engagementStartDate) {
		this.engagementStartDate = engagementStartDate;
	}

	public String getEngagementEndDate() {
		return engagementEndDate;
	}

	public void setEngagementEndDate(String engagementEndDate) {
		this.engagementEndDate = engagementEndDate;
	}

	public String getEmplEffStartDate() {
		return emplEffStartDate;
	}

	public void setEmplEffStartDate(String emplEffStartDate) {
		this.emplEffStartDate = emplEffStartDate;
	}

	public String getEmplEffEndDate() {
		return emplEffEndDate;
	}

	public void setEmplEffEndDate(String emplEffEndDate) {
		this.emplEffEndDate = emplEffEndDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountManagerMailId() {
		return accountManagerMailId;
	}

	public void setAccountManagerMailId(String accountManagerMailId) {
		this.accountManagerMailId = accountManagerMailId;
	}

	public String getRecruiterName() {
		return recruiterName;
	}

	public void setRecruiterName(String recruiterName) {
		this.recruiterName = recruiterName;
	}

	public String getRecruiterMailId() {
		return recruiterMailId;
	}

	public void setRecruiterMailId(String recruiterMailId) {
		this.recruiterMailId = recruiterMailId;
	}

	public Long getClientManagerId() {
		return clientManagerId;
	}

	public void setClientManagerId(Long clientManagerId) {
		this.clientManagerId = clientManagerId;
	}

	public String getClientManagerName() {
		return clientManagerName;
	}

	public void setClientManagerName(String clientManagerName) {
		this.clientManagerName = clientManagerName;
	}

	public String getClientManagerMailId() {
		return clientManagerMailId;
	}

	public void setClientManagerMailId(String clientManagerMailId) {
		this.clientManagerMailId = clientManagerMailId;
	}

	public Long getEmplId() {
		return emplId;
	}

	public void setEmplId(Long emplId) {
		this.emplId = emplId;
	}

	public Long getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(Long fileNumber) {
		this.fileNumber = fileNumber;
	}

	public String getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(String engagementId) {
		this.engagementId = engagementId;
	}

	public String getTsEntryLookUpId() {
		return tsEntryLookUpId;
	}

	public void setTsEntryLookUpId(String tsEntryLookUpId) {
		this.tsEntryLookUpId = tsEntryLookUpId;
	}

	public String getTsEntryLookUpName() {
		return tsEntryLookUpName;
	}

	public void setTsEntryLookUpName(String tsEntryLookUpName) {
		this.tsEntryLookUpName = tsEntryLookUpName;
	}

	public String getTsMethodLookUpId() {
		return tsMethodLookUpId;
	}

	public void setTsMethodLookUpId(String tsMethodLookUpId) {
		this.tsMethodLookUpId = tsMethodLookUpId;
	}

	public String getTsMethod() {
		return tsMethod;
	}

	public void setTsMethod(String tsMethod) {
		this.tsMethod = tsMethod;
	}

	public day getStartDay() {
		return startDay;
	}

	public void setStartDay(day startDay) {
		this.startDay = startDay;
	}

	public day getEndDay() {
		return endDay;
	}

	public void setEndDay(day endDay) {
		this.endDay = endDay;
	}

	public List<TaskDTO> getTaskDTO() {
		return taskDTO;
	}

	public void setTaskDTO(List<TaskDTO> taskDTO) {
		this.taskDTO = taskDTO;
	}
	
}
