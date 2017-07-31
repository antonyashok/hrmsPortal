package com.tm.common.employee.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

public class UserGroupMappingDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 3854544840529004778L;
	
	private Long userId;
	
	private Long userGroupId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

}
