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

import com.tm.common.engagement.domain.AbstractAuditingEntity;
import com.tm.common.engagement.domain.EmployeeProfile.ActiveFlagEnum;

@Entity
@Table(name = "employee_ctc")
public class EmployeeCTC extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emplctc_id")
	private long employeeCTCId;
	
	@Column(name = "empl_id")
	private long employeeId;

	@Column(name = "effective_from")
	private Date effectiveFrom;

	@Column(name = "ctc_amount")
	private Double ctcAmount;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private ActiveFlagEnum activeFlag= ActiveFlagEnum.Y;

	public long getEmployeeCTCId() {
		return employeeCTCId;
	}

	public void setEmployeeCTCId(long employeeCTCId) {
		this.employeeCTCId = employeeCTCId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Double getCtcAmount() {
		return ctcAmount;
	}

	public void setCtcAmount(Double ctcAmount) {
		this.ctcAmount = ctcAmount;
	}

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
	}

	

}
