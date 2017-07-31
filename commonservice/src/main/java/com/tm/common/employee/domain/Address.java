package com.tm.common.employee.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tm.common.engagement.domain.EmployeeProfile.ActiveFlagEnum;


@Entity
@Table(name = "address")
public class Address implements Serializable {

	private static final long serialVersionUID = -1763000396815124576L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	
	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private ActiveFlagEnum activeFlag;
	
	@Column(name = "county")
	private String country;
	
	@Column(name = "addr_unit")
	private String addressUnit;
	
	@Column(name = "addr_typ")
	private String addressType;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "contact_no")
	private String contactNumber;
	
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddressUnit() {
		return addressUnit;
	}

	public void setAddressUnit(String addressUnit) {
		this.addressUnit = addressUnit;
	}

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	
	

	
}
