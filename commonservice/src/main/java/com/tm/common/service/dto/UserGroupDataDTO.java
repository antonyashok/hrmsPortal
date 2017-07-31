package com.tm.common.service.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserGroupDataDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 7135830361609838198L;

	private static final String GROUP_NAME_IS_REQUIRED = "Group Name is Required";
	private static final String AUTHORITIES_IS_REQUIRED = "Authority is Required";

	private Long userGroupId;
	@NotBlank(message = GROUP_NAME_IS_REQUIRED)
	private String userGroupName;
	@NotBlank(message = AUTHORITIES_IS_REQUIRED)
	private String authorities;
	private String activeFlag;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy HH:mm:ss a")
	private String createdDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy HH:mm:ss a")
	private String updatedDate;
	private Long createdBy;
	private Long updatedBy;

	private String isConfigured = "N";

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getIsConfigured() {
		return isConfigured;
	}

	public void setIsConfigured(String isConfigured) {
		this.isConfigured = isConfigured;
	}

}
