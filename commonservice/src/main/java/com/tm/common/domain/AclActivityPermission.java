package com.tm.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "acl_activity_permission")
@JsonIgnoreProperties({ "createDate", "lastUpdateDate" })
public class AclActivityPermission implements Serializable {

	private static final long serialVersionUID = 7797825804423266064L;

	public enum ActiveFlagEnum {
		Y, N
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "activity_permission_id", nullable = false)
	private Long activityPermissionId;

	@Column(name = "activity_id", nullable = false)
	private Long activityId;

	@Column(name = "principal", nullable = false)
	private String principal;

	@Column(name = "principal_type", nullable = false)
	private String principalType;

	@Column(name = "permission_mask", nullable = false)
	private String permissionMask;

	@Enumerated(EnumType.STRING)
	@Column(name = "active_flg")
	private ActiveFlagEnum activeFlag = ActiveFlagEnum.Y;

	@Column(name = "create_date", nullable = false)
	private Date createDate;

	@Column(name = "last_updt_date")
	private Date lastUpdateDate;

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

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

}
