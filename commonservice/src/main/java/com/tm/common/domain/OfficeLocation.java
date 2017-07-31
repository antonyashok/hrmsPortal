package com.tm.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "office")
public class OfficeLocation implements Serializable {

	private static final long serialVersionUID = 8800085139573894010L;

	public enum ActiveFlag {
		Y, N
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ofc_id", nullable = false)
	private Long officeId;

	@Column(name = "ofc_nm", nullable = false)
	private String officeName;

	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private ActiveFlag activeFlag;
	
	@CreatedDate
	@Column(name = "create_dt")
	@JsonIgnore
	private Date createdDate = new Date();

	@LastModifiedDate
	@Column(name = "last_updt_dt")
	
	@JsonIgnore
	private Date lastModifiedDate = new Date();
	
	@Transient
	private String linkedLocations;

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public ActiveFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getLinkedLocations() {
		return linkedLocations;
	}

	public void setLinkedLocations(String linkedLocations) {
		this.linkedLocations = linkedLocations;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}
