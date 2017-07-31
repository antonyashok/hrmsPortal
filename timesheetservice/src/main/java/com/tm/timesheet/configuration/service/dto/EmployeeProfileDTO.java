package com.tm.timesheet.configuration.service.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "Employee", collectionRelation = "Employees")
public class EmployeeProfileDTO extends ResourceSupport implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6249337517554435018L;

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String primaryEmailId;
    private String employeeType;
    private Long roleId;
    private String roleName;
    private String jobTitleId;
    private String jobTitle;
    private Long locationId;
    private String locationName;
    private Date joiningDate;
    private Long managerEmployeeId;
    private Long reportingManagerId;
    private String reportingManagerName;
    private String reportingManagerEmailId;
    private Long salesManagerId;
    private String salesManagerName;
    private String salesManagerMailId;
    private String salesManagerTimeZone;
    private Long provinceId;
    private String provinceName;

    public EmployeeProfileDTO() {}


    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
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


}
