package com.tm.common.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AclActivityPermissionDTO extends ResourceSupport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6564295536565185253L;

	private Long activityPermissionId;

	private Long activityId;

	private String activityName;
	
	private String principal;

	private String principalType;

	private String permissionMask;

	public Long getActivityPermissionId() {
		return activityPermissionId;
	}

	public void setActivityPermissionId(Long activityPermissionId) {
		this.activityPermissionId = activityPermissionId;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getPrincipalType() {
		return principalType;
	}

	public void setPrincipalType(String principalType) {
		this.principalType = principalType;
	}

	public String getPermissionMask() {
		return permissionMask;
	}

	public void setPermissionMask(String permissionMask) {
		this.permissionMask = permissionMask;
	}
		
	
	
}
