package com.tm.common.employee.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(EmployeeCommonInfo.class)
@Table(name="employee_comm_info")
public class EmployeeCommonInfo implements Serializable{

	private static final long serialVersionUID = 8033045585378217703L;
	
	public enum activeFlagEnum {
        Y, N
    }
	
	@Id
	@Column(name="empl_id")
	private Long employeeId;

	@Id
	@Column(name="comm_info_id")
	private Long commonInfoId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private activeFlagEnum activeFlag = activeFlagEnum.Y;
	
	@Column(name = "create_dt")
	private String createDate;
	
	@Column(name = "last_updt_dt")
	private String lastUpdateDate;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getCommonInfoId() {
		return commonInfoId;
	}

	public void setCommonInfoId(Long commonInfoId) {
		this.commonInfoId = commonInfoId;
	}

	public activeFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(activeFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
}
