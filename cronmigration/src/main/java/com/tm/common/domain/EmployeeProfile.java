package com.tm.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * View : common.empl_prfl
 */
@Entity
@Table(name = "empl_prfl")
public class EmployeeProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -849778984925997359L;

	@Id
	@Column(name = "empl_id")
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "empl_email_id")
	private String primaryEmailId;

	@Column(name = "empl_type")
	private String employeeType;

	@Column(name = "emp_role_id")
	private Long roleId;

	@Column(name = "empl_role_nm")
	private String roleName;

	@Column(name = "job_title_id")
	private String jobTitleId;

	@Column(name = "job_title")
	private String jobTitle;

	@Column(name = "ofc_id")
	private Long locationId;

	@Column(name = "ofc_name")
	private String locationName;

	@Temporal(TemporalType.DATE)
	@Column(name = "joining_dt")
	private Date joiningDate;
	
	@Column(name = "mgr_empl_id")
	private Long managerEmployeeId;

	@Column(name = "rep_mgr_id")
	private Long reportingManagerId;

	@Column(name = "rep_mgr_full_name")
	private String reportingManagerName;

	@Column(name = "rep_mgr_email_id")
	private String reportingManagerEmailId;

	@Column(name = "sales_mgr_empl_id")
	private Long salesManagerId;

	@Column(name = "sales_mgr_full_name")
	private String salesManagerName;

	@Column(name = "sales_mgr_email_id")
	private String salesManagerMailId;

	@Column(name = "sales_mgr_tz")
	private String salesManagerTimeZone;

	@Column(name = "st_prv_id")
	private Long provinceId;

	@Column(name = "st_prv_nm")
	private String provinceName;

	@Column(name = "designation_id")
	private Long designationId;

	@Column(name = "full_name")
	private String fullName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPrimaryEmailId() {
		return primaryEmailId;
	}

	public void setPrimaryEmailId(String primaryEmailId) {
		this.primaryEmailId = primaryEmailId;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

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

	public String getJobTitleId() {
		return jobTitleId;
	}

	public void setJobTitleId(String jobTitleId) {
		this.jobTitleId = jobTitleId;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	public Long getManagerEmployeeId() {
		return managerEmployeeId;
	}

	public void setManagerEmployeeId(Long managerEmployeeId) {
		this.managerEmployeeId = managerEmployeeId;
	}

	public Long getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(Long reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public String getReportingManagerName() {
		return reportingManagerName;
	}

	public void setReportingManagerName(String reportingManagerName) {
		this.reportingManagerName = reportingManagerName;
	}

	public String getReportingManagerEmailId() {
		return reportingManagerEmailId;
	}

	public void setReportingManagerEmailId(String reportingManagerEmailId) {
		this.reportingManagerEmailId = reportingManagerEmailId;
	}

	public Long getSalesManagerId() {
		return salesManagerId;
	}

	public void setSalesManagerId(Long salesManagerId) {
		this.salesManagerId = salesManagerId;
	}

	public String getSalesManagerName() {
		return salesManagerName;
	}

	public void setSalesManagerName(String salesManagerName) {
		this.salesManagerName = salesManagerName;
	}

	public String getSalesManagerMailId() {
		return salesManagerMailId;
	}

	public void setSalesManagerMailId(String salesManagerMailId) {
		this.salesManagerMailId = salesManagerMailId;
	}

	public String getSalesManagerTimeZone() {
		return salesManagerTimeZone;
	}

	public void setSalesManagerTimeZone(String salesManagerTimeZone) {
		this.salesManagerTimeZone = salesManagerTimeZone;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Long getDesignationId() {
		return designationId;
	}

	public void setDesignationId(Long designationId) {
		this.designationId = designationId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
