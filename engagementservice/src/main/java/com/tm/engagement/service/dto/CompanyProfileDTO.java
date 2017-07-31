package com.tm.engagement.service.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CompanyProfileDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 6714322910518264355L;

	private static final String COMPANY_NAME_IS_REQUIRED = "Company Name is Required";
	private static final String COMPANY_NUMBER_IS_REQUIRED = "Company Number is Required";
	private static final String COMPANY_ADDRESS_IS_REQUIRED = "Company AddressDTO is Required";

	private Long companyProfileId;
	@NotBlank(message = COMPANY_NAME_IS_REQUIRED)
	private String companyName;
	@NotBlank(message = COMPANY_NUMBER_IS_REQUIRED)
	private String companyInfoNumber;
	@NotBlank(message = COMPANY_ADDRESS_IS_REQUIRED)
	private String companyAddress;
	private List<OfficeLocationDTO> officeList;
	private String activeFlag;
	private Date createdDate;
	private Date updatedDate;
	private String createdBy;
	private String updatedBy;
	private String companyProfileImageId;
	
	private List<CompanyLocationDTO> companyLocationDTO;
	private String serviceFor;
	private List<Long> officeIds;

	public Long getCompanyProfileId() {
		return companyProfileId;
	}

	public void setCompanyProfileId(Long companyProfileId) {
		this.companyProfileId = companyProfileId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyInfoNumber() {
		return companyInfoNumber;
	}

	public void setCompanyInfoNumber(String companyInfoNumber) {
		this.companyInfoNumber = companyInfoNumber;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public List<OfficeLocationDTO> getOfficeList() {
		return officeList;
	}

	public void setOfficeList(List<OfficeLocationDTO> officeList) {
		this.officeList = officeList;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCompanyProfileImageId() {
		return companyProfileImageId;
	}

	public void setCompanyProfileImageId(String companyProfileImageId) {
		this.companyProfileImageId = companyProfileImageId;
	}

	public List<CompanyLocationDTO> getCompanyLocationDTO() {
		return companyLocationDTO;
	}

	public void setCompanyLocationDTO(List<CompanyLocationDTO> companyLocationDTO) {
		this.companyLocationDTO = companyLocationDTO;
	}

	public String getServiceFor() {
		return serviceFor;
	}

	public void setServiceFor(String serviceFor) {
		this.serviceFor = serviceFor;
	}

	public List<Long> getOfficeIds() {
		return officeIds;
	}

	public void setOfficeIds(List<Long> officeIds) {
		this.officeIds = officeIds;
	}
	
}
