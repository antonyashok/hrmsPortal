package com.tm.invoice.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.common.domain.CompanyProfile;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientInfoDTO implements Serializable {
  
  private static final long serialVersionUID = -2017563873484904788L;

	 
	CompanyProfile companyProfile;
	
	EmployeeAttachmentsDTO employeeAttachmentsDTO;

	public CompanyProfile getCompanyProfile() {
		return companyProfile;
	}

	public void setCompanyProfile(CompanyProfile companyProfile) {
		this.companyProfile = companyProfile;
	}

	public EmployeeAttachmentsDTO getEmployeeAttachmentsDTO() {
		return employeeAttachmentsDTO;
	}

	public void setEmployeeAttachmentsDTO(EmployeeAttachmentsDTO employeeAttachmentsDTO) {
		this.employeeAttachmentsDTO = employeeAttachmentsDTO;
	}
	
	

}
