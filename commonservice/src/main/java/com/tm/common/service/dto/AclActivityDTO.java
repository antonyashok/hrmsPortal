package com.tm.common.service.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AclActivityDTO extends ResourceSupport implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long activityId;
	
	private String activityName;
	
	private String authorities;
		
	private Long moduleId;
	
	private String principal;
	
	

	private List<AclActivityPermissionDTO> aclActivityPermissionDTOList;

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

	public String getAuthorities() {
		return authorities;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public List<AclActivityPermissionDTO> getAclActivityPermissionDTOList() {
		return aclActivityPermissionDTOList;
	}

	public void setAclActivityPermissionDTOList(List<AclActivityPermissionDTO> aclActivityPermissionDTOList) {
		this.aclActivityPermissionDTOList = aclActivityPermissionDTOList;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	
	
	
}
