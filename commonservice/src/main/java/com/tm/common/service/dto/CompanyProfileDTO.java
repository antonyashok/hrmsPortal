package com.tm.common.service.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.common.domain.CompanyProfile.ActiveFlagEnum;

@JsonInclude(Include.NON_NULL)
public class CompanyProfileDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 6714322910518264355L;

	private Long companyProfileId;
	private String companyName;
	private String companyInfoNumber;
	private String companyAddress;
	private ActiveFlagEnum activeFlag;
	private Date createdDate;
	private Date updatedDate;
	private String createdBy;
	private String updatedBy;
	private String companyProfileImageId;
	private List<OfficeLocationDTO> officeList;
	
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

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
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
	
	public List<OfficeLocationDTO> getOfficeList() {
		return officeList;
	}

	public void setOfficeList(List<OfficeLocationDTO> officeList) {
		this.officeList = officeList;
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
