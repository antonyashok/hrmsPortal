package com.tm.common.employee.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "employee_role")
@JsonIgnoreProperties({ "createdDate", "updatedDate", "createdBy", "updatedBy" })
public class EmployeeRole implements Serializable {

	private static final long serialVersionUID = 1102706424398134347L;

	public enum ActiveFlagEnum {
		Y, N
	}

	public enum CategoryEnum {
		EMPL, RCTR
	}

	@Id
	@GeneratedValue
	@Column(name = "emp_rl_id")
	private Long roleId;

	@Column(name = "emp_rl_nm")
	private String roleName;

	@Column(name = "emp_rl_desc")
	private String roleDescription;

	@Type(type = "uuid-char")
	@Column(name = "band_id")
	private UUID bandId;

	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private ActiveFlagEnum activeFlag;

	@Enumerated(EnumType.STRING)
	@Column(name = "category")
	private CategoryEnum category;

	@Column(name = "create_dt", updatable = false)
	private Date createdDate;

	@Column(name = "last_updt_dt")
	private Date updatedDate;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "updated_by")
	private Long updatedBy;

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

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
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

	public CategoryEnum getCategory() {
		return category;
	}

	public void setCategory(CategoryEnum category) {
		this.category = category;
	}

}
