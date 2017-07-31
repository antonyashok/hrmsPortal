package com.tm.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "office_location_view")
public class OfficeLocationView {
	
	@Id
	@Column(name = "ofc_id")
	public Long officeId;
	
	@Column(name = "ofc_nm")
	public String officeName;
	
	@Column(name = "addr_strt_1")
	public String addressOne;
	
	@Column(name = "addr_strt_2")
	public String addressTwo;
	
	@Column(name = "cntry_id")
	public Long countryId;
	
	@Column(name = "county")
	public String countryName;
	
	@Column(name = "st_prv_id")
	public Long stateId;
	
	@Column(name = "state")
	public String stateName;
	
	@Column(name = "city_id")
	public Long cityId;
	
	@Column(name = "city")
	public String cityName;
	
	@Column(name = "pstl_cd")
	public String postalCode;
	
	@Column(name = "contact_no")
	public String contactNo;

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

	public String getAddressOne() {
		return addressOne;
	}

	public void setAddressOne(String addressOne) {
		this.addressOne = addressOne;
	}

	public String getAddressTwo() {
		return addressTwo;
	}

	public void setAddressTwo(String addressTwo) {
		this.addressTwo = addressTwo;
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

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	
	
	
	
	
	

}
