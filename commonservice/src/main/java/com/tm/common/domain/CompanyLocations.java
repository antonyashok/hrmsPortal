package com.tm.common.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company_locations")
public class CompanyLocations implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4570410331299398519L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "company_location_id")
	private Long companyLocationId;

	@Column(name = "office_id")
	private Long officeId;

	@Column(name = "company_id")
	private Long companyId;

	public Long getCompanyLocationId() {
		return companyLocationId;
	}

	public void setCompanyLocationId(Long companyLocationId) {
		this.companyLocationId = companyLocationId;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

}
