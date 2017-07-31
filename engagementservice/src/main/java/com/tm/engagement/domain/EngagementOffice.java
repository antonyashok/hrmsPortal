package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "engagement_office_locations")
public class EngagementOffice implements Serializable {

	private static final long serialVersionUID = 4732511727240467372L;

	public enum ActiveFlagEnum {
		Y, N
	}

	@Id
	@GeneratedValue
	@Column(name = "engagement_office_id", nullable = false)
	private Long engmtOfficeId;

	@Column(name = "engmt_id")
	@Type(type = "uuid-char")
	private UUID engagementId;

	@Column(name = "office_id")
	private Long officeId;
	
	@Column(name = "cust_loc_id")
	@Type(type = "uuid-char")
	private UUID customerLocationId;

	@Enumerated(EnumType.STRING)
	@Column(name = "active_flag")
	private ActiveFlagEnum activeFlag = ActiveFlagEnum.Y;

	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Transient
	private String linkedLocations;

	public Long getEngmtOfficeId() {
		return engmtOfficeId;
	}

	public void setEngmtOfficeId(Long engmtOfficeId) {
		this.engmtOfficeId = engmtOfficeId;
	}

	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}
	
	public UUID getCustomerLocationId() {
		return customerLocationId;
	}

	public void setCustomerLocationId(UUID customerLocationId) {
		this.customerLocationId = customerLocationId;
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