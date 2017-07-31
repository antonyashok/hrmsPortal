package com.tm.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rctr_prfl_view")
public class RecruiterProfileView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1023393268546921986L;

	@Id
	@Column(name = "employeeId")
	@GeneratedValue
	private Long employeeId;

	@Column(name = "emp_name")
	private String employeeName;

	@Column(name = "roleId")
	private Long roleId;

	@Column(name = "roleName")
	private String roleName;

	@Column(name = "officeId")
	private Long officeId;

	@Column(name = "officeName")
	private String officeName;

	@Column(name = "accountManagerId")
	private Long accountManagerId;

	@Column(name = "salesManagerId")
	private Long salesManagerId;

	@Column(name = "rcrtCntctInfo")
	private String rcrtCntctInfo;

	@Column(name = "accntMngrCntctInfo")
	private String accntMngrCntctInfo;

	@Column(name = "hourly_flg")
	private String hourlyFlg;

	@Column(name = "actv_flg")
	private String activeFlag;

	@Column(name = "empl_strt_dt")
	private Date joiningDate;
	
	@Column(name = "email")
	private String employeeEmailId;
	
	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
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

	public Long getAccountManagerId() {
		return accountManagerId;
	}

	public void setAccountManagerId(Long accountManagerId) {
		this.accountManagerId = accountManagerId;
	}

	public Long getSalesManagerId() {
		return salesManagerId;
	}

	public void setSalesManagerId(Long salesManagerId) {
		this.salesManagerId = salesManagerId;
	}

	public String getRcrtCntctInfo() {
		return rcrtCntctInfo;
	}

	public void setRcrtCntctInfo(String rcrtCntctInfo) {
		this.rcrtCntctInfo = rcrtCntctInfo;
	}

	public String getAccntMngrCntctInfo() {
		return accntMngrCntctInfo;
	}

	public void setAccntMngrCntctInfo(String accntMngrCntctInfo) {
		this.accntMngrCntctInfo = accntMngrCntctInfo;
	}

	public String getHourlyFlg() {
		return hourlyFlg;
	}

	public void setHourlyFlg(String hourlyFlg) {
		this.hourlyFlg = hourlyFlg;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getEmployeeEmailId() {
		return employeeEmailId;
	}

	public void setEmployeeEmailId(String employeeEmailId) {
		this.employeeEmailId = employeeEmailId;
	}
	
}