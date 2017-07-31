package com.tm.common.employee.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "comm_info")
public class CommonInfo implements Serializable {

	private static final long serialVersionUID = -5750289266518685921L;

	public enum activeFlagEnum {
		Y, N
	}

	public enum contactTypeEnum {
		email, cell, phone, fax
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comm_info_id")
	private long commonInfoId;

	@Column(name = "cntct_id")
	private long contactId;

	@Enumerated(EnumType.STRING)
	@Column(name = "cntct_typ")
	private contactTypeEnum contactType;

	@Column(name = "cntct_info")
	private String contactInfo;

	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private activeFlagEnum activeFlag = activeFlagEnum.Y;

	@Column(name = "create_dt")
	private String createDate;

	@Column(name = "last_updt_dt")
	private String lastUpdateDate;

	public long getCommonInfoId() {
		return commonInfoId;
	}

	public void setCommonInfoId(long commonInfoId) {
		this.commonInfoId = commonInfoId;
	}

	public long getContactId() {
		return contactId;
	}

	public void setContactId(long contactId) {
		this.contactId = contactId;
	}

	public contactTypeEnum getContactType() {
		return contactType;
	}

	public void setContactType(contactTypeEnum contactType) {
		this.contactType = contactType;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public activeFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(activeFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

}
