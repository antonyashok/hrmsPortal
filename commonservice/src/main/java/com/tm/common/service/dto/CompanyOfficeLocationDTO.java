package com.tm.common.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.common.domain.OfficeLocation;

@JsonInclude(Include.NON_NULL)
public class CompanyOfficeLocationDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 6714322910518264355L;

	private Long companyOfficeId;
	private Long companyProfileId;
	private OfficeLocation office;
	private String activeFlag;

	public Long getCompanyOfficeId() {
		return companyOfficeId;
	}

	public void setCompanyOfficeId(Long companyOfficeId) {
		this.companyOfficeId = companyOfficeId;
	}

	public Long getCompanyProfileId() {
		return companyProfileId;
	}

	public void setCompanyProfileId(Long companyProfileId) {
		this.companyProfileId = companyProfileId;
	}

	public OfficeLocation getOffice() {
		return office;
	}

	public void setOffice(OfficeLocation office) {
		this.office = office;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

}
