package com.tm.common.service.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AclModuleDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 7797825804423266064L;
	
	private Long moduleId;
	
	private String moduleName;
	
	private String groupName;
	
	private String linkName;
		
	private List<AclActivityPermissionDTO> aclActivityPermissionList;

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public List<AclActivityPermissionDTO> getAclActivityPermissionList() {
		return aclActivityPermissionList;
	}

	public void setAclActivityPermissionList(List<AclActivityPermissionDTO> aclActivityPermissionList) {
		this.aclActivityPermissionList = aclActivityPermissionList;
	}
	
	
	
	
	
}
