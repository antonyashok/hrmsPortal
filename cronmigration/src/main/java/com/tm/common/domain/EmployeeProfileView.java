/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.domain.EmployeeProfileView.java
 * Author        : Annamalai L
 * Date Created  : Apr 5th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.common.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "empl_cron_view")
public class EmployeeProfileView implements Serializable {

	private static final long serialVersionUID = -9090221332359128356L;

	public enum HourlyFlag {
		Y, N
	}
	
	public enum EmployeeType {
		C, E
	}
	
	public enum ReportingManagerActiveFlag {
		Y, N
	}
	
	public enum SalesManagerEmployeeActiveFlag {
		Y, N
	}
	
	@Id
	@Column(name = "empl_id")
	private Long employeeId;

	@Column(name = "emp_role_id")
	private Long empoyeeRolelId;

	@Column(name = "empl_role_nm")
	private String employeeRoleName;

	@Column(name = "file_no")
	private Integer fileNo;

	@Column(name = "mgr_empl_id")
	private Long managerEmployeeId;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;

	@Column(name = "joining_dt")
	private Date joiningDate;

	@Column(name = "employee_profile_image_id")
	private String employeeProfileImageId;
	
	@Column(name = "full_name")
	private String fullName;

	@Column(name = "empl_email_id")
	private String employeeEmailId;

	@Column(name = "ofc_id")
	private Long officeId;
	
	@Column(name="ofc_name")
	private String officeName;
	
	@Column(name="tm_zone")
	private String timeZone;

	@Column(name = "hourly_flg", nullable = false)
	@Enumerated(EnumType.STRING)
	private HourlyFlag hourlyFlag;
	
