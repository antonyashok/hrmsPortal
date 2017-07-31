package com.tm.engagement.service.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class CompanyLocationDTO implements Serializable {

	private static final long serialVersionUID = 2589653190377634873L;
	
	private Long officeId;
	private String officeName;
	private Long addressId;
	private Long officeAddressId;
	private long companyLocationId;
	private String addressOne;
	private String addressTwo;
	private String countryId;
	private String countryName;
	private String stateId;
	private String stateName;
	private String cityId;
	private String cityName;
	private String contactNo;
	private String zipcode;
	
	@JsonIgnore
	private List<Long> officeIds;
	
	private String isDelete = "Y";
	private String isActive = "Y";
	
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
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Long getOfficeAddressId() {
		return officeAddressId;
	}
	public void setOfficeAddressId(Long officeAddressId) {
		this.officeAddressId = officeAddressId;
	}
	public long getCompanyLocationId() {
		return companyLocationId;
	}
	public void setCompanyLocationId(long companyLocationId) {
		this.companyLocationId = companyLocationId;
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
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getStateId() {
		return stateId;
	}
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public List<Long> getOfficeIds() {
		return officeIds;
	}
	public void setOfficeIds(List<Long> officeIds) {
		this.officeIds = officeIds;
	}

	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	
}
