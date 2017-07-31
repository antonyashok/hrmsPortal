package com.tm.common.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.common.service.dto.CompanyLocationDTO;
import com.tm.common.service.dto.OfficeLocationDTO;

@Entity
@Table(name = "company_profile")
@JsonInclude(value = Include.NON_NULL)
public class CompanyProfile implements Serializable {

	private static final long serialVersionUID = -4049849541314027178L;

	public enum ActiveFlagEnum {
		Y, N
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "company_profile_id", nullable = false)
	private Long companyProfileId;

	@Column(name = "company_name", nullable = false)
	private String companyName;

	@Column(name = "company_info_number", nullable = false)
	private String companyInfoNumber;

	@Column(name = "company_address", nullable = false)
	private String companyAddress;

	@Enumerated(EnumType.STRING)
	@Column(name = "active_flag")
	private ActiveFlagEnum activeFlag;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "updated_date", insertable = false)
	private Date updatedDate;

	@Column(name = "created_by")
	private Long createdBy;

	@Column(name = "updated_by")
	private Long updatedBy;

	@Column(name = "company_profile_image_id")
	private String companyProfileImageId;

	@Transient
	private List<OfficeLocationDTO> officeList;
	
	@Transient
	private List<CompanyLocationDTO> companyLocationDTO;

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

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
	}

	public List<OfficeLocationDTO> getOfficeList() {
		return officeList;
	}

	public void setOfficeList(List<OfficeLocationDTO> officeList) {
		this.officeList = officeList;
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

}
