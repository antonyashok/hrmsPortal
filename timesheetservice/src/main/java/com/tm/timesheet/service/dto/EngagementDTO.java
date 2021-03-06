package com.tm.timesheet.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "Engagement", collectionRelation = "Engagements")
public class EngagementDTO implements Serializable {

	private static final long serialVersionUID = -6387223640688563638L;
	private String engagementId;
	private String name;
	private Long accountManagerId;
    private String clientManagerName;
    private String recruiterName;
    private String engagementName;
	private String accountManagerName;
	private String accountManagerMailId;
	private Long recruiterId;
	private String recruiterMailId;
	private Long clientManagerId;
	private String clientManagerMailId;
	private Long fileNumber;
	private String hiringManagerName;
	private String hiringManagerMailId;

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

	public String getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(String engagementId) {
		this.engagementId = engagementId;
	}

	public String getClientManagerName() {
		return clientManagerName;
	}

	public void setClientManagerName(String clientManagerName) {
		this.clientManagerName = clientManagerName;
	}

	public String getRecruiterName() {
		return recruiterName;
	}

	public void setRecruiterName(String recruiterName) {
		this.recruiterName = recruiterName;
	}

	public String getEngagementName() {
		return engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
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

	public String getHiringManagerName() {
		return hiringManagerName;
	}

	public void setHiringManagerName(String hiringManagerName) {
		this.hiringManagerName = hiringManagerName;
	}

	public String getHiringManagerMailId() {
		return hiringManagerMailId;
	}

	public void setHiringManagerMailId(String hiringManagerMailId) {
		this.hiringManagerMailId = hiringManagerMailId;
	}
	
}
