package com.tm.timesheet.configuration.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "employee_config_settings_view")
public class EmployeeConfigSettingsView implements Serializable {

	private static final long serialVersionUID = 1467848757034659925L;

	public enum UserGroupCategoryEnum {
		RCTR, NONRCTR, CNCTR
	}

	@Id
	@Column(name = "config_grp_id")
	@Type(type = "uuid-char")
	private UUID configurationGroupId;

	@Column(name = "ofc_id")
	private Long officeId;

	@Enumerated(EnumType.STRING)
	@Column(name = "config_grp_category", nullable = false)
	private UserGroupCategoryEnum userGroupCategory;

	@Column(name = "effctv_end_dt")
	@Temporal(value = TemporalType.DATE)
	private Date effectiveEndDate = new Date();

	public UUID getConfigurationGroupId() {
		return configurationGroupId;
	}

	public void setConfigurationGroupId(UUID configurationGroupId) {
		this.configurationGroupId = configurationGroupId;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public UserGroupCategoryEnum getUserGroupCategory() {
		return userGroupCategory;
	}

	public void setUserGroupCategory(UserGroupCategoryEnum userGroupCategory) {
		this.userGroupCategory = userGroupCategory;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

}
