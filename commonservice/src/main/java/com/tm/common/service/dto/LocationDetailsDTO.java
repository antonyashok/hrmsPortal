package com.tm.common.service.dto;

import java.util.List;

import com.tm.common.domain.CompanyLocations;
import com.tm.common.domain.OfficeAddress;
import com.tm.common.domain.OfficeLocation;
import com.tm.common.employee.domain.Address;

public class LocationDetailsDTO {

	private List<OfficeLocation> officeLocations;
	private List<Address> addresses;
	private List<OfficeAddress> officeAddresses;
	private List<CompanyLocations> companyLocations;
	
	public List<OfficeLocation> getOfficeLocations() {
		return officeLocations;
	}
	public void setOfficeLocations(List<OfficeLocation> officeLocations) {
		this.officeLocations = officeLocations;
	}
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	public List<OfficeAddress> getOfficeAddresses() {
		return officeAddresses;
	}
	public void setOfficeAddresses(List<OfficeAddress> officeAddresses) {
		this.officeAddresses = officeAddresses;
	}
	public List<CompanyLocations> getCompanyLocations() {
		return companyLocations;
	}
	public void setCompanyLocations(List<CompanyLocations> companyLocations) {
		this.companyLocations = companyLocations;
	}	
}
