package com.tm.common.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "activitypermission_view")
public class AclActivityPermissionView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Column(name = "activity_permission_id")
	private Long activityPermissionId;
	
	@Id
	@Column(name = "activity_id")
	private Long activityId;
	
	@Column(name = "activity_name")
	private String activityName;

	@Column(name = "module_id")
	private Long moduleId;
	
	@Column(name = "principal")
	private String principal;
	

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

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
}