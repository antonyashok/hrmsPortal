package com.tm.common.engagement.service.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class EngagementDTO extends ResourceSupport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	private Date engagementStartDate;
	private Date engagementEndDate;
	private Date emplEffStartDate;
	private Date emplEffEndDate;
	private String engagementId;
	private String tsEntryLookUpId;
	private String tsEntryLookUpName;
	private String tsMethodLookUpId;
	private String tsMethod;
	private String startDay;
	private String endDay;
	private String engagementTaskId;
	private String emplEngmtTaskMapId;
	private String taskName;
	private String timeruleId;

	public String getTimeruleId() {
		return timeruleId;
	}

	public void setTimeruleId(String timeruleId) {
		this.timeruleId = timeruleId;
	}

	private List<TaskDTO> taskDTO;

	public Long getEmplId() {
		return emplId;
	}

	public void setEmplId(Long emplId) {
		this.emplId = emplId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAccountManagerMailId() {
		return accountManagerMailId;
	}

	public void setAccountManagerMailId(String accountManagerMailId) {
		this.accountManagerMailId = accountManagerMailId;
	}

	public Long getRecruiterId() {
		return recruiterId;
	}

	public void setRecruiterId(Long recruiterId) {
		this.recruiterId = recruiterId;
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

	public Long getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(Long fileNumber) {
		this.fileNumber = fileNumber;
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

	public Date getEngagementStartDate() {
		return engagementStartDate;
	}

	public void setEngagementStartDate(Date engagementStartDate) {
		this.engagementStartDate = engagementStartDate;
	}

	public Date getEngagementEndDate() {
		return engagementEndDate;
	}

	public void setEngagementEndDate(Date engagementEndDate) {
		this.engagementEndDate = engagementEndDate;
	}

	public Date getEmplEffStartDate() {
		return emplEffStartDate;
	}

	public void setEmplEffStartDate(Date emplEffStartDate) {
		this.emplEffStartDate = emplEffStartDate;
	}

	public Date getEmplEffEndDate() {
		return emplEffEndDate;
	}

	public void setEmplEffEndDate(Date emplEffEndDate) {
		this.emplEffEndDate = emplEffEndDate;
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

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

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

	public List<TaskDTO> getTaskDTO() {
		return taskDTO;
	}

	public void setTaskDTO(List<TaskDTO> taskDTO) {
		this.taskDTO = taskDTO;
	}
}