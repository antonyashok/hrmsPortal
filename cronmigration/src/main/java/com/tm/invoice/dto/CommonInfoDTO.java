/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.cron.dto.TimesheetDTO.java
 * Author        : Annamalai L
 * Date Created  : Apr 13th, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.invoice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.common.domain.CompanyProfile;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonInfoDTO {
	 
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