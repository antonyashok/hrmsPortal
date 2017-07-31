package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="employee_engagement_office__detail_view")
public class EmployeeEngagementOfficeDetailView implements Serializable {

    private static final long serialVersionUID = 510265652586899236L;

    @Column(name="engagement_office_id")
    @Id
	private Long engagementOfficeId;

	@Column(name="engmt_id")
	@Type(type = "uuid-char")
	private UUID engagementId;

	@Column(name="ofc_nm")
	private String officeName;

	@Column(name="office_id")
	private Long officeId;
	
	@Column(name="country_nm")
	private String countryName;
	
	@Column(name="cntry_id")
    private Long countryId;

    public Long getEngagementOfficeId() {
        return engagementOfficeId;
    }

    public void setEngagementOfficeId(Long engagementOfficeId) {
        this.engagementOfficeId = engagementOfficeId;
    }

    public UUID getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(UUID engagementId) {
        this.engagementId = engagementId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

}