	@Column(name = "empl_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private EmployeeType employeeType;
	
//	@Column(name = "empl_category")
//	private String employeeCategory;
	
	@Column(name = "st_prv_id")
	private Long stateProvinceId;
	
	@Column(name = "st_prv_nm")
	private String stateProvinceName;
	
	@Column(name = "country_id")
	private Long countryId;
	
	@Column(name = "country_name")
	private String countryName;
	
	@Column(name = "rep_mgr_id")
	private Long reportingManagerId;
	
	@Column(name = "rep_actv_flg", nullable = false)
	@Enumerated(EnumType.STRING)
	private ReportingManagerActiveFlag reportingManagerActiveFlag;
	
	@Column(name = "rep_mgr_rolel_id")
	private Long reportingManagerRoleId;
	
	@Column(name = "rep_mgr_role_name")
	private String reportingManagerRoleName;
	
	@Column(name = "rep_mgr_ofc_id")
	private Long reportingManagerOfficeId;
	
	@Column(name = "rep_mgr_ofc_name")
	private String reportingManagerOfficeName;
	
	@Column(name = "rep_mgr_full_name")
	private String reportingManagerFullName; 
	
	@Column(name = "rep_mgr_email_id")
	private String reportingManagerEmailId;
	
	@Column(name = "sales_mgr_empl_id")
	private Long salesManagerEmployeeId;
	
	@Column(name = "sales_mgr_empl_actv_flg", nullable = false)
	@Enumerated(EnumType.STRING)
	private SalesManagerEmployeeActiveFlag salesManagerEmployeeActiveFlag;
	
	@Column(name = "sales_mgr_role_id")
	private Long salesManagerRoleId;
	
	@Column(name = "sales_mgr_role_name")
	private String salesManagerRoleName;
	
	@Column(name = "sales_mgr_ofc_id")
	private Long salesManagerOfficeId;
	
	@Column(name = "sales_mgr_ofc_name")
	private String salesManagerOfficeName;
	
	@Column(name = "sales_mgr_rep_empl_id")
	private Long salesManagerReportingEmployeeId;
	
	@Column(name = "sales_mgr_full_name")
	private String salesManagerFullName;
	

	@Column(name = "sales_mgr_tz")
	private String salesManagerTimeZone;
	
	@Column(name = "sales_mgr_email_id")
	private String salesManagerEmailId;
	
	@Transient
	private UUID configurationGroupId;
	

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getEmpoyeeRolelId() {
		return empoyeeRolelId;
	}

	public void setEmpoyeeRolelId(Long empoyeeRolelId) {
		this.empoyeeRolelId = empoyeeRolelId;
	}

	public String getEmployeeRoleName() {
		return employeeRoleName;
	}

	public void setEmployeeRoleName(String employeeRoleName) {
		this.employeeRoleName = employeeRoleName;
	}

	public Integer getFileNo() {
		return fileNo;
	}

	public void setFileNo(Integer fileNo) {
		this.fileNo = fileNo;
	}

	public Long getManagerEmployeeId() {
		return managerEmployeeId;
	}

	public void setManagerEmployeeId(Long managerEmployeeId) {
		this.managerEmployeeId = managerEmployeeId;
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

	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getEmployeeProfileImageId() {
		return employeeProfileImageId;
	}

	public void setEmployeeProfileImageId(String employeeProfileImageId) {
		this.employeeProfileImageId = employeeProfileImageId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmployeeEmailId() {
		return employeeEmailId;
	}

	public void setEmployeeEmailId(String employeeEmailId) {
		this.employeeEmailId = employeeEmailId;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public HourlyFlag getHourlyFlag() {
		return hourlyFlag;
	}

	public void setHourlyFlag(HourlyFlag hourlyFlag) {
		this.hourlyFlag = hourlyFlag;
	}

	public EmployeeType getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(EmployeeType employeeType) {
		this.employeeType = employeeType;
	}

//	public String getEmployeeCategory() {
//		return employeeCategory;
//	}
//
//	public void setEmployeeCategory(String employeeCategory) {
//		this.employeeCategory = employeeCategory;
//	}

	public Long getStateProvinceId() {
		return stateProvinceId;
	}

	public void setStateProvinceId(Long stateProvinceId) {
		this.stateProvinceId = stateProvinceId;
	}

	public String getStateProvinceName() {
		return stateProvinceName;
	}

	public void setStateProvinceName(String stateProvinceName) {
		this.stateProvinceName = stateProvinceName;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Long getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(Long reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public ReportingManagerActiveFlag getReportingManagerActiveFlag() {
		return reportingManagerActiveFlag;
	}

	public void setReportingManagerActiveFlag(
			ReportingManagerActiveFlag reportingManagerActiveFlag) {
		this.reportingManagerActiveFlag = reportingManagerActiveFlag;
	}

	public Long getReportingManagerRoleId() {
		return reportingManagerRoleId;
	}

	public void setReportingManagerRoleId(Long reportingManagerRoleId) {
		this.reportingManagerRoleId = reportingManagerRoleId;
	}

	public String getReportingManagerRoleName() {
		return reportingManagerRoleName;
	}

	public void setReportingManagerRoleName(String reportingManagerRoleName) {
		this.reportingManagerRoleName = reportingManagerRoleName;
	}

	public Long getReportingManagerOfficeId() {
		return reportingManagerOfficeId;
	}

	public void setReportingManagerOfficeId(Long reportingManagerOfficeId) {
		this.reportingManagerOfficeId = reportingManagerOfficeId;
	}

	public String getReportingManagerOfficeName() {
		return reportingManagerOfficeName;
	}

	public void setReportingManagerOfficeName(String reportingManagerOfficeName) {
		this.reportingManagerOfficeName = reportingManagerOfficeName;
	}

	public String getReportingManagerFullName() {
		return reportingManagerFullName;
	}

	public void setReportingManagerFullName(String reportingManagerFullName) {
		this.reportingManagerFullName = reportingManagerFullName;
	}

	public String getReportingManagerEmailId() {
		return reportingManagerEmailId;
	}

	public void setReportingManagerEmailId(String reportingManagerEmailId) {
		this.reportingManagerEmailId = reportingManagerEmailId;
	}

	public Long getSalesManagerEmployeeId() {
		return salesManagerEmployeeId;
	}

	public void setSalesManagerEmployeeId(Long salesManagerEmployeeId) {
		this.salesManagerEmployeeId = salesManagerEmployeeId;
	}

	public SalesManagerEmployeeActiveFlag getSalesManagerEmployeeActiveFlag() {
		return salesManagerEmployeeActiveFlag;
	}

	public void setSalesManagerEmployeeActiveFlag(
			SalesManagerEmployeeActiveFlag salesManagerEmployeeActiveFlag) {
		this.salesManagerEmployeeActiveFlag = salesManagerEmployeeActiveFlag;
	}

	public Long getSalesManagerRoleId() {
		return salesManagerRoleId;
	}

	public void setSalesManagerRoleId(Long salesManagerRoleId) {
		this.salesManagerRoleId = salesManagerRoleId;
	}

	public String getSalesManagerRoleName() {
		return salesManagerRoleName;
	}

	public void setSalesManagerRoleName(String salesManagerRoleName) {
		this.salesManagerRoleName = salesManagerRoleName;
	}

	public Long getSalesManagerOfficeId() {
		return salesManagerOfficeId;
	}

	public void setSalesManagerOfficeId(Long salesManagerOfficeId) {
		this.salesManagerOfficeId = salesManagerOfficeId;
	}

	public String getSalesManagerOfficeName() {
		return salesManagerOfficeName;
	}

	public void setSalesManagerOfficeName(String salesManagerOfficeName) {
		this.salesManagerOfficeName = salesManagerOfficeName;
	}

	public Long getSalesManagerReportingEmployeeId() {
		return salesManagerReportingEmployeeId;
	}

	public void setSalesManagerReportingEmployeeId(
			Long salesManagerReportingEmployeeId) {
		this.salesManagerReportingEmployeeId = salesManagerReportingEmployeeId;
	}

	public String getSalesManagerFullName() {
		return salesManagerFullName;
	}

	public void setSalesManagerFullName(String salesManagerFullName) {
		this.salesManagerFullName = salesManagerFullName;
	}

	public String getSalesManagerTimeZone() {
		return salesManagerTimeZone;
	}

	public void setSalesManagerTimeZone(String salesManagerTimeZone) {
		this.salesManagerTimeZone = salesManagerTimeZone;
	}

	public String getSalesManagerEmailId() {
		return salesManagerEmailId;
	}

	public void setSalesManagerEmailId(String salesManagerEmailId) {
		this.salesManagerEmailId = salesManagerEmailId;
	}

	public UUID getConfigurationGroupId() {
		return configurationGroupId;
	}

	public void setConfigurationGroupId(UUID configurationGroupId) {
		this.configurationGroupId = configurationGroupId;
	}

}