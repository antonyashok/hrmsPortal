package com.tm.common.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "job_title")
public class UserGroup implements Serializable {

	private static final long serialVersionUID = -9072423702357738423L;

	public enum groupTypeEnum {
		RCTR, EMPL, CNCTR
	}

	@Id
	@Column(name = "job_title_id")
	private String groupId;

	@Column(name = "job_title")
	private String groupName;

	@Column(name = "job_desc")
	private String groupDescription;

	@Column(name = "job_type")
	@Enumerated(EnumType.STRING)
	private groupTypeEnum groupType;

	@CreatedBy
	@Column(name = "created_by", nullable = false, length = 50, updatable = false)
	@JsonIgnore
	private Long createdBy;

	@CreatedDate
	@Column(name = "create_dt", nullable = false)
	@JsonIgnore
	private ZonedDateTime createdDate = ZonedDateTime.now();

	@LastModifiedBy
	@Column(name = "updated_by", length = 50)
	@JsonIgnore
	private Long lastModifiedBy;

	@JsonIgnore
	@Column(name = "last_updt_dt")
	private ZonedDateTime lastModifiedDate = ZonedDateTime.now();

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public groupTypeEnum getGroupType() {
		return groupType;
	}

	public void setGroupType(groupTypeEnum groupType) {
		this.groupType = groupType;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Long getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public ZonedDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}
