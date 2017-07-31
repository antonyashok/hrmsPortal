package com.tm.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_role_authorization")
public class RoleAuthorizationMapping implements Serializable {

	private static final long serialVersionUID = -2903204145240054522L;

	public enum ActiveEnum {
		Y, N
	}

	@Id
	@GeneratedValue
	@Column(name = "role_authorization_id", nullable = false)
	private Long roleAuthorizationId;

	@Column(name = "emp_rl_id", nullable = false)
	private Long employeeRoleId;

	@Column(name = "user_group_id", nullable = false)
	private Long userGroupId;

	@Enumerated(EnumType.STRING)
	@Column(name = "active_flag")
	private ActiveEnum activeFlag;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "updated_date", insertable = false)
	private Date updatedDate;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "updated_by")
	private Long updatedBy;

	public Long getRoleAuthorizationId() {
		return roleAuthorizationId;
	}

	public void setRoleAuthorizationId(Long roleAuthorizationId) {
		this.roleAuthorizationId = roleAuthorizationId;
	}

	public Long getEmployeeRoleId() {
		return employeeRoleId;
	}

	public void setEmployeeRoleId(Long employeeRoleId) {
		this.employeeRoleId = employeeRoleId;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public ActiveEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveEnum activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
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

	
}
