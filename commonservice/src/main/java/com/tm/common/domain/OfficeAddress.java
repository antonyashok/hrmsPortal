package com.tm.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "office_address")
public class OfficeAddress implements Serializable {

	private static final long serialVersionUID = -4210231940784597683L;
	
	public enum ActiveFlagEnum {
		Y, N
	}
	
	@Id
	@Column(name = "ofc_id")
	private Long officeId;
	
	@Column(name = "addr_id")
	private Long addressId;
		
	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private ActiveFlagEnum activeFlag = ActiveFlagEnum.Y;
	
	@CreatedDate
	@Column(name = "create_dt", nullable = false, updatable = false)
	@JsonIgnore
	private Date createdDate = new Date();

	@LastModifiedDate
	@Column(name = "last_updt_dt")
	@JsonIgnore
	private Date lastModifiedDate = new Date();

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
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

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	
}
