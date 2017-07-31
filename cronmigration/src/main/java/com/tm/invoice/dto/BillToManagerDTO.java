package com.tm.invoice.dto;

import java.io.Serializable;

public class BillToManagerDTO implements Serializable {

	private static final long serialVersionUID = 2533454825921454064L;

	private String billToMgrName;

	private String billToManagerEmailId;

	private String billAddress;

	private String countryName;

	private String stateName;

	private String cityName;

	private String postalCode;

	public String getBillToMgrName() {
		return billToMgrName;
	}

	public void setBillToMgrName(String billToMgrName) {
		this.billToMgrName = billToMgrName;
	}

	public String getBillToManagerEmailId() {
		return billToManagerEmailId;
	}

	public void setBillToManagerEmailId(String billToManagerEmailId) {
		this.billToManagerEmailId = billToManagerEmailId;
	}

	

	public String getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(String billAddress) {
		this.billAddress = billAddress;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
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

	
	
}
