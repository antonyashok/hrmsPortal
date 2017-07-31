package com.tm.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "company_office_locations")
public class CompanyOfficeLocation implements Serializable {

	private static final long serialVersionUID = -4020530904809534616L;

	public enum ActiveFlagEnum {
		Y, N
	}

	@Id
	@GeneratedValue
	@Column(name = "company_office_id", nullable = false)
	private Long companyOfficeId;

	@Column(name = "company_profile_id")
	private Long companyProfileId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "office_id", nullable = false)
	private OfficeLocation office;

	@Enumerated(EnumType.STRING)
	@Column(name = "active_flag")
	private ActiveFlagEnum activeFlag = ActiveFlagEnum.Y;

	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Transient
	private String linkedLocations;

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

	public String getLinkedLocations() {
		return linkedLocations;
	}

	public void setLinkedLocations(String linkedLocations) {
		this.linkedLocations = linkedLocations;
	}

}
