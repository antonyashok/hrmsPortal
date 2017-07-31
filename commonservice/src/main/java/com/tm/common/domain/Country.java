package com.tm.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "country")
@JsonIgnoreProperties({ "createDate", "lastUpdateDate" })
public class Country implements Serializable {

	private static final long serialVersionUID = 7797825804423266064L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cntry_id", nullable = false)
	private long countryId;
	@Column(name = "cntry_nm", nullable = false)
	private String countryName;
	@Column(name = "currcy_id")
	private int currencyId;
	@Column(name = "cntry_desc")
	private String countryDescription;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false)
	private Date createDate;
	@Column(name = "last_updt_dt")
	private Date lastUpdateDate;
	@Transient
	private String isActiveFlg = "N";

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	public String getCountryDescription() {
		return countryDescription;
	}

	public void setCountryDescription(String countryDescription) {
		this.countryDescription = countryDescription;
	}

	public String getIsActiveFlg() {
		return isActiveFlg;
	}

	public void setIsActiveFlg(String isActiveFlg) {
		this.isActiveFlg = isActiveFlg;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

}
