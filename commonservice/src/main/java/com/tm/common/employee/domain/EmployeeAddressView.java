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
@IdClass(EmployeeAddressView.class)
@Table(name = "employee_address_view")
public class EmployeeAddressView implements Serializable {

	private static final long serialVersionUID = -1763000396815124576L;

	@Id
	@Column(name = "addr_id")
	private Long addressId;
	
	@Column(name = "st_prv_id")
	private Long stateId;
	
	@Column(name = "cntry_id")
	private Long countryId;
	
	@Column(name = "city_id")
	private Long cityId;
	
	@Column(name = "addr_strt_1")
	private String firstAddress;
	
	@Column(name = "addr_strt_2")
	private String secondAddress;
	
	@Column(name = "pstl_cd")
	private String postalCode;
	
	@Column(name = "addr_typ")
	private String addressType;
	
	@Id
	@Column(name = "empl_id")
	private Long employeeId;

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getFirstAddress() {
		return firstAddress;
	}

	public void setFirstAddress(String firstAddress) {
		this.firstAddress = firstAddress;
	}

	public String getSecondAddress() {
		return secondAddress;
	}

	public void setSecondAddress(String secondAddress) {
		this.secondAddress = secondAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}


	

}