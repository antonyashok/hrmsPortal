package com.tm.common.employee.service.dto;

import java.io.Serializable;

public class EmployeeAddressDTO implements Serializable {

	private static final long serialVersionUID = -1763000396815124576L;

	private Long employeeId;

	private Long addressId;

	private String activeFlag;

	private AddressDTO officeAddressDTO;

	private AddressDTO temporaryAddressDTO;

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

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
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

	public AddressDTO getOfficeAddressDTO() {
		return officeAddressDTO;
	}

	public void setOfficeAddressDTO(AddressDTO officeAddressDTO) {
		this.officeAddressDTO = officeAddressDTO;
	}

}