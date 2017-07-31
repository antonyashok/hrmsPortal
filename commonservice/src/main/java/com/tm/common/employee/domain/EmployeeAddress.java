package com.tm.common.employee.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tm.common.employee.service.dto.AddressDTO;
import com.tm.common.engagement.domain.EmployeeProfile.ActiveFlagEnum;

@Entity
@IdClass(EmployeeAddress.class)
@Table(name = "employee_address")
public class EmployeeAddress implements Serializable {

	private static final long serialVersionUID = -1763000396815124576L;

	@Id
	@Column(name = "empl_id")
	private Long employeeId;

	@Id
	@Column(name = "addr_id")
	private Long addressId;

	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private ActiveFlagEnum activeFlag;

	@Transient
	private AddressDTO officeAddressDTO;

	@Transient
	private AddressDTO temporaryAddressDTO;

	@Transient
	private AddressDTO permanentAddressDTO;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
	}

	public AddressDTO getOfficeAddressDTO() {
		return officeAddressDTO;
	}

	public void setOfficeAddressDTO(AddressDTO officeAddressDTO) {
		this.officeAddressDTO = officeAddressDTO;
	}

	public AddressDTO getTemporaryAddressDTO() {
		return temporaryAddressDTO;
	}

	public void setTemporaryAddressDTO(AddressDTO temporaryAddressDTO) {
		this.temporaryAddressDTO = temporaryAddressDTO;
	}

	public AddressDTO getPermanentAddressDTO() {
		return permanentAddressDTO;
	}

	public void setPermanentAddressDTO(AddressDTO permanentAddressDTO) {
		this.permanentAddressDTO = permanentAddressDTO;
	}

}