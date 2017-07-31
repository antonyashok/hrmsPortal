package com.tm.common.employee.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class EmployeeRoleDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -9212605264242116777L;

	private static final String ROLE_NAME_IS_REQUIRED = "Designation is Required";
	private static final String ROLE_DESCRIPTION_IS_REQUIRED = "Description is Required";
	private static final String CATEGORY_IS_REQUIRED = "Category is Required";

	private Long roleId;
	@NotBlank(message = ROLE_NAME_IS_REQUIRED)
	private String roleName;
	@NotBlank(message = ROLE_DESCRIPTION_IS_REQUIRED)
	private String roleDescription;
	private String activeFlag;
	@Type(type = "uuid-char")
	private UUID bandId;
	@NotBlank(message = CATEGORY_IS_REQUIRED)
	private String category;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy HH:mm:ss a")
	private String createdDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy HH:mm:ss a")
	private String updatedDate;
	private Long createdBy;
	private Long updatedBy;
	private String attributeValue;
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
	public String getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	public UUID getBandId() {
		return bandId;
	}
	public void setBandId(UUID bandId) {
		this.bandId = bandId;
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
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
}
