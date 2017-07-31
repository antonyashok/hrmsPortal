package com.tm.common.employee.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "employee_role_view")
public class EmployeeRoleView implements Serializable {

	private static final long serialVersionUID = -6337962664413674316L;

	@Id
	@Column(name = "emp_rl_id")
	private Long roleId;

	@Column(name = "emp_rl_nm")
	private String roleName;

	@Column(name = "emp_rl_desc")
	private String roleDescription;

	@Type(type = "uuid-char")
	@Column(name = "band_id")
	private UUID bandId;

	@Column(name = "attrib_val")
	private String bandName;

	@Column(name = "actv_flg")
	private String activeFlag;

	@Column(name = "last_updt_dt")
	private String updatedDate;

	@Column(name = "category")
	private String category;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public UUID getBandId() {
		return bandId;
	}

	public void setBandId(UUID bandId) {
		this.bandId = bandId;
	}

	public String getBandName() {
		return bandName;
	}

	public void setBandName(String bandName) {
		this.bandName = bandName;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
